package com.postnord.simulation_service.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.postnord.simulation_service.config.FlexsimProperties;
import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.util.FlexsimPathUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlexsimScriptService {

    private final FlexsimProperties flexsimProperties;

    public String createOrUpdateScript(ExperimentDto experimentDto) throws IOException {
        Path scriptDir = Paths.get(FlexsimPathUtil.getRootDir(), "flexsim", "script");
        Files.createDirectories(scriptDir);

        Long experimentId = experimentDto.getExperimentId();
        Path scriptPath = scriptDir.resolve("script_" + experimentId + ".txt"); // one file per experiment, safe under concurrency

        
        String callbackHost = flexsimProperties.getCallbackHost(); // was hardcoded "127.0.0.1"
        int callbackPort = flexsimProperties.getCallbackPort();     // was hardcoded 8080

        String parcelFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId, callbackHost, callbackPort,
                "/api/parcels/getParcels/" + experimentId, "Parcel_Data");

        String infeedResourceFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId, callbackHost, callbackPort,
                "/api/resourceDetails/getInfeedDetails/" + experimentId, "Infeed_Resource_Details");

        String zoneResourceFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId, callbackHost, callbackPort,
                "/api/resourceDetails/getZoneDetails/" + experimentId, "Zone_Resource_Details");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");
        String startTime = experimentDto.getStartTime().format(formatter);
        String stopTime = experimentDto.getEndTime().plusMinutes(15).format(formatter);

        String setFlexsimStartTime = String.format("""
                DateTime startTime = DateTime("%s","%%m/%%d/%%Y %%H:%%M:%%S");
                function_s(getmodelunit(START_TIME_NODE), "setDateTime", startTime);
                """, startTime);

        String setFlexsimStopTime = String.format("""
                DateTime stopTime = DateTime("%s","%%m/%%d/%%Y %%H:%%M:%%S");
                int s = stopTime.totalSeconds - startTime.totalSeconds;
                stoptime(s);
                """, stopTime);

        String simulationStartingFunction = String.format(
                "updateSimulationStatus(%d, \"%s\", %d, \"%s\", \"%s\", \"%s\")",
                experimentId, callbackHost, callbackPort, "/api/flexsim/updateSimulationStage",
                ExperimentRunStage.STARTING_SIMULATION, "Starting Simulation");

        String simulationStartedFunction = String.format(
                "updateSimulationStatus(%d, \"%s\", %d, \"%s\", \"%s\", \"%s\")",
                experimentId, callbackHost, callbackPort, "/api/flexsim/updateSimulationStage",
                ExperimentRunStage.SIMULATION_STARTED, "Simulation Started");

        String script = String.format("""
                %s;
                %s;
                %s;
                %s
                %s
                %s;
                MAXRECIRCULATION = %d;
                resetmodel();
                runspeed(4);
                go();
                %s;
                """,
                parcelFunction, infeedResourceFunction, zoneResourceFunction,
                setFlexsimStartTime, setFlexsimStopTime, simulationStartingFunction,
                experimentDto.getMaxRecirculationCount(), simulationStartedFunction);

        Files.write(scriptPath, script.getBytes(StandardCharsets.UTF_8));
        return scriptPath.toString();
    }
}
