import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Button } from "primereact/button";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { InputText } from "primereact/inputtext";
import { RadioButton } from "primereact/radiobutton";
import { Dialog } from "primereact/dialog";

function CustomerProfile() {
    const navigate = useNavigate();

    const breadcrumbItems = [
        { label: "Profile", command: () => navigate("/customer/profile") }
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/customer") };

    const [userData, setUserData] = useState({
        firstName: "",
        lastName: "",
        birthday: "",
        gender: "",
        email: "",
        phoneNumber: "",
        address: ""
    });
    useEffect(() => {
        fetchUserDetails()
    }, []);
    
    const fetchUserDetails = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/customer/get", {
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("token")
                }
            });
            setUserData(res.data);
        } catch (err) {
            console.error(err);
        }
    }
    
    const [msg, setMsg] = useState("")
    const [showDialog, setShowDialog] = useState(false);
    const [tempData, setTempData] = useState({
        firstName: "",
        lastName: "",
        dateOfBirth: "",
        gender: "",
        email: "",
        phoneNumber: "",
        address: ""
    });

    const putCustomer = async () => {
        try {
            await axios.put("http://localhost:8080/api/customer/put", tempData, {
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("token")
                }
            });
            setMsg("Profile updated successfully.");
            setShowDialog(false)
            fetchUserDetails()
        } catch (error) {
            console.error(error)
            setMsg("Failed to update profile.")
        }
    };

    return (
        <div className="container-fluid py-4">
            <div className="row mb-2">
                <div className="col-12 col-lg-10 offset-lg-1">
                    <BreadCrumb model={breadcrumbItems} home={home} />
                </div>
            </div>

            <div className="row justify-content-center">
                <div className="col-12 col-lg-10">
                    <div className="card custom-card">
                        <div className="card-body">
                            <div className="text-center title-manage mb-3">
                                <h2>Customer Profile</h2>
                            </div>

                            {msg && <div className="alert alert-info">{msg}</div>}

                            <div className="row g-3 mt-4">
                                <div className="col-md-6">
                                    <label className="fw-bold">Customer ID:</label>
                                    <p>{userData.id}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">First Name:</label>
                                    <p>{userData.firstName}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Last Name:</label>
                                    <p>{userData.lastName}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Date of Birth:</label>
                                    <p>{userData.dateOfBirth}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Gender:</label>
                                    <p>{userData.gender}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Email:</label>
                                    <p>{userData.email}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Phone Number:</label>
                                    <p>{userData.phoneNumber}</p>
                                </div>
                                <div className="col-md-6">
                                    <label className="fw-bold">Address:</label>
                                    <p>{userData.address}</p>
                                </div>
                            </div>

                            <div className="text-end mt-4">
                                <Button label="Edit Profile" icon="pi pi-pencil" onClick={() => setShowDialog(true)} />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <Dialog
                header="Update Profile"
                visible={showDialog}
                style={{ width: "40vw" }}
                onHide={() => setShowDialog(false)}
                onShow={() => setTempData(userData)}
            >
                <div className="p-fluid">
                    <div className="p-field mb-3">
                        <label>First Name</label>
                        <InputText value={tempData.firstName} onChange={(e) => setTempData({ ...tempData, firstName: e.target.value })} />
                    </div>
                    <div className="p-field mb-3">
                        <label>Last Name</label>
                        <InputText value={tempData.lastName} onChange={(e) => setTempData({ ...tempData, lastName: e.target.value })} />
                    </div>
                    <div className="p-field mb-3">
                        <label>Date of Birth</label>
                        <InputText type="date" value={tempData.dateOfBirth} onChange={(e) => setTempData({ ...tempData, dateOfBirth: e.target.value })} />
                    </div>
                    <div className="p-field mb-3">
                        <label>Gender</label>
                        <div className="d-flex">
                            {["MALE", "FEMALE", "OTHER"].map((g) => (
                                <div className="form-check me-3" key={g}>
                                    <RadioButton inputId={g} name="gender" value={g} onChange={(e) => setTempData({ ...tempData, gender: e.value })} checked={tempData.gender === g} />
                                    <label className="ms-2">{g}</label>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="p-field mb-3">
                        <label>Email</label>
                        <InputText type="email" value={tempData.email} onChange={(e) => setTempData({ ...tempData, email: e.target.value })} />
                    </div>
                    <div className="p-field mb-3">
                        <label>Phone Number</label>
                        <InputText type="tel" value={tempData.phoneNumber} onChange={(e) => setTempData({ ...tempData, phoneNumber: e.target.value })} />
                    </div>
                    <div className="p-field mb-3">
                        <label>Address</label>
                        <InputText value={tempData.address} onChange={(e) => setTempData({ ...tempData, address: e.target.value })} />
                    </div>

                    <div className="text-end">
                        <Button label="Update" icon="pi pi-check" onClick={putCustomer} />
                    </div>
                </div>
            </Dialog>
        </div>
    )
}

export default CustomerProfile
