import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import AppHeader from "./AppHeader";
import "./ExperimentHistory.css";

const PAGE_SIZE = 8;
const API_BASE_URL = "http://localhost:8080";
const HISTORY_ENDPOINT = `${API_BASE_URL}/api/experiments/history`;

const fallbackRows = [
  {
    experimentId: 1042,
    experimentName: "Distribution Baseline",
    sortingType: "distribution",
    startTime: "2026-04-18T23:00:00.000Z",
    endTime: "2026-04-19T06:30:00.000Z",
    status: "COMPLETED",
    createdAt: "2026-04-18T18:30:00.000Z",
  },
  {
    experimentId: 1043,
    experimentName: "Collection Peak Hour",
    sortingType: "collection",
    startTime: "2026-04-19T15:00:00.000Z",
    endTime: "2026-04-19T22:00:00.000Z",
    status: "FAILED",
    createdAt: "2026-04-19T11:20:00.000Z",
  },
  {
    experimentId: 1044,
    experimentName: "Distribution Stress Test",
    sortingType: "distribution",
    startTime: "2026-04-20T23:00:00.000Z",
    endTime: "2026-04-21T05:40:00.000Z",
    status: "RUNNING",
    createdAt: "2026-04-20T19:15:00.000Z",
  },
];

const formatDateTime = (value) => {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "-";
  return date.toLocaleString();
};

const normalizeRows = (rows) =>
  [...rows].sort((a, b) => new Date(b.createdAt || 0).getTime() - new Date(a.createdAt || 0).getTime());

const mapExperimentRow = (row) => ({
  experimentId: row?.experimentId ?? row?.id ?? "-",
  experimentName: row?.experimentName ?? row?.name ?? row?.experimentTitle ?? "-",
  sortingType: row?.sortingType ?? row?.type ?? row?.simulationType ?? "-",
  startTime: row?.startTime ?? row?.simulationStartTime ?? row?.startDateTime ?? null,
  endTime: row?.endTime ?? row?.simulationEndTime ?? row?.endDateTime ?? null,
  status: row?.status ?? row?.simulationStatus ?? row?.runStatus ?? "UNKNOWN",
  createdAt: row?.createdAt ?? row?.createdDate ?? row?.createdOn ?? null,
});

const extractPagedResponse = (data) => {
  if (Array.isArray(data)) {
    return {
      rows: data.map(mapExperimentRow),
      totalPages: Math.max(1, Math.ceil(data.length / PAGE_SIZE)),
      totalElements: data.length,
    };
  }

  const content = Array.isArray(data?.content)
    ? data.content
    : Array.isArray(data?.items)
      ? data.items
      : [];

  const totalPages = Number(data?.totalPages ?? data?.pageCount ?? 1);
  const totalElements = Number(
    data?.totalElements ?? data?.totalItems ?? data?.count ?? content.length
  );

  return {
    rows: content.map(mapExperimentRow),
    totalPages: Number.isFinite(totalPages) && totalPages > 0 ? totalPages : 1,
    totalElements: Number.isFinite(totalElements) ? totalElements : content.length,
  };
};

const ExperimentHistory = () => {
  const navigate = useNavigate();
  const username = localStorage.getItem("username") || "";
  const [pageNumber, setPageNumber] = useState(1);
  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [fetchError, setFetchError] = useState("");
  const [isUsingFallback, setIsUsingFallback] = useState(false);

  const loadFallbackPage = (targetPage) => {
    try {
      const localRows = JSON.parse(localStorage.getItem("experimentHistory") || "[]");
      const sourceRows = localRows.length ? normalizeRows(localRows) : normalizeRows(fallbackRows);
      const fallbackTotalPages = Math.max(1, Math.ceil(sourceRows.length / PAGE_SIZE));
      const safePage = Math.min(Math.max(targetPage, 1), fallbackTotalPages);
      const offset = (safePage - 1) * PAGE_SIZE;

      setPageNumber(safePage);
      setRows(sourceRows.slice(offset, offset + PAGE_SIZE));
      setTotalPages(fallbackTotalPages);
      setTotalElements(sourceRows.length);
      setIsUsingFallback(true);
      setFetchError("Backend history API unavailable. Showing fallback data.");
    } catch (error) {
      console.error("Unable to load fallback history data:", error);
      const fallbackTotalPages = Math.max(1, Math.ceil(fallbackRows.length / PAGE_SIZE));
      const safePage = Math.min(Math.max(targetPage, 1), fallbackTotalPages);
      const offset = (safePage - 1) * PAGE_SIZE;

      setPageNumber(safePage);
      setRows(fallbackRows.slice(offset, offset + PAGE_SIZE));
      setTotalPages(fallbackTotalPages);
      setTotalElements(fallbackRows.length);
      setIsUsingFallback(true);
      setFetchError("Backend history API unavailable. Showing sample data.");
    }
  };

  useEffect(() => {
    const fetchHistory = async () => {
      setIsLoading(true);
      setFetchError("");

      try {
        const query = new URLSearchParams({
          username,
          page: String(pageNumber - 1),
          size: String(PAGE_SIZE),
          sort: "createdAt,desc",
        });

        const response = await fetch(`${HISTORY_ENDPOINT}?${query.toString()}`);

        if (!response.ok) {
          throw new Error(`History API failed with ${response.status}`);
        }

        const data = await response.json();
        const parsed = extractPagedResponse(data);

        setRows(parsed.rows);
        setTotalPages(parsed.totalPages);
        setTotalElements(parsed.totalElements);
        setIsUsingFallback(false);
      } catch (error) {
        console.error("Unable to load experiment history from backend:", error);
        loadFallbackPage(pageNumber);
      } finally {
        setIsLoading(false);
      }
    };

    fetchHistory();
  }, [pageNumber, username]);

  const statusSummary = useMemo(() => {
    if (isLoading) return "Loading history...";
    if (!rows.length) return "No experiments found for this user.";
    const from = (pageNumber - 1) * PAGE_SIZE + 1;
    const to = Math.min(pageNumber * PAGE_SIZE, totalElements);
    return `Showing ${from}-${to} of ${totalElements} experiments`;
  }, [isLoading, rows.length, pageNumber, totalElements]);

  const openResult = (row) => {
    if (String(row.status || "").toUpperCase() !== "COMPLETED") return;

    navigate(`/live-experiment/${row.experimentId}`, {
      state: {
        mode: "history",
        experimentId: row.experimentId,
        experimentName: row.experimentName,
        sortingType: row.sortingType,
      },
    });
  };

  return (
    <div className="history-page">
      <AppHeader />
      <div className="history-shell">
        <h1 className="history-title">Experiment History</h1>
        <p className="history-subtitle">
          Executed experiments for the current user. Only completed runs can be opened in results view.
        </p>
        <p className={`history-meta ${isUsingFallback ? "warning" : ""}`}>{statusSummary}</p>
        {fetchError && <p className="history-error">{fetchError}</p>}

        <section className="history-table-card">
          <table className="history-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Sorting Type</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Status</th>
                <th>Created At</th>
                <th>Results</th>
              </tr>
            </thead>
            <tbody>
              {!isLoading && rows.length === 0 && (
                <tr>
                  <td className="history-state-cell" colSpan={8}>
                    No experiment records available.
                  </td>
                </tr>
              )}

              {isLoading && (
                <tr>
                  <td className="history-state-cell" colSpan={8}>
                    Loading experiment history...
                  </td>
                </tr>
              )}

              {!isLoading && rows.map((row) => {
                const isCompleted = String(row.status || "").toUpperCase() === "COMPLETED";
                return (
                  <tr key={`${row.experimentId}-${row.createdAt || row.startTime || "row"}`}>
                    <td>{row.experimentId || "-"}</td>
                    <td>{row.experimentName || "-"}</td>
                    <td>{row.sortingType || "-"}</td>
                    <td>{formatDateTime(row.startTime)}</td>
                    <td>{formatDateTime(row.endTime)}</td>
                    <td>
                      <span className={`history-status ${String(row.status || "").toLowerCase()}`}>
                        {row.status || "-"}
                      </span>
                    </td>
                    <td>{formatDateTime(row.createdAt)}</td>
                    <td>
                      <button
                        type="button"
                        className="history-result-btn"
                        disabled={!isCompleted}
                        title={isCompleted ? "View results" : "Results available after completion"}
                        aria-label={isCompleted ? "View results" : "Results unavailable"}
                        onClick={() => openResult(row)}
                      >
                        <span className="history-result-icon" aria-hidden="true">&#128065;</span>
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <div className="history-pagination">
            <button
              type="button"
              onClick={() => setPageNumber((prev) => Math.max(1, prev - 1))}
              disabled={pageNumber === 1 || isLoading}
            >
              Previous
            </button>
            <span>
              Page {pageNumber} of {totalPages}
            </span>
            <button
              type="button"
              onClick={() => setPageNumber((prev) => Math.min(totalPages, prev + 1))}
              disabled={pageNumber === totalPages || isLoading}
            >
              Next
            </button>
          </div>
        </section>
      </div>
    </div>
  );
};

export default ExperimentHistory;
