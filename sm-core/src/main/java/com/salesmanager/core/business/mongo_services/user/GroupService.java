package com.salesmanager.core.business.mongo_services.user;

import java.util.List;
import java.util.Set;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.mongo_generic.SalesManagerEntityService;
import com.salesmanager.core.mongo_model.user.Group;
import com.salesmanager.core.mongo_model.user.GroupType;


public interface GroupService extends SalesManagerEntityService<Integer, Group> {


	List<Group> listGroup(GroupType groupType) throws ServiceException;
	List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException;
	List<Group> listGroupByNames(List<String> names) throws ServiceException;
	Group findByName(String groupName) throws ServiceException;

}
