package com.LMS.Pulse.service;// package com.LMS.Pulse.service;

import com.LMS.Pulse.model.UserGroupMapping;
import com.LMS.Pulse.repository.UserGroupMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupMappingService {

    @Autowired
    private UserGroupMappingRepository userGroupMappingRepository;

    /**
     * Saves a user-to-group mapping.
     * @param mapping The UserGroupMapping object containing email and groupName.
     * @return The saved UserGroupMapping entity.
     */
    public UserGroupMapping mapUserToGroup(UserGroupMapping mapping) {
        // You can add business logic here, e.g.,
        // - Check if the user email already exists.
        // - Validate if the groupName is a valid group.
        // - Prevent duplicate mappings.

        return userGroupMappingRepository.save(mapping);
    }
}
