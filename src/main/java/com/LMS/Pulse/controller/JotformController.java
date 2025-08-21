// Updated JotformController.java
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
@CrossOrigin(origins = "http://localhost:5173") // Enable CORS for this origin
public class JotformController {

    @Autowired
    private JotformService jotformService;

    @PostMapping
    public ResponseEntity<Jotform> createJotform(@RequestBody Map<String, Object> data) {
        Jotform savedJotform = jotformService.saveJotform(data);
        return ResponseEntity.ok(savedJotform);
    }

    // New endpoint to get all Jotform names
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllJotformNames() {
        List<String> names = jotformService.getAllJotformNames();
        return ResponseEntity.ok(names);
    }
}
