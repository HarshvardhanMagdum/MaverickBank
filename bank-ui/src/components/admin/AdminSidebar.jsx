import { Link, useLocation } from "react-router-dom";

function AdminSidebar({ setIsClosed, overlayRef, wrapperRef }) {
    const location = useLocation();

    const handleLinkClick = () => {
        setIsClosed(true); // close the sidebar
        if (overlayRef.current) overlayRef.current.style.display = 'none';
        if (wrapperRef.current) wrapperRef.current.classList.remove('toggled');
    };

    return (
        <div>
            <nav className="navbar navbar-inverse fixed-top" id="sidebar-wrapper" role="navigation">
                <ul className="nav sidebar-nav">
                    <div className="sidebar-header">
                        <div className="sidebar-brand">
                            <i className="bi bi-bank" style={{ marginRight: "15px" }}></i>
                            <span style={{ fontSize: "20px", color: "white", fontWeight: "bold" }}>MB</span>
                        </div>
                    </div>
                    <li>
                        <Link to="/admin" onClick={handleLinkClick}>
                            <i className="bi bi-people" style={{ marginRight: "10px" }}/>
                            Manager Users
                        </Link>
                    </li>
                    <li>
                        <Link to="/admin/complaints" onClick={handleLinkClick}>
                            <i className="bi bi-headset" style={{ marginRight: "10px" }}/>
                            <div className="justify-content-center">Complaints</div>
                        </Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
}

export default AdminSidebar;