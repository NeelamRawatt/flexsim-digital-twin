package com.example.flexsim_simulation_service.service;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.util.FlexsimPathUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FlexsimScriptService {

    public String createOrUpdateScript(ExperimentDto experimentDto) throws IOException {
        Path scriptDir = Paths.get(FlexsimPathUtil.getRootDir(), "flexsim", "script");

        Files.createDirectories(scriptDir);

        Path scriptPath = scriptDir.resolve("script.txt");

        Long experimentId = experimentDto.getExperimentId();
        String localhostURI = "127.0.0.1";
        Integer port = 8080;
        String parcelDataPath = "/api/parcels/getParcels/" + experimentId;
        String parcelDataTableName = "Parcel_Data";
        String infeedResourceDetailPath = "/api/resourceDetails/getInfeedDetails/" + experimentId;
        String infeedResourceDetailTableName = "Infeed_Resource_Details";
        String zoneResourceDetailPath = "/api/resourceDetails/getZoneDetails/" + experimentId;
        String zoneResourceDetailTableName = "Zone_Resource_Details";

        String updateStagePath = "/api/flexsim/updateSimulationStage";

        String parcelFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId,
                localhostURI,
                port,
                parcelDataPath,
                parcelDataTableName
                );

        String infeedResourceFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId,
                localhostURI,
                port,
                infeedResourceDetailPath,
                infeedResourceDetailTableName
        );

        String zoneResourceFunction = String.format(
                "getDataForExperiment(%d, \"%s\", %d, \"%s\", \"%s\")",
                experimentId,
                localhostURI,
                port,
                zoneResourceDetailPath,
                zoneResourceDetailTableName
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");

        String startTime = experimentDto.getStartTime().format(formatter);

        String setFlexsimStartTime = String.format(
                """     
                DateTime startTime = DateTime("%s","%%m/%%d/%%Y %%H:%%M:%%S");
                function_s(getmodelunit(START_TIME_NODE), "setDateTime", startTime);
                """,
               startTime
        );

        String stopTime = experimentDto.getEndTime().plusMinutes(15).format(formatter);

        String setFlexsimStopTime = String.format(
                """     
                DateTime stopTime = DateTime("%s","%%m/%%d/%%Y %%H:%%M:%%S");
                int s = stopTime.totalSeconds - startTime.totalSeconds;
                stoptime(s);
                """,
                stopTime
        );

        String simulationStartingFunction = String.format(
                "updateSimulationStatus(%d, \"%s\", %d, \"%s\", \"%s\", \"%s\")",
                experimentId,
                localhostURI,
                port,
                updateStagePath,
                ExperimentRunStage.STARTING_SIMULATION,
                "Starting Simulation"
        );

        Integer MAX_RECIRCULATION = experimentDto.getMaxRecirculationCount();

        String simulationStartedFunction = String.format(
                "updateSimulationStatus(%d, \"%s\", %d, \"%s\", \"%s\", \"%s\")",
                experimentId,
                localhostURI,
                port,
                updateStagePath,
                ExperimentRunStage.SIMULATION_STARTED,
                "Simulation Started"
        );


        String script = String.format(
                """
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
                parcelFunction,
                infeedResourceFunction,
                zoneResourceFunction,
                setFlexsimStartTime,
                setFlexsimStopTime,
                simulationStartingFunction,
                MAX_RECIRCULATION,
                simulationStartedFunction
        );

        Files.write(scriptPath, script.getBytes(StandardCharsets.UTF_8));

        return scriptPath.toString();
    }
}
