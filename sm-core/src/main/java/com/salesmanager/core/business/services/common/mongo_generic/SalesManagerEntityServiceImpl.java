package com.salesmanager.core.business.services.common.mongo_generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.mongo_repositories.users.GroupRepository;
import com.salesmanager.core.business.mongo_repositories.users.UserRepository;
import com.salesmanager.core.mongo_model.generic.SalesManagerEntity;

/**
 * @param <T> entity type
 */
public abstract class SalesManagerEntityServiceImpl<K extends Serializable & Comparable<K>, E extends SalesManagerEntity<K, ?>>
	implements SalesManagerEntityService<K, E> {
	
	/**
	 * Classe de l'entité, déterminé à partir des paramètres generics.
	 */
	private Class<E> objectClass;


    private MongoRepository<E, K> repository;
    
	@SuppressWarnings("unchecked")
	public SalesManagerEntityServiceImpl(MongoRepository<E, K> repository) {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.objectClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
		this.repository = repository;
	}
	
	public SalesManagerEntityServiceImpl(GroupRepository groupRepository) {
		//TODO Auto-generated constructor stub
	}

	protected final Class<E> getObjectClass() {
		return objectClass;
	}


//	public E getById(K id) {
//		return repository.getOne(id);
//	}
	
	public E getById(K id) {
		/*
		 * Changed from repository.getOne(id) to repository.findById(id).orElse(null)
		 * Reason: - repository.getOne(id) returns a lazy proxy and does NOT hit
		 * the database immediately. - If the entity is not found, it only throws
		 * EntityNotFoundException later when accessing any field, which can cause
		 * unexpected 500 Internal Server Error in REST APIs. - This makes exception
		 * handling and debugging harder in service layers that expect immediate
		 * validation.
		 * 
		 * By using repository.findById(id).orElse(null): - We perform an
		 * immediate lookup (eager fetch), allowing us to detect missing records
		 * explicitly. - This enables proper use of custom exceptions (e.g.
		 * ResourceNotFoundException), which are correctly mapped to 404 responses by
		 * our @ControllerAdvice error handler. - It improves API robustness and
		 * predictability, especially in delete/update operations.
		 */
	    return repository.findById(id).orElse(null);
	}

	
	public void save(E entity) throws ServiceException {
		//repository.saveAndFlush(entity);
	}
	
	public void saveAll(Iterable<E> entities) throws ServiceException {
		repository.saveAll(entities);
	}
	
	
	public void create(E entity) throws ServiceException {
		save(entity);
	}

	
	
	public void update(E entity) throws ServiceException {
		save(entity);
	}
	

	public void delete(E entity) throws ServiceException {
		repository.delete(entity);
	}
	
	
	public void flush() {
		//repository.flush();
	}
	

	
	public List<E> list() {
		return repository.findAll();
	}
	

	public Long count() {
		return repository.count();
	}
	
	protected E saveAndFlush(E entity) {
		//return repository.saveAndFlush(entity);
		return repository.save(entity);
	}

}