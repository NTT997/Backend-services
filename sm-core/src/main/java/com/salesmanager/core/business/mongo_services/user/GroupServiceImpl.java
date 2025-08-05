package com.salesmanager.core.business.mongo_services.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.mongo_repositories.users.GroupRepository;
import com.salesmanager.core.business.services.common.mongo_generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.mongo_model.user.Group;
import com.salesmanager.core.mongo_model.user.GroupType;


// @Service("mongogroupService")
public class GroupServiceImpl extends SalesManagerEntityServiceImpl<Integer, Group>
    implements GroupService {

  GroupRepository groupRepository;


  @Inject
  public GroupServiceImpl(GroupRepository groupRepository) {
    super(groupRepository);
    this.groupRepository = groupRepository;

  }


  @Override
  public List<Group> listGroup(GroupType groupType) throws ServiceException {
    try {
      return groupRepository.findByType(groupType);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  public List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException {

      try {
        return ids.isEmpty() ? new ArrayList<Group>() : groupRepository.findByIds(ids);
      } catch (Exception e) {
        throw new ServiceException(e);
      }

  }


  @Override
  public Group findByName(String groupName) throws ServiceException {
    return groupRepository.findByGroupName(groupName);
  }


  @Override
  public List<Group> listGroupByNames(List<String> names) throws ServiceException {
    return groupRepository.findByNames(names);
  }


}
