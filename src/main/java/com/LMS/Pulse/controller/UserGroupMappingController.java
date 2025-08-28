package com.LMS.Pulse.controller;// package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.UserGroupMapping;
import com.LMS.Pulse.service.UserGroupMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // Fine for testing without security
@RequestMapping("/api/mappings") // Base path for this controller
// Allow requests from your React app
public class UserGroupMappingController {

    @Autowired
    private UserGroupMappingService userGroupMappingService;

    @PostMapping("/map-user")
    public ResponseEntity<?> mapUserToGroup(@RequestBody UserGroupMapping request) {
        try {
            // Create a new mapping entity from the request
            UserGroupMapping newMapping = new UserGroupMapping();
            newMapping.setEmail(request.getEmail());
            // The frontend sends 'group', so we map it to 'groupName'
            newMapping.setGroupName(request.getGroupName());

            UserGroupMapping savedMapping = userGroupMappingService.mapUserToGroup(newMapping);

            // Return a success response with the saved object
            return ResponseEntity.ok(savedMapping);
        } catch (Exception e) {
            // Return a generic error response
            return ResponseEntity.badRequest().body("Error mapping user: " + e.getMessage());
        }
    }
}
