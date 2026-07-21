import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Login";
import Logout from "./Logout";
import SortingSelection from "./SortingSelection";
import CreateExperiment from "./CreateExperiment";
import ChuteMapping from "./ChuteMapping";
import AcceptableParcelUnits from "./AcceptableParcelUnits";
import SimulationSettings from "./SimulationSettings";
import ReviewSubmit from "./ReviewSubmit";
import LiveExperimentMetrics from "./LiveExperimentMetrics";
import LiveExperiments from "./LiveExperiments";
import ExperimentHistory from "./ExperimentHistory";

// Protected Route component
const ProtectedRoute = ({ element }) => {
  const username = localStorage.getItem("username");
  return username ? element : <Navigate to="/login" replace />;
};

// Root redirect component
const RootRedirect = () => {
  const username = localStorage.getItem("username");
  return username ? <Navigate to="/sorting" replace /> : <Navigate to="/login" replace />;
};


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<RootRedirect />} />
        <Route path="/login" element={<Login />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/sorting" element={<ProtectedRoute element={<SortingSelection />} />} />
        <Route path="/create-experiment/:type" element={<ProtectedRoute element={<CreateExperiment />} />} />
        <Route path="/chute-mapping" element={<ProtectedRoute element={<ChuteMapping />} />} />
        <Route path="/acceptable-parcel-units" element={<ProtectedRoute element={<AcceptableParcelUnits />} />} />
        <Route path="/simulation-settings" element={<ProtectedRoute element={<SimulationSettings />} />} />
        <Route path="/live-experiments" element={<ProtectedRoute element={<LiveExperiments />} />} />
        <Route path="/live-experiment/:experimentId" element={<ProtectedRoute element={<LiveExperimentMetrics />} />} />
        <Route path="/experiment-history" element={<ProtectedRoute element={<ExperimentHistory />} />} />
        <Route path="/review-submit" element={<ProtectedRoute element={<ReviewSubmit />} />} />
      </Routes>
    </Router>
  );
}

export default App;