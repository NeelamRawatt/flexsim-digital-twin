package com.example.flexsim_simulation_service.service.experiment;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentProgressEventDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExperimentProgressSseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long experimentId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(experimentId, emitter);

        emitter.onCompletion(() -> emitters.remove(experimentId));
        emitter.onTimeout(() -> emitters.remove(experimentId));
        emitter.onError((ex) -> emitters.remove(experimentId));

        send(experimentId, ExperimentProgressEventDto.builder()
                .experimentId(experimentId)
                .status("RUNNING")
                .stage("CONNECTED")
                .message("Connected")
                .build());

        return emitter;
    }

    public void send(Long experimentId, ExperimentProgressEventDto event) {
        SseEmitter emitter = emitters.get(experimentId);
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("simulation-progress")
                    .data(event));
        } catch (IOException ex) {
            emitters.remove(experimentId);
            emitter.completeWithError(ex);
        }
    }

    public void complete(Long experimentId) {
        SseEmitter emitter = emitters.remove(experimentId);
        if (emitter != null) {
            emitter.complete();
        }
    }
}