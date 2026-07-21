
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./SimulationSettings.css";

const steps = [
    "Experiment Configuration",
    "Chute Mapping & Resource Distribution",
    "Acceptable Parcel Units",
    "Simulation Settings",
];


const CHUTE_ID_MASTER = {
    South: {
        "252": [252],
    },
    East: {
        "101-114": [101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114],
        "121-135": [121, 122, 131, 132, 133, 134, 135],
        "301-337": [301, 302, 303, 304, 305, 306, 311, 312, 313, 314, 315, 316, 321, 322, 323, 324, 325, 326, 327, 332, 333, 334, 335, 336, 337]
    },
    North: {
        "152": [152],
    },
    West: {
        "201-214": [201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214],
        "221-236": [221, 222, 223, 224, 231, 232, 233, 234, 235, 236],
        "402-433": [402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433]
    }
};

const ZONE_NAME_MAP = {
    "ZONE-1": "South",
    "ZONE-2": "East",
    "ZONE-3": "North",
    "ZONE-4": "West",
};

const KPI_OPTIONS = {
    Overall: [
        "Scanned Parcels",
        "Parcel Throughput & Rejected Parcels",
        "Blocked Parcels",
        "Total Recirculation count",
    ],
    Chute: [
        "Blocked Chutes",
        "Parcel Throughput",
        "Reject Summary",
    ],
    Infeed: [
        "Parcels Unloaded",
        "Parcel Mix",
    ],
    Resource: [
        "Weight Handled",
        "Top 10 Resources - Parcel Handled",
        "Top 10 Resources - Weight Handled",
    ],

};

const getAllKpis = () => Object.values(KPI_OPTIONS).flat();
const HISTORY_KEY = "experimentHistory";
const ACTIVE_EXPERIMENT_KEY = "activeExperiment";

const SimulationSettings = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const previousState = location.state || {};

    const {
        sortingType,
        experimentTitle = "",
        selectedDate = "",
        useCaseId = "",
        startTime = "23:00:00",
        endTime = "",
        parcelPayloadStartTime = "",
        parcelPayloadEndTime = "",
        parcelCount = 0,
        infeedFile = null,
        zoneFile = null,
        infeedData = {},
        zoneData = {},
        zoneRawData = [],
        shiftTimings = {},
        selectedShift = "1",
        extraShiftCount = 0,
        directChuteData = [],
        simulationSettings: simulationSettingsFromState = {},
    } = previousState;

    const allKpis = useMemo(() => getAllKpis(), []);

    const [selectedKpiMode, setSelectedKpiMode] = useState(
        simulationSettingsFromState.selectedKpiMode || ""
    );
    const [selectedKpis, setSelectedKpis] = useState(
        simulationSettingsFromState.selectedKpis || []
    );
    const [maxRecirculationCount, setMaxRecirculationCount] = useState(
        simulationSettingsFromState.maxRecirculationCount || ""
    );
    const [parcelEnhanceDirection, setParcelEnhanceDirection] = useState(
        simulationSettingsFromState.parcelEnhanceDirection || "increase"
    );
    const [parcelEnhanceValue, setParcelEnhanceValue] = useState(
        simulationSettingsFromState.parcelEnhanceValue || ""
    );

    const [isKpiSectionOpen, setIsKpiSectionOpen] = useState(
        simulationSettingsFromState.selectedKpiMode === "Custom Selection"
    );

    const kpiDropdownRef = useRef(null);
    const progressEventSourceRef = useRef(null);
    const hasRedirectedRef = useRef(false);
    const currentExperimentIdRef = useRef(null);

    const [username, setUsername] = useState("");
    const [isSimulating, setIsSimulating] = useState(false);
    const [simulationStatusMessage, setSimulationStatusMessage] = useState("");

    useEffect(() => {
        const storedUsername = localStorage.getItem("username");
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);

    const closeSimulationProgressStream = () => {
        if (progressEventSourceRef._cleanup) {
            progressEventSourceRef._cleanup();
            progressEventSourceRef._cleanup = null;
        }
        if (progressEventSourceRef.current) {
            progressEventSourceRef.current.close();
            progressEventSourceRef.current = null;
        }
    };

    const upsertHistoryRecord = (record) => {
        try {
            const currentRows = JSON.parse(localStorage.getItem(HISTORY_KEY) || "[]");
            const existingIndex = currentRows.findIndex(
                (item) => Number(item.experimentId) === Number(record.experimentId)
            );

            if (existingIndex >= 0) {
                currentRows[existingIndex] = {
                    ...currentRows[existingIndex],
                    ...record,
                    updatedAt: new Date().toISOString(),
                };
            } else {
                currentRows.push({
                    ...record,
                    createdAt: record.createdAt || new Date().toISOString(),
                    updatedAt: new Date().toISOString(),
                });
            }

            localStorage.setItem(HISTORY_KEY, JSON.stringify(currentRows));
        } catch (error) {
            console.error("Unable to store experiment history:", error);
        }
    };

    const setActiveExperimentRecord = (record) => {
        localStorage.setItem(ACTIVE_EXPERIMENT_KEY, JSON.stringify(record));
    };

    const clearActiveExperimentRecord = (experimentId) => {
        try {
            const rawValue = localStorage.getItem(ACTIVE_EXPERIMENT_KEY);
            if (!rawValue) return;

            const activeExperiment = JSON.parse(rawValue);
            if (!experimentId || Number(activeExperiment.experimentId) === Number(experimentId)) {
                localStorage.removeItem(ACTIVE_EXPERIMENT_KEY);
            }
        } catch (error) {
            console.error("Unable to clear active experiment:", error);
        }
    };

    useEffect(() => {
        return () => {
            closeSimulationProgressStream();
        };
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (
                kpiDropdownRef.current &&
                !kpiDropdownRef.current.contains(event.target)
            ) {
                setIsKpiSectionOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const toggleKpi = (kpi) => {
        setSelectedKpis((prev) =>
            prev.includes(kpi)
                ? prev.filter((item) => item !== kpi)
                : [...prev, kpi]
        );
    };

    const toggleSelectAll = () => {
        if (selectedKpis.length === allKpis.length) {
            setSelectedKpis([]);
        } else {
            setSelectedKpis(allKpis);
        }
    };
    const handleParcelModeChange = (mode) => {
        setParcelEnhanceDirection(mode);
        setParcelEnhanceValue("");
    };

    const adjustedParcelCount = useMemo(() => {
        const base = Number(parcelCount || 0);
        const percent = Number(parcelEnhanceValue || 0);
        if (!percent) return base;
        const change = (base * percent) / 100;
        return parcelEnhanceDirection === "increase"
            ? Math.round(base + change)
            : Math.max(0, Math.round(base - change));
    }, [parcelCount, parcelEnhanceDirection, parcelEnhanceValue]);


    const parseChuteList = (chuteValue) => {

        if (!chuteValue) return [];

        const cleaned = String(chuteValue).replace(/[\[\]\s]/g, "");

        if (!cleaned) return [];

        return cleaned

            .split(",")

            .map((item) => Number(item))

            .filter((num) => !Number.isNaN(num));

    };

    const getChuteRangeLabel = (chuteValue) => {
        if (!chuteValue) return "";
        const cleaned = String(chuteValue).replace(/[\[\]\s]/g, "");
        if (!cleaned) return "";
        const chuteList = cleaned
            .split(",")
            .map(Number)
            .filter((n) => !Number.isNaN(n))
            .sort((a, b) => a - b);
        if (!chuteList.length) return "";
        const first = chuteList[0];
        const last = chuteList[chuteList.length - 1];
        return first === last ? `${first}` : `${first}-${last}`;
    };

    const buildZoneFileMap = (zoneRawData) => {

        const fileMap = {};

        (zoneRawData || []).forEach((row) => {

            const shiftId = String(row.shiftId);

            const zone =

                row.zoneId === "ZONE-1"

                    ? "South"

                    : row.zoneId === "ZONE-2"

                        ? "East"

                        : row.zoneId === "ZONE-3"

                            ? "North"

                            : row.zoneId === "ZONE-4"

                                ? "West"

                                : row.zoneId;

            const chuteIds = parseChuteList(row.chuteIdRaw);

            const chuteRange = getChuteRangeLabel(row.chuteIdRaw);

            if (!fileMap[shiftId]) fileMap[shiftId] = {};

            if (!fileMap[shiftId][zone]) fileMap[shiftId][zone] = {};

            if (!fileMap[shiftId][zone][chuteRange]) {

                fileMap[shiftId][zone][chuteRange] = chuteIds;

            }

        });

        return fileMap;

    };

    // const buildZonePayload = (experimentId, zoneData) => {

    //     const payload = [];

    //     Object.keys(zoneData || {}).forEach((shiftId) => {

    //         const shiftZones = zoneData[shiftId] || {};

    //         Object.keys(shiftZones).forEach((zone) => {

    //             (shiftZones[zone] || []).forEach((row) => {

    //                 const chuteRange = String(row.chuteRange || "").trim();

    //                 const resourceCount = Number(row.resourceCount || 0);

    //                 const error = String(row.error || "").trim();

    //                 if (!chuteRange || error) return;

    //                 payload.push({

    //                     experimentId,

    //                     shiftId,

    //                     zone,

    //                     chuteRange,

    //                     chuteIds: row.chuteIds || [],

    //                     resourceCount,

    //                 });

    //             });

    //         });

    //     });

    //     return payload;

    // };


    const buildZoneResourceDetailPayload = (experimentId, zoneRawData) => {
        const counters = {};

        return (zoneRawData || []).map((row) => {
            const shiftId = String(row.shiftId || "");
            const zoneId = String(row.zoneId || "");
            const chutes = String(row.chuteIdRaw || "").replace(/\s+/g, "");
            const key = `${shiftId}__${zoneId}__${chutes}`;


            if (!counters[key]) {
                counters[key] = 1;
            }

            const payloadRow = {
                experimentId,
                shiftId,
                zoneId,
                resourceId: counters[key],
                chutes: chutes,
            };

            counters[key] += 1;

            return payloadRow;
        });
    };

    const buildAcceptableParcelPayload = (experimentId, directChuteData) => {
        const findValue = (label, field) => {
            const row = (directChuteData || []).find(
                (item) => item.label?.toLowerCase() === label.toLowerCase()
            );
            return Number(row?.[field] ?? 0);
        };
        return {
            experimentId,
            minHeight: findValue("Height (m)", "min"),
            maxHeight: findValue("Height (m)", "max"),
            minLength: findValue("Length (m)", "min"),
            maxLength: findValue("Length (m)", "max"),
            minWidth: findValue("Width (m)", "min"),
            maxWidth: findValue("Width (m)", "max"),
            minWeight: findValue("Weight (kg)", "min"),
            maxWeight: findValue("Weight (kg)", "max"),
        };
    };

    const buildInfeedPayload = (experimentId, infeedData) => {

        const payload = [];

        Object.keys(infeedData || {}).forEach((zone) => {

            (infeedData[zone] || []).forEach((row) => {

                Object.keys(row.shiftData || {}).forEach((shiftId) => {

                    const cell = row.shiftData[shiftId];

                    payload.push({

                        experimentId,

                        shiftId,

                        zoneId: ZONE_NAME_MAP[zone] || zone,

                        infeed: row.infeed,

                        tc: row.tc,

                        noOfResources: Number(cell?.resource || 0),

                        active: Boolean(cell?.active),

                    });

                });

            });

        });

        return payload;

    };


    const buildNavigationState = () => ({
        ...previousState,
        sortingType,
        experimentTitle,
        selectedDate,
        useCaseId,
        startTime,
        endTime,
        parcelCount,
        infeedFile,
        zoneFile,
        infeedData,
        zoneData,
        zoneRawData,
        shiftTimings,
        selectedShift,
        extraShiftCount,
        directChuteData,
        simulationSettings: {
            selectedKpiMode,
            selectedKpis,
            maxRecirculationCount,
            parcelEnhanceDirection,
            parcelEnhanceValue,
            adjustedParcelCount,
        },
    });

    const handleBack = () => {
        navigate("/acceptable-parcel-units", {
            state: buildNavigationState(),
        });
    };

    const handleSimulationProgressUpdate = (eventData, experimentId) => {
        const stage = String(eventData?.stage || "").toUpperCase();
        const status = String(eventData?.status || "").toUpperCase();

        upsertHistoryRecord({
            experimentId,
            experimentName: experimentTitle,
            sortingType,
            startTime: parcelPayloadStartTime || `${selectedDate}T${startTime}`,
            endTime: parcelPayloadEndTime || `${selectedDate}T${endTime}`,
            status: status || "RUNNING",
            stage,
            owner: username,
        });

        if (eventData?.message) {
            setSimulationStatusMessage(eventData.message);
        }

        if (stage === "SIMULATION_STARTED" && !hasRedirectedRef.current) {
            setActiveExperimentRecord({
                experimentId,
                experimentName: experimentTitle,
                sortingType,
                owner: username,
                status: status || "RUNNING",
                stage,
            });
            hasRedirectedRef.current = true;
            closeSimulationProgressStream();
            setIsSimulating(false);

            navigate(`/live-experiment/${experimentId}`, {
                state: {
                    ...buildNavigationState(),
                    experimentId,
                    experimentName: experimentTitle,
                    mode: "live",
                },
            });
            return;
        }

        if (status === "FAILED") {
            clearActiveExperimentRecord(experimentId);
            closeSimulationProgressStream();
            setIsSimulating(false);
            setSimulationStatusMessage(eventData?.message || eventData?.errorMessage || "Simulation failed");
            return;
        }

        if (status === "COMPLETED") {
            clearActiveExperimentRecord(experimentId);
            closeSimulationProgressStream();
            setIsSimulating(false);
        }
    };

    const subscribeToSimulationProgress = (experimentId) => {
        closeSimulationProgressStream();

        const MAX_RETRIES = 5;
        const BASE_DELAY_MS = 2000;
        let retryCount = 0;
        let retryTimeoutId = null;

        const connect = () => {
            if (hasRedirectedRef.current) return;

            const eventSource = new EventSource(
                `http://localhost:8080/api/flexsim/progress/stream/${experimentId}`
            );

            progressEventSourceRef.current = eventSource;

            const handleIncomingEvent = (event) => {
                try {
                    const parsedData = JSON.parse(event.data);
                    retryCount = 0;
                    handleSimulationProgressUpdate(parsedData, experimentId);
                } catch (error) {
                    console.error("Unable to parse SSE event data:", error);
                }
            };

            eventSource.onmessage = handleIncomingEvent;
            eventSource.addEventListener("simulation-progress", handleIncomingEvent);

            eventSource.onerror = () => {
                if (hasRedirectedRef.current) {
                    closeSimulationProgressStream();
                    return;
                }

                eventSource.close();
                progressEventSourceRef.current = null;

                if (retryCount < MAX_RETRIES) {
                    retryCount += 1;
                    const delay = BASE_DELAY_MS * Math.pow(2, retryCount - 1);
                    setSimulationStatusMessage(
                        `Connection lost. Reconnecting (${retryCount}/${MAX_RETRIES})...`
                    );
                    retryTimeoutId = setTimeout(() => {
                        if (!hasRedirectedRef.current) connect();
                    }, delay);
                } else {
                    setIsSimulating(false);
                    setSimulationStatusMessage("Unable to receive live simulation updates. Please check the experiment status manually.");
                }
            };
        };

        progressEventSourceRef._cleanup = () => {
            if (retryTimeoutId) clearTimeout(retryTimeoutId);
        };

        connect();
    };


    const handleSimulate = async () => {
        if (isSimulating) return;

        hasRedirectedRef.current = false;
        setIsSimulating(true);
        setSimulationStatusMessage("Creating Experiment");

        const formattedStartTime =
            startTime?.length === 5 ? `${startTime}:00` : startTime;
        const formattedEndTime =
            endTime?.length === 5 ? `${endTime}:00` : endTime;

        const simulationStartTime = parcelPayloadStartTime || `${selectedDate}T${formattedStartTime}`;
        const simulationEndTime = parcelPayloadEndTime || `${selectedDate}T${formattedEndTime}`;


        const payload = {
            terminal: "Koge",
            experimentName: experimentTitle,
            sortingType,
            useCaseId,
            selectedDate,
            startTime: simulationStartTime,
            endTime: simulationEndTime,
            parcelCount: Number(parcelCount || 0),
            newParcelCount: Number(adjustedParcelCount || 0),
            parcelChangeValue: Number(parcelEnhanceValue || 0),
            parcelChangeMode: parcelEnhanceDirection === "increase" ? "INCREMENT" : "DECREMENT",
            maxRecirculationCount: Number(maxRecirculationCount || 0),
            username: username
        };



        console.log("payload", payload);
        try {
            setSimulationStatusMessage("Creating Experiment");
            const response = await fetch("http://localhost:8080/api/experiments/createExperiment", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                throw new Error("Failed to save experiment config");
            }

            const data = await response.json();

            console.log("Saved successfully:", data);

            const experimentData = data;

            // store experimentId for next tables
            const experimentId = data.experimentId;
            console.log("Experiment saved with ID:", experimentId);
            currentExperimentIdRef.current = experimentId;

            upsertHistoryRecord({
                experimentId,
                experimentName: experimentTitle,
                sortingType,
                startTime: simulationStartTime,
                endTime: simulationEndTime,
                status: "RUNNING",
                owner: username,
            });


            const infeedPayLoad = buildInfeedPayload(experimentId, infeedData);
            console.log("INFEED PAYLOAD:", infeedPayLoad);

            setSimulationStatusMessage("Setting Infeed Resource Details");
            const infeedRespose = await fetch("http://localhost:8080/api/resourceDetails/setInfeedDetails", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(infeedPayLoad),
            });

            if (!infeedRespose.ok) {
                const errorText = await infeedRespose.text();
                console.error("Infeeed save filed:", errorText);
                throw new Error("failed to save infeed resource");
            }

            const infeedDataResponse = await infeedRespose.text();
            console.log("Infeed Resource Data saved succesfully:", infeedDataResponse);

            const zonePayload = buildZoneResourceDetailPayload(experimentId, zoneRawData);
            console.log("Zone PayLoad: ", zonePayload);

            setSimulationStatusMessage("Setting Zone Resource Details");
            const zoneResponse = await fetch("http://localhost:8080/api/resourceDetails/setZoneDetails", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(zonePayload),
            });

            if (!zoneResponse.ok) {
                const errorText = await zoneResponse.text();
                console.error("Zone save filed:", errorText);
                throw new Error("failed to save zone resource");
            }

            const zonedDataResponse = await zoneResponse.text();
            console.log("Zone Resource Data saved succesfully:", zonedDataResponse);


            const acceptableParcelPayload = buildAcceptableParcelPayload(
                experimentId,
                directChuteData
            );
            console.log("Acceptable Parcel Payload:", acceptableParcelPayload);
            setSimulationStatusMessage("Setting Acceptable Parcel Unit");
            const acceptableParcelResponse = await fetch(
                "http://localhost:8080/api/acceptable-parcel-unit/save",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(acceptableParcelPayload),
                }
            );
            if (!acceptableParcelResponse.ok) {
                const errorText = await acceptableParcelResponse.text();
                console.error("Acceptable parcel save failed:", errorText);
                throw new Error("Failed to save acceptable parcel unit");
            }
            const acceptableParcelResponseText = await acceptableParcelResponse.text();
            console.log(
                "Acceptable Parcel Unit saved successfully:",
                acceptableParcelResponseText
            );

            setSimulationStatusMessage("Starting Simulation");
            const startSimulation = await fetch(
                "http://localhost:8080/api/simulation/startSimulation",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(experimentData),
                }
            );

            if (!startSimulation.ok) {
                const errorText = await startSimulation.text();
                console.error("Start simulation failed:", errorText);
                throw new Error("Failed to start simulation");
            }

            setSimulationStatusMessage("Waiting for simulation progress updates");
            subscribeToSimulationProgress(experimentId);

        } catch (error) {
            console.error("Error:", error);
            if (currentExperimentIdRef.current) {
                upsertHistoryRecord({
                    experimentId: currentExperimentIdRef.current,
                    experimentName: experimentTitle,
                    sortingType,
                    startTime: simulationStartTime,
                    endTime: simulationEndTime,
                    status: "FAILED",
                    owner: username,
                });
                clearActiveExperimentRecord(currentExperimentIdRef.current);
            }
            closeSimulationProgressStream();
            setIsSimulating(false);
            setSimulationStatusMessage("Simulation setup failed");
        } finally {
            // setIsSimulating(false);
            // setSimulationStatusMessage("");
        }
    };


    return (
        <div className="sim-page">
            <AppHeader />

            <div className="sim-shell">
                <div className="sim-title-block">
                    <h1 className="sim-page-title">
                        {sortingType
                            ? `${sortingType.charAt(0).toUpperCase() + sortingType.slice(1)} Sorting Optimization`
                            : "Sorting Optimization"}
                    </h1>
                </div>

                <div className="sim-stepper">
                    {steps.map((step, i) => (
                        <div className="sim-step-item" key={i}>
                            <div className={i < 3 ? "sim-circle completed" : "sim-circle active"}>
                                {i + 1}
                            </div>
                            <span className="sim-step-label">{step}</span>
                            {i !== steps.length - 1 && (
                                <div className={i < 3 ? "sim-line active" : "sim-line"} />
                            )}
                        </div>
                    ))}
                </div>

                <div className="sim-summary-card">
                    <div className="sim-summary-grid">
                        <div className="sim-summary-item">
                            <span className="sim-summary-label">Experiment Title</span>
                            <span className="sim-summary-value">{experimentTitle || "-"}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">Sorting Type</span>
                            <span className="sim-summary-value">{sortingType || "-"}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">UseCase Id</span>
                            <span className="sim-summary-value">{useCaseId}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">Parcel Count</span>
                            <span className="sim-summary-value">{parcelCount}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">Date</span>
                            <span className="sim-summary-value">{selectedDate || "-"}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">Start Time</span>
                            <span className="sim-summary-value">{startTime || "-"}</span>
                        </div>

                        <div className="sim-summary-item">
                            <span className="sim-summary-label">End Time</span>
                            <span className="sim-summary-value">{endTime || "-"}</span>
                        </div>
                    </div>
                </div>

                <div className="sim-card">
                    <div className="sim-card-header">
                        <div>
                            <h3>KPI Metric Selection</h3>
                            <p>Select KPI, max recirculation count, and parcel enhance settings.</p>
                        </div>
                    </div>

                    <div className="sim-card-body">
                        <div className="sim-row sim-kpi-row">
                            <div className="sim-label">Select Simulation KPI</div>

                            <div className="sim-field sim-kpi-field" ref={kpiDropdownRef}>
                                <select
                                    className="sim-select"
                                    value={selectedKpiMode}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        setSelectedKpiMode(value);

                                        if (value === "All Selected") {
                                            setSelectedKpis(allKpis);
                                            setIsKpiSectionOpen(false);
                                        } else if (value === "Custom Selection") {
                                            setIsKpiSectionOpen(true);
                                        } else {
                                            setSelectedKpis([]);
                                            setIsKpiSectionOpen(false);
                                        }
                                    }}
                                >
                                    <option value="">Select KPI</option>
                                    <option value="All Selected">All Selected</option>
                                    <option value="Custom Selection">Custom Selection</option>
                                </select>

                                {isKpiSectionOpen && (
                                    <div className="sim-kpi-overlay">
                                        <div className="sim-kpi-select-all">
                                            <label className="sim-checkbox-row">
                                                <input
                                                    type="checkbox"
                                                    checked={selectedKpis.length === allKpis.length}
                                                    onChange={toggleSelectAll}
                                                />
                                                <span>Select All</span>
                                            </label>
                                        </div>

                                        <div className="sim-kpi-grid">
                                            {Object.entries(KPI_OPTIONS).map(([group, items]) => (
                                                <div className="sim-kpi-group" key={group}>
                                                    <h4>{group}</h4>
                                                    {items.map((item) => (
                                                        <label className="sim-checkbox-row" key={item}>
                                                            <input
                                                                type="checkbox"
                                                                checked={selectedKpis.includes(item)}
                                                                onChange={() => toggleKpi(item)}
                                                            />
                                                            <span>{item}</span>
                                                        </label>
                                                    ))}
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div className="sim-row">
                            <div className="sim-label">Max Recirculation Count</div>
                            <div className="sim-field">
                                <input
                                    type="number"
                                    min="0"
                                    className="sim-input"
                                    value={maxRecirculationCount}
                                    onChange={(e) => setMaxRecirculationCount(e.target.value)}
                                    placeholder="Enter max recirculation count"
                                />
                            </div>
                        </div>

                        <div className="sim-row">
                            <div className="sim-label">Parcels Enhance</div>
                            <div className="sim-field">
                                <div className="sim-enhance-block">
                                    <div className="sim-toggle-row">
                                        <button
                                            type="button"
                                            className={`sim-toggle-btn ${parcelEnhanceDirection === "increase" ? "active" : ""
                                                }`}
                                            onClick={() => handleParcelModeChange("increase")}
                                        >
                                            Increase
                                        </button>
                                        <button
                                            type="button"
                                            className={`sim-toggle-btn ${parcelEnhanceDirection === "decrease" ? "active" : ""
                                                }`}
                                            onClick={() => handleParcelModeChange("decrease")}
                                        >
                                            Decrease
                                        </button>
                                    </div>

                                    <div className="sim-enhance-summary">
                                        <div className="sim-enhance-line">
                                            <span>Parcel Count:</span>
                                            <strong>{adjustedParcelCount} Parcels</strong>
                                        </div>

                                        <div className="sim-enhance-line value-line">
                                            <span>
                                                {parcelEnhanceDirection === "increase"
                                                    ? "Increase by (%):"
                                                    : "Decrease by (%):"}
                                            </span>

                                            <input
                                                type="number"
                                                min="0"
                                                className="sim-mini-input"
                                                value={parcelEnhanceValue}
                                                onChange={(e) => setParcelEnhanceValue(e.target.value)}
                                            />
                                        </div>

                                        <div className="sim-enhance-line">
                                            <span>New total:</span>
                                            <strong>{adjustedParcelCount} Parcels</strong>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="sim-button-row">
                            <button className="sim-back-btn" onClick={handleBack}>
                                Back
                            </button>

                            <button
                                className="sim-next-btn"
                                onClick={handleSimulate}
                                disabled={isSimulating}
                            >
                                {isSimulating ? "Simulating..." : "Simulate"}
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {isSimulating && (
                <div className="sim-loader-overlay" role="status" aria-live="polite">
                    <div className="sim-loader-card">
                        <div className="sim-loader-spinner" />
                        <p className="sim-loader-message">{simulationStatusMessage || "Processing"}</p>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SimulationSettings;

