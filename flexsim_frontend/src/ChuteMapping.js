import React, { useEffect, useMemo, useState } from "react";

import "./ChuteMapping.css";

import { useLocation, useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";

import * as XLSX from "xlsx";

const steps = [

  "Experiment Configuration",

  "Chute Mapping & Resource Distribution",

  "Acceptable Parcel Units",



  "Simulation Settings",

];

const CHUTE_MASTER = [
  { zone: "North", start: 152, end: 152, type: "Reject Chute" },
  { zone: "East", start: 121, end: 135, type: "Boom Conveyor Chute" },
  { zone: "East", start: 301, end: 337, type: "Direct Chute" },
  { zone: "East", start: 101, end: 114, type: "Spiral Chute" },
  { zone: "South", start: 252, end: 252, type: "Reject Chute" },
  { zone: "West", start: 201, end: 214, type: "Spiral Chute" },
  { zone: "West", start: 402, end: 433, type: "Direct Chute" },
  { zone: "West", start: 221, end: 236, type: "Boom Conveyor Chute" },
];

const DISTRIBUTION_MASTER_SHIFTS = [
  { id: "1", start: "23:00:00", end: "23:30:00" },
  { id: "2", start: "23:30:00", end: "00:30:00" },
  { id: "3", start: "00:30:00", end: "01:30:00" },
  { id: "4", start: "01:30:00", end: "02:00:00" },
  { id: "5", start: "02:00:00", end: "02:30:00" },
  { id: "6", start: "02:30:00", end: "03:00:00" },
  { id: "7", start: "03:00:00", end: "04:00:00" },
  { id: "8", start: "04:00:00", end: "05:00:00" },
  { id: "9", start: "05:00:00", end: "06:00:00" },
  { id: "10", start: "06:00:00", end: "06:30:00" },
  { id: "11", start: "06:30:00", end: "07:00:00" },
];


const MASTER_FINAL_END = "07:00:00";

const INFEED_MAPPING = {

  South: [

    { infeed: "IF-01", tc: "KIP7-KIP8" },

    { infeed: "IF-01", tc: "B14-B15" },

    { infeed: "IF-02", tc: "KIP5-KIP6" },

    { infeed: "IF-02", tc: "B9-B10-B11" },

    { infeed: "IF-03", tc: "KIP1-KIP2" },

  ],

  North: [

    { infeed: "IF-04", tc: "B113-B116" },

    { infeed: "IF-05", tc: "KIP3-KIP4" },

    { infeed: "IF-06", tc: "KIP9-KIP10" },

  ],

};
const ZONE_NAME_MAP = {
  "ZONE-1": "South",
  "ZONE-2": "East",
  "ZONE-3": "North",
  "ZONE-4": "West",
};


const parseManualChuteRange = (value) => {
  const cleaned = String(value || "").trim();
  if (!cleaned) {
    return { startId: null, endId: null, isValidFormat: false };
  }
  if (cleaned.includes("-")) {
    const [start, end] = cleaned.split("-").map((item) => Number(item.trim()));
    if (Number.isNaN(start) || Number.isNaN(end)) {
      return { startId: null, endId: null, isValidFormat: false };
    }
    return {
      startId: start,
      endId: end,
      isValidFormat: true,
    };
  }
  const single = Number(cleaned);
  if (Number.isNaN(single)) {
    return { startId: null, endId: null, isValidFormat: false };
  }
  return {
    startId: single,
    endId: single,
    isValidFormat: true,
  };
};

const validateZoneChuteRange = (zone, chuteRange) => {
  const { startId, endId, isValidFormat } = parseManualChuteRange(chuteRange);
  if (!chuteRange || !String(chuteRange).trim()) {
    return "";
  }
  if (!isValidFormat) {
    return "Enter valid chute range like 101-114 or 252";
  }
  if (startId > endId) {
    return "Starting chute id cannot be greater than ending chute id";
  }
  const matchedRange = CHUTE_MASTER.find(
    (item) =>
      item.zone === zone &&
      startId >= item.start &&
      endId <= item.end
  );
  if (!matchedRange) {
    return "Invalid chute range for selected zone";
  }
  return "";
};

const normalizeTime = (time) => {
  if (!time) return "";
  return time.length === 5 ? `${time}:00` : time;
};

const timeToSeconds = (time) => {
  const normalized = normalizeTime(time);
  const [hh, mm, ss] = normalized.split(":").map(Number);
  return hh * 3600 + mm * 60 + ss;
}
// Convert overnight times into one increasing timeline from 23:00 onwards
const toTimelineSeconds = (time) => {
  const seconds = timeToSeconds(time);
  const threshold = timeToSeconds("23:00:00");
  return seconds < threshold ? seconds + 24 * 3600 : seconds;
};

const getVisibleMasterShifts = (selectedEndTime) => {
  const endTimeline = toTimelineSeconds(selectedEndTime);
  return DISTRIBUTION_MASTER_SHIFTS.filter(
    (shift) => toTimelineSeconds(shift.start) < endTimeline
  );
};

const isAddShiftAllowed = (selectedEndTime) => {
  const normalizedEnd = normalizeTime(selectedEndTime);
  if (!normalizedEnd || normalizedEnd === MASTER_FINAL_END) return false;
  return DISTRIBUTION_MASTER_SHIFTS.some(
    (shift) => shift.end === normalizedEnd && shift.end !== MASTER_FINAL_END
  );
};

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
  const chuteList = parseChuteList(chuteValue);
  if (!chuteList.length) return "";
  const sorted = [...chuteList].sort((a, b) => a - b);
  const first = sorted[0];
  const last = sorted[sorted.length - 1];
  return first === last ? `${first}` : `${first}-${last}`;
};




const parseZoneExcelFile = (file) => {

  return new Promise((resolve, reject) => {

    const reader = new FileReader();

    reader.onload = (e) => {

      try {

        const data = new Uint8Array(e.target.result);

        const workbook = XLSX.read(data, { type: "array" });

        const sheetName = workbook.SheetNames[0];

        const worksheet = workbook.Sheets[sheetName];

        const jsonData = XLSX.utils.sheet_to_json(worksheet, { defval: "" });

        const mapped = jsonData.map((row, index) => ({

          id: index + 1,

          shiftId: String(row["Shift_ID"] || row["Shift ID"] || "").trim(),

          zoneId: String(row["Zone_ID"] || row["Zone ID"] || "").trim(),

          resourceIndex: String(row["Resource"] || "").trim(),

          chuteIdRaw: String(row["Chute_ID"] || row["Chute ID"] || "").trim(),

        }));

        resolve(mapped);

      } catch (error) {

        reject(error);

      }

    };

    reader.onerror = (error) => reject(error);

    reader.readAsArrayBuffer(file);

  });

};



const ChuteMapping = () => {

  const location = useLocation();

  const navigate = useNavigate();

  const previousState = location.state || {}

  const {

    sortingType,

    experimentTitle,

    selectedDate,

    useCaseId,

    startTime,

    endTime,

    parcelCount = 0,

    infeedFile = null,
    zoneFile = null,

    // restore edited data when coming back
    infeedData: infeedDataFromState,
    zoneData: zoneDataFromState,
    shiftTimings: shiftTimingsFromState,
    selectedShift: selectedShiftFromState,
    extraShiftCount: extraShiftCountFromState = 0,

    // forward later-page data too
    directChuteData = [],
    simulationSettings = {},

  } = previousState;

  const [infeedRawData, setInfeedRawData] = useState([]);

  const [infeedData, setInfeedData] = useState(

    infeedDataFromState || { South: [], North: [] }

  );

  const [loading, setLoading] = useState(true);

  const [selectedShift, setSelectedShift] = useState(

    selectedShiftFromState || "1"

  );

  const [openSection, setOpenSection] = useState("infeed");

  const [selectedZoneTab, setSelectedZoneTab] = useState("South");

  const [zoneRawData, setZoneRawData] = useState([]);

  const [extraShiftCount, setExtraShiftCount] = useState(extraShiftCountFromState);

  const [selectedZoneResourceTab, setSelectedZoneResourceTab] = useState("South");

  const [zoneData, setZoneData] = useState(zoneDataFromState || {});

  const ZONE_ORDER = ["South", "East", "North", "West"];
  const headingText = useMemo(() => {

    if (sortingType === "distribution") return "Distribution Sorting Optimization";

    if (sortingType === "collection") return "Collection Sorting Optimization";

    return "Sorting Optimization";

  }, [sortingType]);

  const normalizedEndTime = useMemo(() => normalizeTime(endTime), [endTime]);

  const visibleMasterShifts = useMemo(() => {
    if (!normalizedEndTime) return [];
    const endTimeline = toTimelineSeconds(normalizedEndTime);
    const baseVisible = DISTRIBUTION_MASTER_SHIFTS.filter(
      (shift) => toTimelineSeconds(shift.start) < endTimeline
    );
    const baseCount = baseVisible.length;
    return DISTRIBUTION_MASTER_SHIFTS.slice(0, baseCount + extraShiftCount);
  }, [normalizedEndTime, extraShiftCount]);

  const canAddShift = useMemo(() => {
    if (!normalizedEndTime) return false;
    const endTimeline = toTimelineSeconds(normalizedEndTime);
    const baseVisible = DISTRIBUTION_MASTER_SHIFTS.filter(
      (shift) => toTimelineSeconds(shift.start) < endTimeline
    );
    return baseVisible.length + extraShiftCount < DISTRIBUTION_MASTER_SHIFTS.length;
  }, [normalizedEndTime, extraShiftCount]);

  const visibleShiftOptions = useMemo(() => {
    return visibleMasterShifts.map((shift) => ({
      id: String(shift.id),
      label: `Shift ${shift.id}`,
      timing: `${shift.start} - ${shift.end}`,
    }));
  }, [visibleMasterShifts]);

  const createBlankZoneRow = () => ({
    chuteRange: "",
    resourceCount: 0,
    error: "",
  });
  const createBlankZoneShiftData = () => ({
    South: [createBlankZoneRow()],
    East: [createBlankZoneRow()],
    North: [createBlankZoneRow()],
    West: [createBlankZoneRow()],
  });


  const buildNavigationState = () => ({
    ...previousState,
    sortingType,
    experimentTitle,
    selectedDate,
    startTime,
    endTime,
    parcelCount,
    infeedFile,
    zoneFile,
    useCaseId,
    zoneRawData,

    // latest updated chute mapping data
    infeedData,
    zoneData,
    shiftTimings,
    selectedShift,
    extraShiftCount,

    // keep later-page data too
    directChuteData,
    simulationSettings,
  });


  useEffect(() => {
    setExtraShiftCount(0);
  }, [normalizedEndTime]);



  useEffect(() => {

    if (infeedDataFromState && Object.keys(infeedDataFromState).length) {
      setLoading(false);
      return;
    }
    if (!infeedFile) {

      setLoading(false);

      return;

    }

    const parseFile = async () => {

      try {

        const parsed = await parseExcelFile(infeedFile);

        setInfeedRawData(parsed);

      } catch (error) {

        console.error("Error parsing infeed file:", error);

      } finally {

        setLoading(false);

      }

    };

    parseFile();

  }, [infeedFile, infeedDataFromState]);


  useEffect(() => {
    if (infeedDataFromState && Object.keys(infeedDataFromState).length) return;
    if (!infeedRawData.length) return;

    const mappedData = {
      South: INFEED_MAPPING.South.map((item) => ({
        ...item,
        shiftData: buildShiftData(item.tc),
      })),
      North: INFEED_MAPPING.North.map((item) => ({
        ...item,
        shiftData: buildShiftData(item.tc),
      })),
    };

    setInfeedData(mappedData);
  }, [infeedRawData, infeedDataFromState]);


  // Keep selected shift valid when visible shift range changes
  useEffect(() => {
    if (!visibleShiftOptions.length) return;
    const exists = visibleShiftOptions.some((shift) => shift.id === selectedShift);
    if (!exists) {
      setSelectedShift(visibleShiftOptions[0].id);
    }
  }, [visibleShiftOptions, selectedShift]);

  const parseExcelFile = (file) => {

    return new Promise((resolve, reject) => {

      const reader = new FileReader();

      reader.onload = (e) => {

        try {

          const data = new Uint8Array(e.target.result);

          const workbook = XLSX.read(data, { type: "array" });

          const sheetName = workbook.SheetNames[0];

          const worksheet = workbook.Sheets[sheetName];

          const jsonData = XLSX.utils.sheet_to_json(worksheet, { defval: "" });

          const mapped = jsonData.map((row, index) => ({

            id: index + 1,

            shiftId: String(row["Shift_ID"] || row["Shift ID"] || "").trim(),

            tc: String(row["TC"] || "").trim().replace(/[\[\]]/g, ""),

            resourceCount: Number(row["No of Resources"] || row["No_of_Resources"] || 0),

          }));

          resolve(mapped);

        } catch (error) {

          reject(error);

        }

      };

      reader.onerror = (error) => reject(error);

      reader.readAsArrayBuffer(file);

    });

  };

  const buildShiftData = (tc) => {
    const shiftMap = {};
    DISTRIBUTION_MASTER_SHIFTS.forEach((shift) => {
      const match = infeedRawData.find(
        (row) => row.shiftId === shift.id && row.tc === tc
      );
      const resourceValue = match ? Number(match.resourceCount || 0) : 0;
      shiftMap[shift.id] = {
        resource: resourceValue,
        active: resourceValue > 0, // OFF by default if 0
      };
    });
    return shiftMap;
  };

  const handleShiftChange = (e) => {

    setSelectedShift(e.target.value);

  };

  // const handleShiftTimingChange = (value) => {

  //   setShiftTimings((prev) => ({

  //     ...prev,

  //     [selectedShift]: value,

  //   }));

  // };

  const shiftTimings = useMemo(() => {
    const timings = {};
    visibleShiftOptions.forEach((shift) => {
      timings[shift.id] = shift.timing;
    });
    return timings;
  }, [visibleShiftOptions]);

  const handleAddShift = () => {

    if (!canAddShift) return;

    const nextVisibleShift = DISTRIBUTION_MASTER_SHIFTS.find(

      (shift) => !visibleShiftOptions.some(

        (visibleShift) => String(visibleShift.id) === String(shift.id)

      )

    );

    if (!nextVisibleShift) return;

    const nextId = String(nextVisibleShift.id);

    setInfeedData((prev) => {

      const updated = { ...prev };

      Object.keys(updated).forEach((zone) => {

        updated[zone] = updated[zone].map((row) => ({

          ...row,

          shiftData: {

            ...row.shiftData,

            [nextId]: row.shiftData[nextId] || {

              resource: 0,

              active: false,

            },

          },

        }));

      });

      return updated;

    });

    setZoneData((prev) => {

      const updated = { ...prev };
      const fromFile = buildZoneDataFromRawForShift(nextId);


      updated[nextId] = fromFile || createBlankZoneShiftData();
      return updated;

    });


    setExtraShiftCount((prev) => prev + 1);

    setSelectedShift(nextId);

  };


  const handleResourceChange = (zoneName, rowIndex, value) => {
    const numericValue = value === "" ? "" : Number(value);
    setInfeedData((prev) => {
      const updatedZoneRows = [...prev[zoneName]];
      updatedZoneRows[rowIndex] = {
        ...updatedZoneRows[rowIndex],
        shiftData: {
          ...updatedZoneRows[rowIndex].shiftData,
          [selectedShift]: {
            resource: numericValue,
            active: numericValue > 0,
          },
        },
      };
      return {
        ...prev,
        [zoneName]: updatedZoneRows,
      };
    });
  };


  const handleStatusToggle = (zoneName, rowIndex) => {
    setInfeedData((prev) => {
      const updatedZoneRows = [...prev[zoneName]];
      const currentCell = updatedZoneRows[rowIndex].shiftData[selectedShift];
      updatedZoneRows[rowIndex] = {
        ...updatedZoneRows[rowIndex],
        shiftData: {
          ...updatedZoneRows[rowIndex].shiftData,
          [selectedShift]: {
            ...currentCell,
            active: !currentCell.active,
          },
        },
      };
      return {
        ...prev,
        [zoneName]: updatedZoneRows,
      };
    });
  };

  const infeedShiftSummary = useMemo(() => {
    const summary = {};
    Object.keys(shiftTimings).forEach((shiftKey) => {
      let total = 0;
      Object.keys(infeedData).forEach((zone) => {
        infeedData[zone].forEach((row) => {
          const cell = row.shiftData?.[shiftKey];
          if (cell && cell.active) {
            total += Number(cell.resource || 0);
          }
        });
      });
      summary[shiftKey] = total;
    });
    return summary;
  }, [infeedData, shiftTimings]);

  const zoneShiftSummary = useMemo(() => {

    const summary = {};

    Object.keys(shiftTimings).forEach((shiftKey) => {

      const zones = zoneData[shiftKey] || {};

      let total = 0;

      Object.keys(zones).forEach((zone) => {

        (zones[zone] || []).forEach((row) => {

          const hasValidRange = String(row.chuteRange || "").trim() !== "";

          const count = Number(row.resourceCount || 0);
          const hasError = String(row.error || "").trim() != "";

          if (hasValidRange && count > 0 && !hasError) {

            total += count;

          }

        });

      });

      summary[shiftKey] = total;

    });

    return summary;

  }, [zoneData, shiftTimings]);


  const totalInfeedResources = useMemo(() => {

    return Object.values(infeedShiftSummary).reduce((sum, value) => sum + value, 0);

  }, [infeedShiftSummary]);

  const totalZoneResources = useMemo(() => {

    return Object.values(zoneShiftSummary).reduce((sum, value) => sum + value, 0);

  }, [zoneShiftSummary]);

  const selectedShiftTiming = useMemo(() => {
    const found = visibleShiftOptions.find((shift) => shift.id === selectedShift);
    return found ? found.timing : "";
  }, [visibleShiftOptions, selectedShift]);

  const buildZoneDataFromRawForShift = (shiftId) => {
    const shiftRows = zoneRawData.filter(
      (row) => String(row.shiftId) === String(shiftId)
    );

    if (!shiftRows.length) {
      return null;
    }


    const grouped = {
      South: {},
      East: {},
      North: {},
      West: {},
    };

    shiftRows.forEach((row) => {
      const zoneName = ZONE_NAME_MAP[row.zoneId] || row.zoneId;
      const chuteRange = getChuteRangeLabel(row.chuteIdRaw);

      if (!grouped[zoneName][chuteRange]) {
        grouped[zoneName][chuteRange] = {
          chuteRange,
          resourceCount: 0,
          error: "",
        };
      }

      grouped[zoneName][chuteRange].resourceCount += 1;
    });

    return {
      South: Object.values(grouped.South),
      East: Object.values(grouped.East),
      North: Object.values(grouped.North),
      West: Object.values(grouped.West),
    };
  };


  useEffect(() => {
    if (zoneDataFromState && Object.keys(zoneDataFromState).length) return;
    if (!zoneFile) return;
    const parseZoneFileData = async () => {
      try {
        const parsed = await parseZoneExcelFile(zoneFile);
        setZoneRawData(parsed);
      } catch (error) {
        console.error("Error parsing zone file:", error);
      }
    };
    parseZoneFileData();
  }, [zoneFile, zoneDataFromState]);


  useEffect(() => {

    if (zoneDataFromState && Object.keys(zoneDataFromState).length) return;

    if (!zoneRawData.length) return;

    const groupedByShift = {};

    zoneRawData.forEach((row) => {

      const shiftKey = String(row.shiftId);

      const zoneName = ZONE_NAME_MAP[row.zoneId] || row.zoneId;

      const chuteRange = getChuteRangeLabel(row.chuteIdRaw);

      if (!groupedByShift[shiftKey]) {

        groupedByShift[shiftKey] = {

          South: {},

          East: {},

          North: {},

          West: {},

        };

      }

      if (!groupedByShift[shiftKey][zoneName][chuteRange]) {

        groupedByShift[shiftKey][zoneName][chuteRange] = {

          chuteRange,
          chuteIds: parseChuteList(row.chuteIdRaw),

          resourceCount: 0,

          error: "",

        };

      }

      groupedByShift[shiftKey][zoneName][chuteRange].resourceCount += 1;

    });

    const finalData = {};

    Object.keys(groupedByShift).forEach((shiftKey) => {

      finalData[shiftKey] = {};

      ZONE_ORDER.forEach((zone) => {

        finalData[shiftKey][zone] = Object.values(groupedByShift[shiftKey][zone] || {});

      });

    });

    setZoneData(finalData);

  }, [zoneRawData, zoneDataFromState]);





  const handleZoneResourceChange = (shift, zone, index, value) => {

    const numericValue = value === "" ? "" : Number(value);

    setZoneData((prev) => {

      const updated = { ...prev };

      const rows = [...(updated[shift]?.[zone] || [])];

      rows[index] = {

        ...rows[index],

        resourceCount: numericValue,

      };

      updated[shift] = {

        ...updated[shift],

        [zone]: rows,

      };

      return updated;

    });

  };


  const buildManualChuteIds = (value) => {

    const cleaned = String(value || "").trim();

    if (!cleaned) return [];

    if (cleaned.includes(",")) {

      return cleaned

        .split(",")

        .map((item) => item.trim())

        .filter((item) => item !== "");

    }

    if (cleaned.includes("-")) {

      const [start, end] = cleaned.split("-").map((item) => Number(item.trim()));

      if (Number.isNaN(start) || Number.isNaN(end) || start > end) return [];

      const result = [];

      for (let i = start; i <= end; i++) {

        result.push(String(i));

      }

      return result;

    }

    const single = Number(cleaned);

    if (Number.isNaN(single)) return [];

    return [String(single)];

  };


  const handleZoneRangeChange = (shift, zone, index, value) => {

    setZoneData((prev) => {

      const updated = { ...prev };

      const rows = [...(updated[shift]?.[zone] || [])];

      const errorMessage = validateZoneChuteRange(zone, value);

      rows[index] = {

        ...rows[index],

        chuteRange: value,
        chuteIds: errorMessage ? [] : buildManualChuteIds(value),

        error: errorMessage,

        resourceCount: errorMessage ? 0 : rows[index].resourceCount,

      };

      updated[shift] = {

        ...updated[shift],

        [zone]: rows,

      };

      return updated;

    });

  };

  const handleAddZoneRow = (shift, zone) => {

    setZoneData((prev) => {

      const updated = { ...prev };

      const existingRows = [...(updated[shift]?.[zone] || [])];

      updated[shift] = {

        ...updated[shift],

        [zone]: [...existingRows, createBlankZoneRow()],

      };

      return updated;

    });

  };






  const handleBack = () => {
    navigate("/create-experiment/distribution", {
      state: buildNavigationState(),
    });
  };
  const handleNext = () => {
    navigate("/acceptable-parcel-units", {
      state: buildNavigationState(),
    });
  };
  return (
    <div className="mapping-page">
      <AppHeader />
      <div className="mapping-shell">
        <div className="page-title-block">
          <h1 className="page-title">{headingText}</h1>
          <p className="page-subtitle">

            Review and update resource distribution in a compact single-page view.
          </p>
        </div>
        <div className="stepper">

          {steps.map((step, i) => (
            <div className="step-item" key={i}>
              <div className={i <= 1 ? "circle active" : "circle"}>{i + 1}</div>
              <span className="step-label">{step}</span>

              {i !== steps.length - 1 && (
                <div className={i < 1 ? "line active" : "line"} />

              )}
            </div>

          ))}
        </div>
        <div className="summary-card">
          <div className="summary-grid">
            <div>
              <span className="summary-label">Experiment Title</span>
              <p>{experimentTitle}</p>
            </div>
            <div>
              <span className="summary-label">Sorting Type</span>
              <p>{sortingType}</p>
            </div>
            <div>
              <span className="summary-label">UseCase Id</span>
              <p>{useCaseId}</p>
            </div>
            <div>
              <span className="summary-label">Parcel Count</span>
              <p>{parcelCount ?? "--"}</p>
            </div>
            <div>
              <span className="summary-label">Date</span>
              <p>{selectedDate}</p>
            </div>
            <div>
              <span className="summary-label">Start Time</span>
              <p>{startTime}</p>
            </div>
            <div>
              <span className="summary-label">End Time</span>
              <p>{endTime}</p>
            </div>
          </div>
        </div>
        <div className="single-page-layout">
          <div className="work-panel">
            <div className="section-card compact-card">
              <div

                className={`collapsible-header ${openSection === "infeed" ? "open" : ""}`}

                onClick={() =>

                  setOpenSection((prev) => (prev === "infeed" ? "" : "infeed"))

                }
              >
                <div>
                  <h3>Infeed Resource Mapping</h3>
                  <p>Edit infeed resources zone-wise for the selected shift</p>
                </div>
                <span className="collapse-icon">

                  {openSection === "infeed" ? "−" : "+"}
                </span>
              </div>

              {openSection === "infeed" && (
                <div className="collapsible-body">
                  <div className="zone-tabs">

                    {["South", "North"].map((zone) => (
                      <button

                        key={zone}

                        type="button"

                        className={`zone-tab ${selectedZoneTab === zone ? "active" : ""}`}

                        onClick={() => setSelectedZoneTab(zone)}
                      >

                        {zone}
                      </button>

                    ))}
                  </div>

                  {loading ? (
                    <div className="loading-box">Reading infeed file...</div>

                  ) : (
                    <div className="compact-table-wrapper">
                      <table className="compact-table">
                        <thead>
                          <tr>
                            <th>Infeed</th>
                            <th>TC</th>
                            <th>No. of Resources</th>
                            <th>Status</th>
                          </tr>
                        </thead>
                        <tbody>

                          {infeedData[selectedZoneTab]?.map((row, index) => {

                            const cell = row.shiftData?.[selectedShift] || {

                              resource: 0,

                              active: true,

                            };

                            return (
                              <tr key={`${selectedZoneTab}-${row.infeed}-${row.tc}`}>
                                <td>{row.infeed}</td>
                                <td>{row.tc}</td>
                                <td>
                                  <input

                                    type="number"

                                    min="0"

                                    value={cell.resource}

                                    disabled={!cell.active}

                                    onChange={(e) =>

                                      handleResourceChange(

                                        selectedZoneTab,

                                        index,

                                        e.target.value

                                      )

                                    }

                                    className={`compact-input ${!cell.active ? "disabled" : ""}`}

                                  />
                                </td>
                                <td>
                                  <label className="switch">
                                    <input

                                      type="checkbox"

                                      checked={cell.active}

                                      onChange={() =>

                                        handleStatusToggle(selectedZoneTab, index)

                                      }

                                    />
                                    <span className="slider"></span>
                                  </label>
                                </td>
                              </tr>

                            );

                          })}
                        </tbody>
                      </table>
                    </div>

                  )}
                </div>

              )}
            </div>
            <div className="section-card compact-card">
              <div

                className={`collapsible-header ${openSection === "zone" ? "open" : ""}`}

                onClick={() =>

                  setOpenSection((prev) => (prev === "zone" ? "" : "zone"))

                }
              >
                <div>
                  <h3>Zone Resource Mapping</h3>
                  <p>Update chute range resource counts for the selected shift</p>
                </div>
                <span className="collapse-icon">

                  {openSection === "zone" ? "−" : "+"}
                </span>
              </div>

              {openSection === "zone" && (
                <div className="collapsible-body">
                  <div className="zone-tabs zone-tabs-zone">

                    {ZONE_ORDER.map((zone) => (
                      <button

                        key={zone}

                        type="button"

                        className={`zone-tab ${selectedZoneResourceTab === zone ? "active" : ""}`}

                        onClick={() => setSelectedZoneResourceTab(zone)}
                      >

                        {zone}
                      </button>

                    ))}
                  </div>
                  <div className="zone-section-header">
                    <div>
                      <h4>{selectedZoneResourceTab} Zone</h4>
                      <p>Chute range-wise resource allocation</p>
                    </div>
                    <button

                      type="button"

                      className="add-zone-row-btn"

                      onClick={() => handleAddZoneRow(selectedShift, selectedZoneResourceTab)}
                    >

                      + Add Resource
                    </button>
                  </div>

                  <div className="compact-table-wrapper zone-table-wrapper">
                    <table className="compact-table zone-compact-table">
                      <thead>
                        <tr>
                          <th>Chute Range</th>
                          <th>No. of Resources</th>
                        </tr>
                      </thead>
                      <tbody>

                        {(zoneData[selectedShift]?.[selectedZoneResourceTab] || []).map((row, index) => (
                          <tr key={`${selectedShift}-${selectedZoneResourceTab}-${index}`}>
                            <td>
                              <div className="zone-input-cell">
                                <input

                                  type="text"

                                  placeholder="e.g. 101-114"

                                  value={row.chuteRange}

                                  onChange={(e) =>

                                    handleZoneRangeChange(

                                      selectedShift,

                                      selectedZoneResourceTab,

                                      index,

                                      e.target.value

                                    )

                                  }

                                  className="compact-input zone-range-input"

                                />
                                {row.error && <p className="zone-field-error">{row.error}</p>}
                              </div>
                            </td>
                            <td>
                              <input

                                type="number"

                                min="0"

                                value={row.resourceCount}

                                onChange={(e) =>

                                  handleZoneResourceChange(

                                    selectedShift,

                                    selectedZoneResourceTab,

                                    index,

                                    e.target.value

                                  )

                                }

                                className="compact-input zone-resource-input"
                                disabled={!!row.error || !String(row.chuteRange || "").trim()}

                              />
                            </td>
                          </tr>

                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>


              )}
            </div>


          </div>
          <div className="summary-side-panel">
            <div className="section-card">
              <div className="section-header summary-header-with-btn">
                <h3>Shift Summary</h3>
                <button

                  type="button"

                  className="add-shift-btn"

                  onClick={handleAddShift}
                  disabled={!canAddShift}
                >

                  + Add Shift
                </button>
              </div>
              <div className="shift-selector-box">
                <label className="summary-label">Select Shift</label>
                <select

                  value={selectedShift}

                  onChange={handleShiftChange}

                  className="shift-dropdown"
                >

                  {visibleShiftOptions.map((shift) => (
                    <option key={shift.id} value={shift.id}>

                      {shift.label}
                    </option>

                  ))}
                </select>
                <input

                  type="text"

                  className="shift-timing-input"

                  // placeholder="23:00:00 - 07:00:00"

                  value={selectedShiftTiming}

                  // onChange={(e) => handleShiftTimingChange(e.target.value)}
                  readOnly

                />

                {!canAddShift && (
                  <p className="shift-note">
                    Extra sift cannot be added because selected end time already reaches 07:00:00.
                  </p>
                )}
              </div>
              <div className="hours-table">
                <div className="hours-header three-col">
                  <span>Shift</span>
                  <span>Infeed</span>
                  <span>Zone</span>

                </div>

                {Object.keys(shiftTimings).map((shift) => (
                  <div

                    key={shift}

                    className={`hours-row  three-col ${selectedShift === shift ? "selected" : ""}`}
                  >
                    <span>Shift {shift}</span>
                    <span>{infeedShiftSummary[shift] || 0}</span>
                    <span>{zoneShiftSummary[shift] || 0}</span>

                  </div>

                ))}
                <div className="hours-total three-col">
                  <span>Total</span>
                  <span>{totalInfeedResources}</span>
                  <span>{totalZoneResources}</span>

                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="button-row">
          <button type="button" className="back-btn" onClick={handleBack}>

            Back
          </button>
          <button type="button" className="next-btn" onClick={handleNext}>

            Next
          </button>
        </div>
      </div>
    </div>

  );

};

export default ChuteMapping;
