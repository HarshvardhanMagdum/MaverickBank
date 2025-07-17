import { Link, useNavigate } from "react-router-dom";
import '../../css/Dashboard.css';
import { useDispatch, useSelector } from "react-redux";
import { setUserDetails } from "../../store/actions/UserAction";
import Logo from '/images/logo-home-transparent.png'; 

function CustomerNavbar() {
    const user = useSelector(state => state.user)
    const navigate = useNavigate();
    const dispatch = useDispatch()
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
        <div >
            <nav className="navbar justify-content-between"  >
                <div className="d-flex align-items-center ms-5">
                    {/* Logo */}
                    <img
                        src={Logo}
                        alt="Logo"
                        height="60"
                        style={{ objectFit: 'contain' }}
                    />
                </div>
                <div className="d-flex align-items-center gap-4 profile">
                    <span className="">Welcome, <strong>{user.username}</strong></span>
                    <div className="dropdown me-2">
                        <img
                            src="https://cdn-icons-png.flaticon.com/512/149/149071.png"
                            width="35"
                            height="35"
                            alt="profile"
                            className="rounded-circle dropdown-toggle"
                            data-bs-toggle="dropdown"
                            style={{ cursor: 'pointer' }}
                        />
                        <ul className="dropdown-menu dropdown-menu-end">
                            <li className="dropdown-item"><Link to="/customer/profile">Profile Management</Link></li>
                            <li><hr className="dropdown-divider" /></li>
                            <li><button className="dropdown-item" onClick={() => logout()}>Logout</button></li>
                        </ul>
                    </div>
                </div>
            </nav>
        </div>
    )
}

export default CustomerNavbar; 