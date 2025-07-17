import { Link, useNavigate } from 'react-router-dom'
import '../../../src/css/CustomerSignup.css'
import { useState } from 'react'
import axios from 'axios'

function CustomerSignUp() {

    let [firstName, setFirstName] = useState("")
    let [lastName, setLastName] = useState("")
    let [birthday, setBirthday] = useState("")
    let [gender, setGender] = useState("")
    let [email, setEmail] = useState("")
    let [phone, setPhone] = useState("")
    let [address, setAddress] = useState("")
    let [username, setUsername] = useState("")
    let [password, setPassword] = useState("")
    let [msg, setMsg] = useState("")

    const navigate = useNavigate()

    const postCustomer = async (e) => {
        e.preventDefault() // prevent form from reload
        if (firstName == "" || lastName == "" || birthday == "" || gender == "" || email == "" || phone == "" || address == "" || username == "" || password == "") {
            setMsg("All fields are required!")
            return
        }
        try {
           await axios.post("http://localhost:8080/api/customer/post", {
    'firstName': firstName,
    'lastName': lastName,
    'email': email,
    'phoneNumber': phone,
    'address': address,
    'gender': gender,
    'dateOfBirth': birthday,
    'username': username,
    'password': password
}, {
    headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
    }
})      
            setMsg("Registration Successful!")
            setTimeout(() => navigate("/"), 2500)
        } catch (error) {
            setMsg("Something went wrong!")
        }
    }

    return (
        <div className="signup-container min-vh-100 d-flex  align-items-center">
            <div className="card signup-card shadow-p4">
                <h2 className="mb-4 text-center signup-title fw-bold ">Customer Registration Form</h2>
                <form className="row g-3" onSubmit={postCustomer}>
                    {
                        msg != "" ? <div>
                            <div className="alert alert-info">{msg}</div>
                        </div> : ""
                    }
                    <div className="col-md-6 form-floating">
                        <input type="text" className="form-control" placeholder="First Name"
                            onChange={(e) => setFirstName(e.target.value)}/>
                        <label>First Name</label>
                    </div>
                    <div className="col-md-6 form-floating">
                        <input type="text" className="form-control" placeholder="Second Name" 
                            onChange={(e) => setLastName(e.target.value)}/>
                        <label>Second Name</label>
                    </div>
                    <div className="col-md-6 form-floating">
                        <input type="date" className="form-control" placeholder="Birthday" 
                            onChange={(e) => setBirthday(e.target.value)}/>
                        <label>Birthday</label>
                    </div>
                    <div className="col-md-6 d-flex align-items-center">
                        <label className='me-3'>Gender:</label>
                        <div className='form-check me-2'>
                            <input className="form-check-input" type="radio" name="gender" value={"MALE"}
                                onChange={(e) => setGender(e.target.value)}/>
                            <label className="form-check-label" >Male</label>
                        </div>
                        <div className='form-check me-2'>
                            <input className="form-check-input" type="radio" name="gender" value={"FEMALE"}
                                onChange={(e) => setGender(e.target.value)}/>
                            <label className="form-check-label" >Female</label>
                        </div>
                        <div className='form-check'>
                            <input className="form-check-input" type="radio" name="gender" value={"OTHER"}
                                onChange={(e) => setGender(e.target.value)}/>
                            <label className="form-check-label" >Other</label>
                        </div>
                    </div>
                    <div className="col-md-6 form-floating">
                        <input type="email" className="form-control" placeholder="Email" 
                            onChange={(e) => setEmail(e.target.value)}/>
                        <label>Email</label>
                    </div>
                    <div className="col-md-6 form-floating">
                        <input type="tel" className="form-control" placeholder="Phone Number" 
                            onChange={(e) => setPhone(e.target.value)}/>
                        <label>Phone Number</label>
                    </div>
                    <div className="col-md-12 form-floating">
                        <input type="text" className="form-control" placeholder="Address" 
                            onChange={(e) => setAddress(e.target.value)}/>
                        <label>Address</label>
                    </div>
                    <div className="col-md-6 form-floating ">
                        <input type="text" className="form-control" placeholder="Username" 
                            onChange={(e) => setUsername(e.target.value)}/>
                        <label>Username</label>
                    </div>
                    <div className="col-md-6 form-floating mb-4">
                        <input type="text" className="form-control" placeholder="Password" 
                            onChange={(e) => setPassword(e.target.value)}/>
                        <label>Password</label>
                    </div>
                    <div className="d-grid gap-2">
                        <button className="login-btn btn-primary rounded-pill" type='submit'>SIGN UP</button>
                    </div>
                    <div className="text-center">
                        Already have an account?{" "}
                        <Link onClick={() => navigate("/")} to="/">Login</Link>
                    </div>
                </form>
            </div>
        </div>
        )
}

export default CustomerSignUp