package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.service.JotformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api/jotforms")
public class JotformController {

    @Autowired
    private JotformService jotformService;


    @GetMapping
    public List<Jotform> getAllJotforms() {
        return jotformService.getAllJotforms();
    }

    @PostMapping
    public ResponseEntity<Jotform> createJotform(@RequestBody Map<String, Object> data) {
        Jotform savedJotform = jotformService.saveJotform(data);
        return ResponseEntity.ok(savedJotform);
    }

    /**
     * DELETE /api/jotforms/{id} - Deletes a specific Jotform by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJotform(@PathVariable Long id) {
        try {
            jotformService.deleteJotformById(id);
            return ResponseEntity.noContent().build(); // HTTP 204: Success, no content to return
        } catch (Exception e) {
            // This will catch the EntityNotFoundException from the service
            return ResponseEntity.notFound().build(); // HTTP 404: Not Found
        }
    }

    // ----- Other Endpoints -----

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllJotformNames() {
        List<String> names = jotformService.getAllJotformNames();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/react")
    public Jotform getreact() {
        return jotformService.getreact();
    }


}
