package com.salesmanager.core.business.mongo_repositories.users;


import com.salesmanager.core.model.user.PermissionCriteria;
import com.salesmanager.core.model.user.PermissionList;




public interface PermissionRepositoryCustom {

	PermissionList listByCriteria(PermissionCriteria criteria);


}
