import React, { useMemo, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./LiveExperimentMetrics.css";

const metricTabs = [
  { key: "parcels", label: "Parcels" },
  { key: "resources", label: "Resources" },
  { key: "induction", label: "Induction" },
  { key: "chutes", label: "Chutes" },
];

const LiveExperimentMetrics = () => {
  const { experimentId } = useParams();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("parcels");

  const sortingType = location.state?.sortingType || "";
  const experimentName = location.state?.experimentName || "";
  const mode = (location.state?.mode || "live").toLowerCase();

  const pageTitle = useMemo(() => {
    if (mode === "history") {
      return "Experiment Results";
    }
    return "Live Experiment Metrics";
  }, [mode]);

  const subtitleText = useMemo(() => {
    if (mode === "history") {
      return experimentName ? `${experimentName} (${experimentId || "-"})` : `Experiment ID: ${experimentId || "-"}`;
    }
    return `Experiment ID: ${experimentId || "-"}`;
  }, [mode, experimentName, experimentId]);

  return (
    <div className="live-metrics-page">
      <AppHeader />
      <div className="live-metrics-shell">
        <h1 className="live-metrics-title">{pageTitle}</h1>
        <p className="live-metrics-subtitle">
          {subtitleText}
        </p>
        <p className="live-metrics-subtitle">
          Sorting Type: {sortingType || "-"}
        </p>

        <div className="live-metrics-tabs" role="tablist" aria-label="Metrics Tabs">
          {metricTabs.map((tab) => (
            <button
              key={tab.key}
              type="button"
              className={`live-metrics-tab ${activeTab === tab.key ? "active" : ""}`}
              onClick={() => setActiveTab(tab.key)}
            >
              {tab.label}
            </button>
          ))}
        </div>

        <div className="live-metrics-placeholder">
          {mode === "live" ? "Real-time " : "Historical "}
          {metricTabs.find((tab) => tab.key === activeTab)?.label} dashboard placeholder.
          Charts and detailed KPIs can be rendered in this panel.
        </div>
      </div>
    </div>
  );
};

export default LiveExperimentMetrics;
