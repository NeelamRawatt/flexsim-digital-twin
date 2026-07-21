import React, { useEffect, useMemo, useState } from "react";

import "./CreateExperiment.css";
import AppHeader from "./AppHeader";

import { useLocation, useNavigate, useParams } from "react-router-dom";

const steps = [

    "Experiment Configuration",

    "Chute Mapping & Resource Distribution",

    "Acceptable Parcel Units",

    "Simulation Settings",

];

const CreateExperiment = () => {

    const { type } = useParams();

    const location = useLocation();

    const navigate = useNavigate();
    const [parcelCount, setParcelCount] = useState(0);
   
    const [endTimeError, setEndTimeError] = useState("");


    const useCaseId = location.state?.useCaseId || "";

    const [formData, setFormData] = useState({

        experimentTitle: "",

        selectedDate: "",

        startTime: (type === "distribution") ? "23:00:00" : "15:00:00",

        endTime: "",

        infeedFile: null,

        zoneFile: null,

    });




    const headingText = useMemo(() => {

        if (type === "distribution") {

            return "Distribution Sorting Optimization";

        }

        if (type === "collection") {

            return "Collection Sorting Optimization";

        }

        return "Sorting Optimization";

    }, [type]);

    const normalizeToSeconds = (time) => {
        if (!time) return "";
        return time.length === 5 ? `${time}:00` : time;
    };

    const isEndTimeValid = (simulationType, endTime) => {
        const normalizedEndTime = normalizeToSeconds(endTime);
        if (!normalizedEndTime) return true;

        const endSeconds = timeToSeconds(normalizedEndTime);

        if (simulationType === "distribution") {
            return endSeconds >= timeToSeconds("23:00:00") || endSeconds <= timeToSeconds("07:00:00");
        }

        if (simulationType === "collection") {
            return (
                endSeconds >= timeToSeconds("15:00:00") &&
                endSeconds <= timeToSeconds("23:00:00")
            );
        }

        return false;
    };

    const getEndTimeValidationMessage = (simulationType) => {
        if (simulationType === "distribution") {
            return "Distribution end time must be between 23:00:00-23:59:59 or 00:00:00-07:00:00";
        }
        return "Collection end time must be between 15:00:00 and 23:00:00";
    };

    const buildUtcIsoDateTime = (dateValue, timeValue, dayOffset = 0) => {
        if (!dateValue || !timeValue) return "";
        const [year, month, day] = dateValue.split("-").map(Number);
        const normalizedTime = normalizeToSeconds(timeValue);
        const [hour, minute, second] = normalizedTime.split(":").map(Number);

        return new Date(
            Date.UTC(year, month - 1, day + dayOffset, hour || 0, minute || 0, second || 0)
        ).toISOString();
    };

    const timeToSeconds = (time) => {
        if (!time) return 0;
        const parts = time.split(":").map(Number);
        const hh = parts[0] || 0;
        const mm = parts[1] || 0;
        const ss = parts[2] || 0;
        return hh * 3600 + mm * 60 + ss;
    };


    useEffect(() => {
        if (!formData.endTime) {
            setEndTimeError("");
            return;
        }

        if (!isEndTimeValid(type, formData.endTime)) {
            setEndTimeError(getEndTimeValidationMessage(type));
            return;
        }

        setEndTimeError("");
    }, [formData.endTime, type]);
    
  

    const triggerParcelCountIfReady = (nextFormData) => {
        const { selectedDate, startTime, endTime } = nextFormData;
        const normalizedEndTime = normalizeToSeconds(endTime);

        if (!selectedDate || !startTime || !normalizedEndTime) {
            setParcelCount(0);
            return;
        }

        if (!isEndTimeValid(type, normalizedEndTime)) {
            setParcelCount(0);
            return;
        }

        fetchParcelCount(selectedDate, startTime, normalizedEndTime);
    };

    const handleEndTimeChange = (rawValue) => {
        const digits = rawValue.replace(/\D/g, "");
        
        // Limit to 6 digits max (HHMMSS)
        if (digits.length > 6) return;
        
        // Extract potential HH, MM, SS
        const hh = digits.slice(0, 2);
        const mm = digits.slice(2, 4);
        const ss = digits.slice(4, 6);
        
        // Validate each component is in valid range
        if (hh && Number(hh) > 23) return; // Hours must be 0-23
        if (mm && Number(mm) > 59) return; // Minutes must be 0-59
        if (ss && Number(ss) > 59) return; // Seconds must be 0-59
        
        // Build formatted time with colons
        let formatted = hh;
        if (mm) formatted += `:${mm}`;
        if (ss) formatted += `:${ss}`;

        setFormData((prev) => {
            const updatedFormData = { ...prev, endTime: formatted };
            triggerParcelCountIfReady(updatedFormData);
            return updatedFormData;
        });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => {
            const updatedFormData = {
                ...prev,
                [name]: value,
            };

            triggerParcelCountIfReady(updatedFormData);
            return updatedFormData;
        });
    };


   
    const handleFileChange = (e) => {

        const { name, files } = e.target;

        setFormData((prev) => ({

            ...prev,

            [name]: files && files.length > 0 ? files[0] : null,

        }));

    };



    const fetchParcelCount = async (selectedDate, startTime, endTime) => {

        try {
            const normalizedStartTime = normalizeToSeconds(startTime);
            const normalizedEndTime = normalizeToSeconds(endTime);
            const endSeconds = timeToSeconds(normalizedEndTime);
            const isNextDayDistributionEnd =
                type === "distribution" && endSeconds <= timeToSeconds("07:00:00");

            const payload = {
                parcelDate: selectedDate,
                startTime: buildUtcIsoDateTime(selectedDate, normalizedStartTime, 0),
                endTime: buildUtcIsoDateTime(selectedDate, normalizedEndTime, isNextDayDistributionEnd ? 1 : 0),
            };

            const response = await fetch("http://localhost:8080/api/parcels/getParcelCount", {

                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                },

                body: JSON.stringify(payload),

            });

            const data = await response.json();

            if (response.ok) {

                setParcelCount(data?.parcelCount ?? 0);

            } else {

                console.error("API error:", data);

                setParcelCount(0);

            }

        } catch (error) {

            console.error("Fetch error:", error);

            setParcelCount(0);

        }

    };


    const isFormValid =

        formData.experimentTitle.trim() &&

        formData.selectedDate &&

        formData.startTime &&

        formData.endTime &&

        formData.infeedFile &&

        formData.zoneFile &&
        
        !endTimeError

    const handleNext = () => {

        if (!isFormValid) return;

        const normalizedStartTime = normalizeToSeconds(formData.startTime);
        const normalizedEndTime = normalizeToSeconds(formData.endTime);
        const endSeconds = timeToSeconds(normalizedEndTime);
        const isNextDayDistributionEnd =
            type === "distribution" && endSeconds <= timeToSeconds("07:00:00");

        const parcelPayloadStartTime = buildUtcIsoDateTime(formData.selectedDate, normalizedStartTime, 0);
        const parcelPayloadEndTime = buildUtcIsoDateTime(
            formData.selectedDate,
            normalizedEndTime,
            isNextDayDistributionEnd ? 1 : 0
        );

        navigate("/chute-mapping", {

            state: {

                sortingType: type,

                useCaseId,

                experimentTitle: formData.experimentTitle,

                selectedDate: formData.selectedDate,

                startTime: formData.startTime,

                endTime: formData.endTime,

                parcelPayloadStartTime,

                parcelPayloadEndTime,

                infeedFile: formData.infeedFile,

                zoneFile: formData.zoneFile,

                parcelCount

            },

        });

    };

    return (
        <div className="experiment-page">
            <AppHeader />
            <div className="experiment-shell">
                <div className="page-title-block">
                    <h1 className="page-title">{headingText}</h1>
                </div>
                <div className="stepper">

                    {steps.map((step, i) => (
                        <div className="step-item" key={i}>
                            <div className={i === 0 ? "circle active" : "circle"}>{i + 1}</div>
                            <span className="step-label">{step}</span>

                            {i !== steps.length - 1 && (
                                <div className={i === 0 ? "line active" : "line"} />

                            )}

                        </div>

                    ))}
                </div>
                <div className="config-card">
                    <div className="config-header">
                        <h3>Experiment Configuration</h3>
                    </div>
                    <div className="form-grid">
                        <div className="form-row">
                            <label className="field-label title-row">

                                Experiment Title <span className="required">*</span>
                            </label>
                            <div className="field-content ">
                                <input

                                    type="text"

                                    name="experimentTitle"

                                    value={formData.experimentTitle}

                                    onChange={handleChange}

                                    placeholder="Enter Experiment Title"

                                    className="text-input title-input"

                                />
                            </div>

                        </div>

                        <div className="form-row">
                            <label className="field-label">

                                Timeline <span className="required">*</span>
                            </label>
                            <div className="field-content">
                                <div className="timeline-grid">
                                    <div className="mini-field">
                                        <label className="mini-label">Date</label>
                                        <input

                                            type="date"

                                            name="selectedDate"

                                            value={formData.selectedDate}

                                            onChange={(e) => {
                                                const value = e.target.value;
                                                setFormData((prev) => {
                                                    const updatedFormData = {
                                                        ...prev,
                                                        selectedDate: value,
                                                    };

                                                    triggerParcelCountIfReady(updatedFormData);

                                                    return updatedFormData;
                                                });
                                            }}

                                            className="text-input"

                                        />
                                    </div>
                                    <div className="mini-field">
                                        <label className="mini-label">Start Time</label>
                                        <input

                                            type="text"

                                            name="startTime"

                                            step="1"

                                            value={formData.startTime}

                                           
                                            className="text-input"

                                            readOnly
                                            disabled


                                        />

                                    </div>
                                    <div className="mini-field">
                                        <label className="mini-label">End Time</label>
                                        <input

                                            type="text"

                                            name="endTime"

                                            value={formData.endTime}

                                            onChange={(e) => handleEndTimeChange(e.target.value)}

                                            placeholder="HH:MM:SS"

                                            maxLength={8}

                                            className="text-input"

                                        />

                                        {endTimeError && <p className="field-error">{endTimeError}</p>}

                                    </div>

                                    <div className="mini-field">
                                        <label className="mini-label">Parcel Count</label>
                                        <div className="parcel-count-field">
                                            {parcelCount}
                                        </div>
                                        {/* <span className="parcel-value">0</span> */}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="form-row">
                            <label className="field-label">

                                Infeed Resource File <span className="required">*</span>
                            </label>
                            <div className="field-content">
                                <div className="upload-box">
                                    <div className="upload-text">
                                        <div className="upload-title">

                                            {formData.infeedFile ? formData.infeedFile.name : "Select file"}
                                        </div>
                                        <div className="upload-subtitle">

                                            Upload Infeed Resource File
                                        </div>
                                    </div>
                                    <label htmlFor="infeedFile" className="browse-btn">

                                        Browse
                                    </label>
                                    <input

                                        id="infeedFile"

                                        type="file"

                                        name="infeedFile"
                                        accept=".xlsx,.xls,.csv"

                                        hidden

                                        onChange={handleFileChange}

                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-row">
                            <label className="field-label">

                                Zone Resource File <span className="required">*</span>
                            </label>
                            <div className="field-content">
                                <div className="upload-box">
                                    <div className="upload-text">
                                        <div className="upload-title">

                                            {formData.zoneFile ? formData.zoneFile.name : "Select file"}
                                        </div>
                                        <div className="upload-subtitle">

                                            Upload Zone Resource File
                                        </div>
                                    </div>
                                    <label htmlFor="zoneFile" className="browse-btn">

                                        Browse
                                    </label>
                                    <input

                                        id="zoneFile"

                                        type="file"

                                        name="zoneFile"

                                        accept=".xlsx,.xls,.csv"
                                        hidden

                                        onChange={handleFileChange}

                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="button-row">
                        <button

                            type="button"

                            className={`next-btn ${isFormValid ? "" : "disabled"}`}

                            disabled={!isFormValid}

                            onClick={handleNext}
                        >

                            Next
                        </button>
                    </div>
                </div>
            </div>
        </div>

    );

};

export default CreateExperiment;
