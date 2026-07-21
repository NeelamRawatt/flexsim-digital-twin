import React, { useState } from "react";

import "./Login.css";

import { useNavigate } from "react-router-dom";

const Login = () => {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({

    username: "",

    password: "",

  });

  const [fieldErrors, setFieldErrors] = useState({

    username: "",

    password: "",

  });

  const [generalError, setGeneralError] = useState("");

  const handleChange = (e) => {

    const { name, value } = e.target;

    setFormData((prev) => ({

      ...prev,

      [name]: value,

    }));

    setFieldErrors((prev) => ({

      ...prev,

      [name]: "",

    }));

    setGeneralError("");

  };

  const validateForm = () => {

    const errors = {

      username: "",

      password: "",

    };

    let isValid = true;

    if (!formData.username.trim()) {

      errors.username = "Username is required";

      isValid = false;

    }

    if (!formData.password.trim()) {

      errors.password = "Password is required";

      isValid = false;

    }

    setFieldErrors(errors);

    setGeneralError("");

    return isValid;

  };

  const handleSubmit = async (e) => {

  e.preventDefault();

  // reset errors

  setFieldErrors({ username: "", password: "" });

  setGeneralError("");

  // frontend validation

  if (!formData.username) {

    setFieldErrors((prev) => ({ ...prev, username: "Username is required" }));

    return;

  }

  if (!formData.password) {

    setFieldErrors((prev) => ({ ...prev, password: "Password is required" }));

    return;

  }

  try {

    const response = await fetch("http://localhost:8080/api/auth/login", {

      method: "POST",

      headers: {

        "Content-Type": "application/json",

      },

      body: JSON.stringify(formData),

    });

    const data = await response.json();

    if (response.ok) {

      // Store username in localStorage
      if (data.username) {
        localStorage.setItem("username", data.username);
      }

      navigate("/sorting");

    } else {

      // backend structured error

      if (data.field) {

        setFieldErrors((prev) => ({

          ...prev,

          [data.field]: data.message,

        }));

      } else {

        setGeneralError(data.message || "Login failed");

      }

    }

  } catch (error) {

    console.error(error);

    setGeneralError("Server error");

  }

};
 
  return (
    <div className="login-page">
      <div className="login-card">
        <h2>Koge Terminal</h2>
        <p className="subtitle">Flexsim Dashboard Login</p>

        {generalError && <div className="general-error">{generalError}</div>}
        <form onSubmit={handleSubmit} noValidate>
          <div className="input-group">
            <label htmlFor="username">User ID</label>
            <input

              id="username"

              type="text"

              name="username"

              value={formData.username}

              onChange={handleChange}

              placeholder="Enter your user ID"

              className={fieldErrors.username ? "input-error" : ""}

            />

            {fieldErrors.username && (
              <p className="field-error">{fieldErrors.username}</p>

            )}
          </div>
          <div className="input-group">
            <label htmlFor="password">Password</label>
            <input

              id="password"

              type="password"

              name="password"

              value={formData.password}

              onChange={handleChange}

              placeholder="Enter your password"

              className={fieldErrors.password ? "input-error" : ""}

            />

            {fieldErrors.password && (
              <p className="field-error">{fieldErrors.password}</p>

            )}
          </div>
          <button type="submit" className="login-btn">

            Login
          </button>
        </form>
      </div>
    </div>

  );

};

export default Login;
