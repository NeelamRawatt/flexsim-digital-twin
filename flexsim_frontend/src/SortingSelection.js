import React from "react";

import "./SortingSelection.css";
import AppHeader from "./AppHeader";

import { useNavigate } from "react-router-dom";

const SortingSelection = () => {

  const navigate = useNavigate();

  const handleSelect = (type, useCaseId) => {

    navigate(`/create-experiment/${type}`, {

      state: {

        useCaseId,

      },

    });

  };

  return (
<div className="sorting-page">
<AppHeader />
<div className="sorting-container">
<h1 className="title">Select Sorting Type</h1>
<p className="subtitle">Choose a module to start a new experiment</p>
<div className="card-container">
<div

          className="sorting-card"

          onClick={() => handleSelect("collection", 2)}
>
<div className="icon">📦</div>
<h2>Collection Sorting</h2>
<p>Optimize inbound parcel collection process</p>
</div>
<div

          className="sorting-card"

          onClick={() => handleSelect("distribution", 1)}
>
<div className="icon">🚚</div>
<h2>Distribution Sorting</h2>
<p>Optimize outbound distribution workflow</p>
</div>
</div>
</div>
</div>

  );

};

export default SortingSelection;
 