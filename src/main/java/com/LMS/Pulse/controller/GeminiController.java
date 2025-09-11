package com.LMS.Pulse.controller;

import com.LMS.Pulse.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    public String askGeminiApi(@RequestBody String prompt) {
        return geminiService.askGemini(prompt);
    }
}
