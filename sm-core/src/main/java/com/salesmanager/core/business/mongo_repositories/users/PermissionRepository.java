package com.salesmanager.core.business.mongo_repositories.users;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.salesmanager.core.mongo_model.user.Permission;


public interface PermissionRepository extends MongoRepository<Permission, Integer>, PermissionRepositoryCustom {

	
	//@Query("select p from Permission as p where p.id = ?1")
	@Query("{id :?0}")  
	Permission findOne(Integer id);
	
	// @Query("select p from Permission as p order by p.id")
	List<Permission> findAll();
	
	// @Query("select distinct p from Permission as p join fetch p.groups groups where groups.id in (?1)")
	List<Permission> findByGroups(Set<Integer> groupIds);
}
