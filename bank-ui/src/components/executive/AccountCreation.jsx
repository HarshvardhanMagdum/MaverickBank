import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Dropdown } from "primereact/dropdown";
import { Button } from "primereact/button";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AccountCreation() {
    const navigate = useNavigate();
    const branchId = localStorage.getItem("branchId");

    const [accountTypes, setAccountTypes] = useState([]);
    const [selectedType, setSelectedType] = useState(null);
    const [customerId, setCustomerId] = useState("");
    const [pan, setPan] = useState("");
    const [aadhar, setAadhar] = useState("");
    const [msg, setMsg] = useState("");

    const breadcrumbItems = [
        { label: "Create Account" }
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/executive") };

    useEffect(() => {
        const fetchTypes = async () => {
            try {
                const res = await axios.get("http://localhost:8080/api/accountType/get-all",{
                    headers: { 'Authorization' : `Bearer ${localStorage.getItem("token")}` }
                });
                setAccountTypes(res.data);
            } catch (err) {
                console.error("Failed to fetch account types", err);
            }
        };
        fetchTypes();
    }, []);

    // State to hold fetched customer info
    const [customerInfo, setCustomerInfo] = useState(null);
    const [custError, setCustError] = useState("");

    // Fetch customer details by ID
    const fetchCustomerDetails = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/customer/get-one/${customerId}`,{
                headers: { 'Authorization' : "Bearer " +localStorage.getItem("token")}
            });
            setCustomerInfo(res.data);
            setCustError("");
        } catch (err) {
            setCustomerInfo(null);
            setCustError("Customer not found.");
        }
    };


    const createAccount = async () => {
        try {
            const res = await axios.post(
                `http://localhost:8080/api/account/post/${customerId}/${branchId}?type=${selectedType.type}`,
                {
                    panNumber: pan,
                    aadharNumber: aadhar
                },{
                    headers: { 'Authorization' : "Bearer " +localStorage.getItem("token")}
                }
            );
            setMsg("Account created successfully!");
        } catch (err) {
            console.error(err);
            setMsg("Failed to create account.");
        }
    };

    return (
        <div className="container mt-4">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-4">
                <h2 className="text-center fw-bold mb-4 mt-2 main-title">Create Account for Customer</h2>

                {msg && <div className="alert alert-info">{msg}</div>}

                <div className="row g-4 mt-4">
                    <div className="col-md-6">
                        <label className="form-label fw-bold">Customer ID</label>
                        <div className="input-group">
                            <input type="number" className="form-control" value={customerId}
                                onChange={(e) => setCustomerId(e.target.value)} placeholder="Enter Customer ID" />
                            <Button icon="pi pi-check" className="p-button-sm" disabled={!customerId} onClick={fetchCustomerDetails} />
                        </div>
                        {custError && <small className="text-danger">{custError}</small>}

                        {customerInfo && (
                            <div className="mt-2 text-muted">
                                <div><strong>Name:</strong> {customerInfo.firstName} {customerInfo.lastName}</div>
                                <div><strong>Email:</strong> {customerInfo.email}</div>
                                <div><strong>Phone:</strong> {customerInfo.phoneNumber}</div>
                                <div><strong>DOB:</strong> {customerInfo.dateOfBirth}</div>
                            </div>
                        )}
                    </div>

                    <div className="col-md-6">
                        <label className="form-label fw-bold">Account Type</label>
                        <Dropdown value={selectedType} options={accountTypes} onChange={(e) => setSelectedType(e.value)}
                            optionLabel="type" placeholder="Select Account Type" className="w-100" />
                        {selectedType && (
                            <small className="text-muted">Initial Deposit: ₹{selectedType.initialDeposit}</small>
                        )}
                    </div>

                    <div className="col-md-6">
                        <label className="form-label fw-bold">PAN Number</label>
                        <input type="text" className="form-control" value={pan}
                            onChange={(e) => setPan(e.target.value)} placeholder="Enter PAN number" />
                    </div>

                    <div className="col-md-6">
                        <label className="form-label fw-bold">Aadhar Number</label>
                        <input type="text" className="form-control" value={aadhar}
                            onChange={(e) => setAadhar(e.target.value)} placeholder="Enter Aadhar number" />
                    </div>
                </div>

                <div className="text-end mt-4">
                    <Button label="Create Account" icon="pi pi-check" onClick={createAccount}
                        disabled={!customerId || !selectedType || !pan || !aadhar} />
                </div>
            </div>
        </div>
    )
}

export default AccountCreation
