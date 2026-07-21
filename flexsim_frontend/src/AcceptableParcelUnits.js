
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./AcceptableParcelUnits.css";

const steps = [
    "Experiment Configuration",
    "Chute Mapping & Resource Distribution",
    "Acceptable Parcel Units",
    "Simulation Settings",
];

const DEFAULT_DIRECT_CHUTE_DATA = [
    { label: "Height (m)", min: "0", max: "0" },
    { label: "Length (m)", min: "0", max: "0.6" },
    { label: "Width (m)", min: "0", max: "0.4" },
    { label: "Weight (kg)", min: "0", max: "0.65" },
];

const AcceptableParcelUnits = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const previousState = location.state || {};

    const {
        sortingType,
        experimentTitle = "",
        selectedDate = "",
        useCaseId="",
        startTime = "23:00:00",
        endTime = "",
        parcelCount = 0,
        infeedFile = null,
        zoneFile = null,
        infeedData = {},
        zoneData = {},
        zoneRawData=[],
        shiftTimings = {},
        selectedShift = "1",
        extraShiftCount = 0,
        directChuteData: directChuteDataFromState,
        simulationSettings = {},
    } = previousState;

    const [directChuteData, setDirectChuteData] = useState(
        Array.isArray(directChuteDataFromState) && directChuteDataFromState.length > 0
            ? directChuteDataFromState
            : DEFAULT_DIRECT_CHUTE_DATA
    );

    const handleValueChange = (index, field, value) => {
        setDirectChuteData((prev) => {
            const updated = [...prev];
            updated[index] = {
                ...updated[index],
                [field]: value,
            };
            return updated;
        });
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
        simulationSettings,
    });

    const handleBack = () => {
        navigate("/chute-mapping", {
            state: buildNavigationState(),
        });
    };

    const handleNext = () => {
        navigate("/simulation-settings", {
            state: buildNavigationState(),
        });
    };

    return (
        <div className="apu-page">
            <AppHeader />

            <div className="apu-shell">
                <div className="apu-title-block">
                    <h1 className="apu-page-title">
                        {sortingType
                            ? `${sortingType.charAt(0).toUpperCase() + sortingType.slice(1)} Sorting Optimization`
                            : "Sorting Optimization"}
                    </h1>
                </div>

                <div className="apu-stepper">
                    {steps.map((step, i) => (
                        <div className="apu-step-item" key={i}>
                            <div
                                className={
                                    i < 2
                                        ? "apu-circle completed"
                                        : i === 2
                                            ? "apu-circle active"
                                            : "apu-circle"
                                }
                            >
                                {i + 1}
                            </div>
                            <span className="apu-step-label">{step}</span>
                            {i !== steps.length - 1 && (
                                <div className={i < 2 ? "apu-line active" : "apu-line"} />
                            )}
                        </div>
                    ))}
                </div>

                <div className="apu-summary-card">
                    <div className="apu-summary-grid">
                        <div className="apu-summary-item">
                            <span className="apu-summary-label">Experiment Title</span>
                            <span className="apu-summary-value">{experimentTitle || "-"}</span>
                        </div>

                        <div className="apu-summary-item">
                            <span className="apu-summary-label">Sorting Type</span>
                            <span className="apu-summary-value">{sortingType || "-"}</span>
                        </div>

                        <div className="apu-summary-item">
                            <span className="apu-summary-label">Parcel Count</span>
                            <span className="apu-summary-value">{parcelCount}</span>
                        </div>

                        <div className="apu-summary-item">
                            <span className="apu-summary-label">Date</span>
                            <span className="apu-summary-value">{selectedDate || "-"}</span>
                        </div>

                        <div className="apu-summary-item">
                            <span className="apu-summary-label">Start Time</span>
                            <span className="apu-summary-value">{startTime || "-"}</span>
                        </div>

                        <div className="apu-summary-item">
                            <span className="apu-summary-label">End Time</span>
                            <span className="apu-summary-value">{endTime || "-"}</span>
                        </div>
                    </div>
                </div>

                <div className="apu-card">
                    <div className="apu-card-header">
                        <div>
                            <h3>Acceptable Parcel Units</h3>
                            <p>Enter acceptable parcel limits for direct chute.</p>
                        </div>
                    </div>

                    <div className="apu-card-body">
                        <div className="apu-table-wrapper">
                            <table className="apu-table">
                                <thead>
                                    <tr>
                                        <th className="apu-first-head">Direct Chute</th>
                                        <th>Min</th>
                                        <th>Max</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {directChuteData.map((row, index) => (
                                        <tr key={row.label}>
                                            <td className="apu-label-cell">{row.label}</td>
                                            <td>
                                                <input
                                                    type="number"
                                                    step="0.01"
                                                    min="0"
                                                    value={row.min}
                                                    onChange={(e) =>
                                                        handleValueChange(index, "min", Math.max(0,e.target.value))
                                                    }
                                                    className="apu-input"
                                                />
                                            </td>
                                            <td>
                                                <input
                                                    type="number"
                                                    step="0.01"
                                                    min="0"
                                                    value={row.max}
                                                    onChange={(e) =>
                                                        handleValueChange(index, "max", Math.max(0,e.target.value))
                                                    }
                                                    className="apu-input"
                                                />
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>

                        <div className="apu-button-row">
                            <button className="apu-back-btn" onClick={handleBack}>
                                Back
                            </button>

                            <button className="apu-next-btn" onClick={handleNext}>
                                Next
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AcceptableParcelUnits;

