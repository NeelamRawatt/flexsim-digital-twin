package com.postnord.simulation_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.postnord.simulation_service.service.ExperimentProgressSseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flexsim")
public class FlexsimSimulationSseController {
    private final ExperimentProgressSseService experimentProgressSseService;

    @GetMapping(value = "/progress/stream/{experimentId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamProgress(@PathVariable Long experimentId) {
        return experimentProgressSseService.subscribe(experimentId);
    }
}
