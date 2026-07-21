import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Logout = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Clear username from localStorage
    localStorage.removeItem("username");
    
    // Redirect to login page after a short delay
    const timer = setTimeout(() => {
      navigate("/login");
    }, 500);

    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div style={{ 
      display: "flex", 
      justifyContent: "center", 
      alignItems: "center", 
      height: "100vh",
      fontSize: "18px"
    }}>
      Logging out...
    </div>
  );
};

export default Logout;
