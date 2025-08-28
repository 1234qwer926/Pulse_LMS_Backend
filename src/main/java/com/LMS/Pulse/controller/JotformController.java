package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.service.JotformService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Jotform entities.
 * Provides endpoints for creating, retrieving, and deleting Jotforms.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/jotforms") // Base path for all endpoints in this controller
public class JotformController {

    @Autowired
    private JotformService jotformService;

    /**
     * GET /api/jotforms : Retrieves a list of all Jotforms.
     *
     * @return A list of all Jotform entities.
     */
    @GetMapping
    public List<Jotform> getAllJotforms() {
        return jotformService.getAllJotforms();
    }

    /**
     * POST /api/jotforms : Creates a new Jotform from the given data.
     *
     * @param data A map representing the JSON structure of the new Jotform.
     * @return A ResponseEntity containing the saved Jotform with an HTTP 200 OK status.
     */
    @PostMapping
    public ResponseEntity<Jotform> createJotform(@RequestBody Map<String, Object> data) {
        Jotform savedJotform = jotformService.saveJotform(data);
        return ResponseEntity.ok(savedJotform);
    }

    /**
     * DELETE /api/jotforms/{id} : Deletes a specific Jotform by its ID.
     *
     * @param id The ID of the Jotform to delete.
     * @return A ResponseEntity with an HTTP 204 No Content status on success,
     *         or an HTTP 404 Not Found status if the Jotform does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJotform(@PathVariable Long id) {
        try {
            jotformService.deleteJotformById(id);
            return ResponseEntity.noContent().build(); // HTTP 204: Success, no content
        } catch (EntityNotFoundException e) {
            // This exception is thrown by the service if the entity doesn't exist.
            return ResponseEntity.notFound().build(); // HTTP 404: Not Found
        }
    }

    // ----- Other Optional Endpoints -----

    /**
     * GET /api/jotforms/names : Retrieves a distinct list of all Jotform names.
     *
     * @return A ResponseEntity containing the list of names with an HTTP 200 OK status.
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllJotformNames() {
        List<String> names = jotformService.getAllJotformNames();
        return ResponseEntity.ok(names);
    }

    /**
     * GET /api/jotforms/react : A sample endpoint to retrieve a specific Jotform.
     *
     * @return The Jotform with ID 1, or null if not found.
     */
    @GetMapping("/react")
    public Jotform getreact() {
        return jotformService.getreact();
    }
}
