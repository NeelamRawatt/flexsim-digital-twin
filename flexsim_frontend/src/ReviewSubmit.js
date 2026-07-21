
import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./ReviewSubmit.css";

const steps = [
    "Experiment Configuration",
    "Chute Mapping & Resource Distribution",
    "Acceptable Parcel Units",
    "Simulation Settings",
];

const ReviewSubmit = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const data = location.state || {};

    const {
        sortingType,
        experimentTitle,
        selectedDate,
        startTime,
        endTime,
        parcelCount,
        infeedData = {},
        zoneData = {},
        shiftTimings = {},
        selectedShift,
        extraShiftCount,
        directChuteData = [],
        simulationSettings = {},
    } = data;

    const handleBack = () => {
        navigate("/simulation-settings", { state: data });
    };

    const renderInfeedData = () => {
        const rows = [];

        Object.keys(infeedData || {}).forEach((zone) => {
            (infeedData[zone] || []).forEach((item, index) => {
                rows.push({
                    id: `${zone}-${index}`,
                    zone,
                    infeed: item.infeed,
                    tc: item.tc,
                    shiftData: item.shiftData,
                });
            });
        });

        if (!rows.length) {
            return <p className="review-empty">No infeed data available.</p>;
        }

        return (
            <div className="review-table-wrapper">
                <table className="review-table">
                    <thead>
                        <tr>
                            <th>Zone</th>
                            <th>Infeed</th>
                            <th>TC</th>
                            <th>Shift Data</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rows.map((row) => (
                            <tr key={row.id}>
                                <td>{row.zone}</td>
                                <td>{row.infeed}</td>
                                <td>{row.tc}</td>
                                <td>
                                    <pre className="review-pre">
                                        {JSON.stringify(row.shiftData, null, 2)}
                                    </pre>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    };

    const renderZoneData = () => {
        const shifts = Object.keys(zoneData || {});

        if (!shifts.length) {
            return <p className="review-empty">No zone data available.</p>;
        }

        return shifts.map((shiftKey) => (
            <div key={shiftKey} className="review-subcard">
                <h4>Shift {shiftKey}</h4>
                {Object.keys(zoneData[shiftKey] || {}).map((zone) => (
                    <div key={`${shiftKey}-${zone}`} className="review-zone-block">
                        <h5>{zone}</h5>
                        {(zoneData[shiftKey][zone] || []).length ? (
                            <div className="review-table-wrapper">
                                <table className="review-table">
                                    <thead>
                                        <tr>
                                            <th>Chute Range</th>
                                            <th>Resource Count</th>
                                            <th>Error</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {zoneData[shiftKey][zone].map((row, index) => (
                                            <tr key={`${shiftKey}-${zone}-${index}`}>
                                                <td>{row.chuteRange}</td>
                                                <td>{row.resourceCount}</td>
                                                <td>{row.error || "-"}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        ) : (
                            <p className="review-empty small">No rows</p>
                        )}
                    </div>
                ))}
            </div>
        ));
    };

    return (
        <div className="review-page">
            <AppHeader />

            <div className="review-shell">
                <div className="review-title-block">
                    <h1 className="review-page-title">
                        {sortingType
                            ? `${sortingType.charAt(0).toUpperCase() + sortingType.slice(1)} Sorting Optimization`
                            : "Sorting Optimization"}
                    </h1>
                </div>

                <div className="review-stepper">
                    {steps.map((step, i) => (
                        <div className="review-step-item" key={i}>
                            <div className="review-circle completed">{i + 1}</div>
                            <span className="review-step-label">{step}</span>
                            {i !== steps.length - 1 && <div className="review-line active" />}
                        </div>
                    ))}
                </div>

                <div className="review-card">
                    <h2>Final Review Data</h2>
                    <p>Below is all the data carried from the first page till simulation settings.</p>
                </div>

                <div className="review-grid">
                    <div className="review-card">
                        <h3>Experiment Configuration</h3>
                        <div className="review-kv">
                            <div><span>Experiment Title</span><strong>{experimentTitle || "-"}</strong></div>
                            <div><span>Sorting Type</span><strong>{sortingType || "-"}</strong></div>
                            <div><span>Date</span><strong>{selectedDate || "-"}</strong></div>
                            <div><span>Start Time</span><strong>{startTime || "-"}</strong></div>
                            <div><span>End Time</span><strong>{endTime || "-"}</strong></div>
                            <div><span>Parcel Count</span><strong>{parcelCount ?? "-"}</strong></div>
                            <div><span>Selected Shift</span><strong>{selectedShift || "-"}</strong></div>
                            <div><span>Extra Shift Count</span><strong>{extraShiftCount ?? 0}</strong></div>
                        </div>
                    </div>

                    <div className="review-card">
                        <h3>Shift Timings</h3>
                        {Object.keys(shiftTimings || {}).length ? (
                            <div className="review-table-wrapper">
                                <table className="review-table">
                                    <thead>
                                        <tr>
                                            <th>Shift</th>
                                            <th>Timing</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {Object.entries(shiftTimings).map(([shift, timing]) => (
                                            <tr key={shift}>
                                                <td>{shift}</td>
                                                <td>{timing}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        ) : (
                            <p className="review-empty">No shift timings available.</p>
                        )}
                    </div>
                </div>

                <div className="review-card">
                    <h3>Acceptable Parcel Units</h3>
                    {directChuteData.length ? (
                        <div className="review-table-wrapper">
                            <table className="review-table">
                                <thead>
                                    <tr>
                                        <th>Parameter</th>
                                        <th>Min</th>
                                        <th>Max</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {directChuteData.map((row) => (
                                        <tr key={row.label}>
                                            <td>{row.label}</td>
                                            <td>{row.min}</td>
                                            <td>{row.max}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    ) : (
                        <p className="review-empty">No acceptable parcel unit data available.</p>
                    )}
                </div>

                <div className="review-card">
                    <h3>Simulation Settings</h3>
                    <div className="review-kv">
                        <div><span>KPI Mode</span><strong>{simulationSettings.selectedKpiMode || "-"}</strong></div>
                        <div>
                            <span>Selected KPIs</span>
                            <strong>
                                {simulationSettings.selectedKpis?.length
                                    ? simulationSettings.selectedKpis.join(", ")
                                    : "-"}
                            </strong>
                        </div>
                        <div><span>Max Recirculation Count</span><strong>{simulationSettings.maxRecirculationCount || "-"}</strong></div>
                        <div><span>Parcel Enhance Direction</span><strong>{simulationSettings.parcelEnhanceDirection || "-"}</strong></div>
                        <div><span>Parcel Enhance Value (%)</span><strong>{simulationSettings.parcelEnhanceValue || "-"}</strong></div>
                        <div><span>Adjusted Parcel Count</span><strong>{simulationSettings.adjustedParcelCount ?? "-"}</strong></div>
                    </div>
                </div>

                <div className="review-card">
                    <h3>Infeed Data</h3>
                    {renderInfeedData()}
                </div>

                <div className="review-card">
                    <h3>Zone Data</h3>
                    {renderZoneData()}
                </div>

                <div className="review-button-row">
                    <button className="review-back-btn" onClick={handleBack}>
                        Back
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ReviewSubmit;

