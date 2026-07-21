import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./LiveExperiments.css";

const LiveExperiments = () => {
  const navigate = useNavigate();
  const [activeExperiment, setActiveExperiment] = useState(null);

  useEffect(() => {
    const readActiveExperiment = () => {
      try {
        const rawValue = localStorage.getItem("activeExperiment");
        setActiveExperiment(rawValue ? JSON.parse(rawValue) : null);
      } catch (error) {
        console.error("Unable to parse active experiment:", error);
        setActiveExperiment(null);
      }
    };

    readActiveExperiment();
    const intervalId = setInterval(readActiveExperiment, 3000);

    return () => clearInterval(intervalId);
  }, []);

  const statusText = useMemo(() => {
    if (!activeExperiment) return "No running experiment";
    return activeExperiment.status || "RUNNING";
  }, [activeExperiment]);

  const openLiveMetrics = () => {
    if (!activeExperiment?.experimentId) return;

    navigate(`/live-experiment/${activeExperiment.experimentId}`, {
      state: {
        ...activeExperiment,
        mode: "live",
      },
    });
  };

  return (
    <div className="live-exp-page">
      <AppHeader />
      <div className="live-exp-shell">
        <h1 className="live-exp-title">Live Experiment</h1>
        <p className="live-exp-subtitle">
          Real-time monitoring for active experiments.
        </p>

        <section className="live-exp-card">
          <div className="live-exp-row">
            <span className="live-exp-label">Current Status</span>
            <span className={`live-exp-status ${activeExperiment ? "active" : "idle"}`}>
              {statusText}
            </span>
          </div>

          {activeExperiment ? (
            <>
              <div className="live-exp-grid">
                <div>
                  <div className="live-exp-label">Experiment ID</div>
                  <div className="live-exp-value">{activeExperiment.experimentId}</div>
                </div>
                <div>
                  <div className="live-exp-label">Experiment Name</div>
                  <div className="live-exp-value">{activeExperiment.experimentName || "-"}</div>
                </div>
                <div>
                  <div className="live-exp-label">Sorting Type</div>
                  <div className="live-exp-value">{activeExperiment.sortingType || "-"}</div>
                </div>
              </div>

              <button type="button" className="live-exp-primary-btn" onClick={openLiveMetrics}>
                Open Live Metrics
              </button>
            </>
          ) : (
            <>
              <p className="live-exp-empty-text">
                No live run is currently tracked for this user. Start a new run from Create Experiment.
              </p>
              <button
                type="button"
                className="live-exp-secondary-btn"
                onClick={() => navigate("/sorting")}
              >
                Go To Create Experiment
              </button>
            </>
          )}
        </section>
      </div>
    </div>
  );
};

export default LiveExperiments;
