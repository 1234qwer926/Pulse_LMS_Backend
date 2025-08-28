package com.LMS.Pulse.service;

import com.LMS.Pulse.model.UserGroupMapping;
import com.LMS.Pulse.repository.UserGroupMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserGroupMappingService {

    @Autowired
    private UserGroupMappingRepository repository;

    public UserGroupMapping mapUserToGroup(UserGroupMapping mapping) {
        return repository.save(mapping);
    }

    public List<UserGroupMapping> getAllMappings() {
        return repository.findAll();
    }

    public void deleteMapping(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Mapping not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
