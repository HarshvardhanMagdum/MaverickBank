import { Link, useLocation, useNavigate } from "react-router-dom";
import { setUserDetails } from "../../store/actions/UserAction";
import { useDispatch } from "react-redux";

function ExecutiveSidebar({ setIsClosed, overlayRef, wrapperRef }) {
    const location = useLocation();

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleLinkClick = () => {
        setIsClosed(true); // close the sidebar
        if (overlayRef.current) overlayRef.current.style.display = 'none';
        if (wrapperRef.current) wrapperRef.current.classList.remove('toggled');
    };

    const logout = () => {
        let u = {
            "username": "",
            "role": ""
        }
        setUserDetails(dispatch)(u)
        localStorage.clear();
        navigate("/")
    }

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
                        <Link to="/executive" onClick={handleLinkClick}>
                            <i className="bi bi-building" style={{ marginRight: "10px" }}></i>
                            My Branch
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/accounts" onClick={handleLinkClick}>
                            <i className="bi bi-safe" style={{ marginRight: "10px" }} />
                            Accounts
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/transactions" onClick={handleLinkClick}>
                            <i className="bi bi-currency-exchange" style={{ marginRight: "10px" }} />
                            Transactions
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/loans" onClick={handleLinkClick}>
                            <i className="bi bi-cash-stack" style={{ marginRight: "10px" }} />
                            Loans
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/createAccount" onClick={handleLinkClick}>
                            <i className="bi bi-file-earmark-plus" style={{ marginRight: "10px" }} />
                            Create Account
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/addCustomer" onClick={handleLinkClick}>
                            <i className="bi bi-person-fill-add" style={{ marginRight: "10px" }} />
                            Add Customer
                        </Link>
                    </li>
                    <li>
                        <Link to="/executive/profile" onClick={handleLinkClick}>
                            <i className="bi bi-person" style={{ marginRight: "10px" }} />
                            Profile
                        </Link>
                    </li>
                    <li>
                        <Link to="/#" onClick={()=>{logout()}}>
                            <i className="bi bi-box-arrow-right" style={{ marginRight: "10px" }} />
                            Log Out
                        </Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
}

export default ExecutiveSidebar;