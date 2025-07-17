import React, { useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AddCustomer() {
    const navigate = useNavigate();
    const branchId = localStorage.getItem("branchId");

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [gender, setGender] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [msg, setMsg] = useState("");

    const breadcrumbItems = [
        { label: "Add Customer", command: () => navigate("/executive/add-customer") }
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/executive") };

    const postCustomer = async (e) => {
        e.preventDefault();
        try {
            await axios.post("http://localhost:8080/api/customer/post", {
                firstName,
                lastName,
                email,
                dateOfBirth,
                gender,
                address,
                phoneNumber,
                username,
                password
            },{
                headers: { 'Authorization': `Bearer ${localStorage.getItem("token")}` }
            });
            setMsg("Customer created successfully.");
        } catch (err) {
            console.error("Failed to create customer", err);
            setMsg("Something went wrong.");
        }
    };

    return (
        <div className="container-fluid py-4">
            <div className="row mb-3">
                <div className="col-12 col-lg-10 offset-lg-1">
                    <BreadCrumb model={breadcrumbItems} home={home} />
                </div>
            </div>

            <div className="row justify-content-center">
                <div className="col-12 col-lg-10">
                    <div className="card shadow rounded-4 p-4">
                        <h2 className="text-center fw-bold mb-4 main-title">Register New Customer</h2>

                        {msg && <div className="alert alert-info text-center">{msg}</div>}

                        <form className="row g-3" onSubmit={postCustomer}>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="First Name" onChange={e => setFirstName(e.target.value)} required />
                                <label>First Name</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="Last Name" onChange={e => setLastName(e.target.value)} required />
                                <label>Last Name</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="date" className="form-control" placeholder="Birthday" onChange={e => setDateOfBirth(e.target.value)} required />
                                <label>Birthday</label>
                            </div>

                            <div className="col-md-6 d-flex align-items-center">
                                <label className="me-3">Gender:</label>
                                <div className="form-check me-3">
                                    <input className="form-check-input" type="radio" name="gender" value="MALE" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Male</label>
                                </div>
                                <div className="form-check me-3">
                                    <input className="form-check-input" type="radio" name="gender" value="FEMALE" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Female</label>
                                </div>
                                <div className="form-check">
                                    <input className="form-check-input" type="radio" name="gender" value="OTHER" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Other</label>
                                </div>
                            </div>

                            <div className="col-md-6 form-floating">
                                <input type="email" className="form-control" placeholder="Email" onChange={e => setEmail(e.target.value)} required />
                                <label>Email</label>
                            </div>
                            <div className="col-md-6 form-floating">
                                <input type="tel" className="form-control" placeholder="Phone" onChange={e => setPhoneNumber(e.target.value)} required />
                                <label>Phone</label>
                            </div>
                            <div className="col-md-6 form-floating">
                                <input type="text" className="form-control" placeholder="Address" onChange={e => setAddress(e.target.value)} required />
                                <label>Address</label>
                            </div>

                            <div className="col-md-6 form-floating">
                                <input type="text" className="form-control" placeholder="Username" onChange={e => setUsername(e.target.value)} required />
                                <label>Username</label>
                            </div>
                            <div className="col-md-6 form-floating">
                                <input type="txt" className="form-control" placeholder="Password" onChange={e => setPassword(e.target.value)} required />
                                <label>Password</label>
                            </div>

                            <div className="d-grid gap-2 col-4 mx-auto mt-3">
                                <button type="submit" className="btn btn-primary rounded-pill">Register</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddCustomer;
