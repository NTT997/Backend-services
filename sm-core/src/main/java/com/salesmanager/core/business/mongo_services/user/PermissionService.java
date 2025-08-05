package com.salesmanager.core.business.mongo_services.user;

import java.util.List;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.mongo_generic.SalesManagerEntityService;
import com.salesmanager.core.mongo_model.user.Group;
import com.salesmanager.core.mongo_model.user.Permission;
import com.salesmanager.core.mongo_model.user.PermissionCriteria;
import com.salesmanager.core.mongo_model.user.PermissionList;



public interface PermissionService extends SalesManagerEntityService<Integer, Permission> {

  List<Permission> getByName();

  List<Permission> listPermission() throws ServiceException;

  Permission getById(Integer permissionId);

  List<Permission> getPermissions(List<Integer> groupIds) throws ServiceException;

  void deletePermission(Permission permission) throws ServiceException;

  PermissionList listByCriteria(PermissionCriteria criteria) throws ServiceException;

  void removePermission(Permission permission, Group group) throws ServiceException;

}
