package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.UserGroupMapping;
import com.LMS.Pulse.service.UserGroupMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Allows all origins, adjust for production
@RequestMapping("/api/user-mappings")
public class UserGroupMappingController {

    @Autowired
    private UserGroupMappingService service;

    @PostMapping
    public ResponseEntity<UserGroupMapping> createMapping(@RequestBody UserGroupMapping mapping) {
        return ResponseEntity.ok(service.mapUserToGroup(mapping));
    }

    @GetMapping
    public ResponseEntity<List<UserGroupMapping>> getAllMappings() {
        return ResponseEntity.ok(service.getAllMappings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMapping(@PathVariable Long id) {
        try {
            service.deleteMapping(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
