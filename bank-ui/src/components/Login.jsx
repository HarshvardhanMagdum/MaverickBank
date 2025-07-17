import { useState } from "react"
import axios from "axios"
import { Link, useNavigate } from "react-router-dom"
import { Eye, EyeSlash } from 'react-bootstrap-icons'
import './../css/login.css'
import { setUserDetails } from "../store/actions/UserAction"
import { useDispatch } from "react-redux"


function Login() {

    const navigate = useNavigate()
    const dispatch = useDispatch()

    let [username, setUsername] = useState("")
    let [password, setPassword] = useState("")
    let [msg, setMsg] = useState("")
    const [showPassword, setShowPassword] = useState(false);

    const processLogin = async () => {
        let encodedString = window.btoa(username+":"+password)
        try{
            const response = await axios.get("http://localhost:8080/api/user/token",{
                headers : { "Authorization":"Basic "+encodedString}
            })
            // console.log(response.data.token)
            let token = response.data.token
            localStorage.setItem("token", token)
            
            let details = await axios.get("http://localhost:8080/api/user/details",{
                headers: {"Authorization":"Bearer "+token}
            })
            // console.log(details)
            let user = {
                'username': username,
                'role': details.data.user.role
            }
            localStorage.setItem("user", JSON.stringify(user));
            setUserDetails(dispatch)(user)
            

            let role = details.data.user.role
            switch (role) {
                case "CUSTOMER":
                    navigate("/customer")
                    break;
                case "EXECUTIVE":
                    localStorage.setItem("branchId", details.data.branch.id)
                    navigate("/executive")
                    break;
                case "MANAGER":
                    navigate("/manager")
                    break;
                case "ADMIN":
                    navigate("/admin")
                    break;
                default:
                    setMsg("Login Disabled!.. Contact admin at admin@example.com")
                    break;
            }
            setMsg("Login Seccess!!")
        }catch(err){
            setMsg("Invalid Credentials!!")
            if(err.response.data.msg == "INACTIVE")
                setMsg("Account is deactivated, please contact admin at admin@mavk.com")
        }
    }

    return (
        <div className="login-container-fluid min-vh-100 d-flex  align-items-center " >
            <div className="row w-100">

                <div className="col-md-6 d-flex flex-column justify-content-center px-5">
                    <h1 className="display-5 text-white fw-bold mb-3">Welcome to Maveric Bank</h1>
                    <p className="lead text-white login-text-shadow-md">
                        Manage your finances with ease. Secure, reliable, and accessible banking at your fingertips.
                        Log in to check your account, transfer funds, apply for loans, and more!
                    </p>
                    <p className="text-white mt-3">
                        Need help? Contact support@maverickbank.com
                    </p>
                </div>

                <div className="col-md-6 d-flex justify-content-center">
                    <div className="card shadow-sm w-75 p-4">
                        <div className="card-body">
                            <div className="text-center mb-4">
                                <img src="/images/logo-transparent.png" className="login-logo" />
                                <h3 className="login-card-title login-gradient-text">LOGIN</h3>
                            </div>
                            {
                                msg!=""?<div>
                                    <div className="alert alert-info">{msg}</div>
                                </div>:""
                            }
                            <form>
                            <div className="form-floating mb-3">
                                <input type="email" className="form-control" placeholder="name@example.com"
                                    onChange={(e) => setUsername(e.target.value)} autoComplete="username"/>
                                <label>Email address</label>
                            </div>
                            <div className="form-floating">
                                <input type={showPassword ? "text" : "password"} className="form-control" placeholder="Password"
                                    onChange={(e) => setPassword(e.target.value)} autoComplete="current-password"/>
                                <label>Password</label>
                                <span
                                    onClick={() => setShowPassword(!showPassword)}
                                    style={{
                                        position: "absolute",
                                        right: "10px",
                                        top: "50%",
                                        transform: "translateY(-50%)",
                                        cursor: "pointer",
                                        zIndex: 2,
                                    }}
                                >
                                    {showPassword ? <EyeSlash /> : <Eye />}
                                </span>
                            </div>
                            </form>
                            <div className="d-grid gap-2 mt-3">
                                <button className="login-btn btn-primary rounded-pill" onClick={() => processLogin()}>LOGIN</button>
                            </div>
                            <div className="text-center mt-3">
                                For Customer, Don't have an account?{" "}
                                <Link onClick={() => navigate("/signup")} to="/signup">Sign Up</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Login