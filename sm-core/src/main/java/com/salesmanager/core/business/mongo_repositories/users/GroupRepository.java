package com.salesmanager.core.business.mongo_repositories.users;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.salesmanager.core.mongo_model.user.Group;


public interface GroupRepository extends  MongoRepository<Group, Integer> {


	Group findById(Long id);
	
	@Query("select distinct g from Group as g left join fetch g.permissions perms order by g.id")
	List<Group> findAll();
	
	@Query("select distinct g from Group as g left join fetch g.permissions perms where perms.id in (?1) ")
	List<Group> findByPermissions(Set<Integer> permissionIds);
	
	@Query("select distinct g from Group as g left join fetch g.permissions perms where g.id in (?1) ")
	List<Group> findByIds(Set<Integer> groupIds);
	
	@Query("select distinct g from Group as g left join fetch g.permissions perms where g.groupName in (?1) ")
	List<Group> findByNames(List<String> groupeNames);
	
	@Query("select distinct g from Group as g left join fetch g.permissions perms where g.groupType = ?1")
	List<Group> findByType(com.salesmanager.core.mongo_model.user.GroupType groupType);
	
	@Query("select g from Group as g left join fetch g.permissions perms where g.groupName =?1")
	Group findByGroupName(String name);
}
