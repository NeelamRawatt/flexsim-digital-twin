import React, { useEffect, useMemo, useState } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import {
    RiFlaskLine,
    RiLiveLine,
    RiHistoryLine,
    RiMenuLine,
    RiCloseLine,
    RiLogoutBoxRLine,
    RiUserLine,
    RiTerminalBoxLine,
} from "react-icons/ri";
import "./AppHeader.css";

const AppHeader = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [isCollapsed, setIsCollapsed] = useState(
        () => localStorage.getItem("sidebarCollapsed") === "true"
    );
    const username = localStorage.getItem("username") || "";

    const menuItems = useMemo(
        () => [
            {
                key: "create",
                label: "Create Experiment",
                icon: <RiFlaskLine size={20} />,
                path: "/sorting",
                activeWhen: [
                    "/sorting",
                    "/create-experiment",
                    "/chute-mapping",
                    "/acceptable-parcel-units",
                    "/simulation-settings",
                    "/review-submit",
                ],
            },
            {
                key: "live",
                label: "Live Experiment",
                icon: <RiLiveLine size={20} />,
                path: "/live-experiments",
                activeWhen: ["/live-experiments", "/live-experiment"],
            },
            {
                key: "history",
                label: "Experiment History",
                icon: <RiHistoryLine size={20} />,
                path: "/experiment-history",
                activeWhen: ["/experiment-history"],
            },
        ],
        []
    );

    const isMenuItemActive = (item) =>
        item.activeWhen.some((prefix) => location.pathname.startsWith(prefix));

    useEffect(() => {
        document.body.classList.add("app-shell-enabled");
        document.documentElement.style.setProperty(
            "--app-sidebar-width",
            isCollapsed ? "72px" : "252px"
        );
        localStorage.setItem("sidebarCollapsed", String(isCollapsed));

        return () => {
            document.body.classList.remove("app-shell-enabled");
            document.documentElement.style.setProperty("--app-sidebar-width", "0px");
        };
    }, [isCollapsed]);

    return (
        <>
            <aside className={`app-sidebar ${isCollapsed ? "collapsed" : ""}`}>
                {/* Brand / Logo row */}
                <div className="app-sidebar-brand">
                    {!isCollapsed && (
                        <div className="app-brand-content">
                            <RiTerminalBoxLine size={24} className="app-brand-icon" />
                            <span className="app-brand-name">Koge Terminal</span>
                        </div>
                    )}
                    <button
                        type="button"
                        className="app-sidebar-toggle"
                        onClick={() => setIsCollapsed((prev) => !prev)}
                        aria-label={isCollapsed ? "Expand menu" : "Collapse menu"}
                    >
                        {isCollapsed ? <RiMenuLine size={22} /> : <RiCloseLine size={22} />}
                    </button>
                </div>

                {/* Navigation items */}
                <nav className="app-sidebar-nav">
                    {menuItems.map((item) => {
                        const active = isMenuItemActive(item);
                        return (
                            <NavLink
                                to={item.path}
                                key={item.key}
                                className={`app-menu-item ${active ? "active" : ""}`}
                                title={isCollapsed ? item.label : undefined}
                            >
                                <span className="app-menu-icon">{item.icon}</span>
                                {!isCollapsed && (
                                    <span className="app-menu-label">{item.label}</span>
                                )}
                                {active && !isCollapsed && (
                                    <span className="app-menu-active-dot" />
                                )}
                            </NavLink>
                        );
                    })}
                </nav>

                {/* User badge at bottom */}
                {username && (
                    <div className={`app-sidebar-user ${isCollapsed ? "collapsed" : ""}`}>
                        <div className="app-user-avatar">
                            <RiUserLine size={16} />
                        </div>
                        {!isCollapsed && (
                            <div className="app-user-info">
                                <span className="app-user-name">{username}</span>
                                <span className="app-user-role">Operator</span>
                            </div>
                        )}
                    </div>
                )}
            </aside>

            <header className="app-topbar">
                <div className="app-title-wrap">
                    <h3 className="app-terminal-title">Koge Terminal</h3>
                    <span className="app-topbar-breadcrumb">
                        {menuItems.find((m) => isMenuItemActive(m))?.label || ""}
                    </span>
                </div>
                <button
                    className="app-logout-btn"
                    onClick={() => navigate("/logout")}
                >
                    <RiLogoutBoxRLine size={16} style={{ marginRight: 6, verticalAlign: "middle" }} />
                    Logout
                </button>
            </header>
        </>
    );
};

export default AppHeader;
