import React, { useEffect, useState } from "react"
import { BreadCrumb } from "primereact/breadcrumb"
import { Dropdown } from "primereact/dropdown"
import { Button } from "primereact/button"
import { useNavigate } from "react-router-dom"
import axios from "axios";

function CreateAccount() {
    const navigate = useNavigate()
    const [accountTypes, setAccountTypes] = useState([])
    const [branches, setBranches] = useState([])
    const [selectedType, setSelectedType] = useState(null)
    const [selectedBranch, setSelectedBranch] = useState(null)
    const [pan, setPan] = useState("")
    const [aadhar, setAadhar] = useState("")
    const [msg, setMsg] = useState("")

    const breadcrumbItems = [
        { label: "My Account", command: () => navigate("/customer") },
        { label: "Create Account", command: () => navigate("/customer/account/create") }
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/customer") }

    useEffect(() => {
        const fetchData = async () => {
            try {
                const typeRes = await axios.get("http://localhost:8080/api/accountType/get-all")
                setAccountTypes(typeRes.data)
                const branchRes = await axios.get("http://localhost:8080/api/branch/get-all")
                setBranches(branchRes.data)
            } catch (err) {
                console.error("Failed to fetch types or branches", err)
            }
        }

        fetchData()
    }, [])

    const postAccount = async () => {
        try {
            await axios.post(
                `http://localhost:8080/api/account/post/?ifscCode=${selectedBranch.ifscCode}&type=${selectedType.type}`,
                {
                    panNumber: pan,
                    aadharNumber: aadhar
                },
                {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                }
            )
            setMsg("Account created successfully!");
            setTimeout(() => navigate("/customer"), 2500)
        } catch (err) {
            setMsg("Failed to create account.");
            console.error(err)
        }
    };

    return (
        <div className="container mt-4">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card mt-4 ">
                <div className="card-body">
                    <h2 className="mt-4 fw-bold text-center mb-4 createAcc-title">Create Account</h2>

                    {msg!=""? <div className="alert alert-info">{msg}</div> : ""}

                    <div className="row g-3 mt-3">
                        <div className="col-md-6">
                            <label className="form-label fw-bold">Account Type</label>
                            <Dropdown
                                value={selectedType}
                                options={accountTypes}
                                onChange={(e) => setSelectedType(e.value)}
                                optionLabel="type"
                                placeholder="Select Account Type"
                                className="w-100"
                            />
                            {selectedType && (
                                <small className="text-muted">Initial Deposit: ₹{selectedType.initialDeposit}</small>
                            )}
                        </div>

                        <div className="col-md-6">
                            <label className="form-label fw-bold">Select Branch</label>
                            <Dropdown
                                value={selectedBranch}
                                options={branches}
                                onChange={(e) => setSelectedBranch(e.value)}
                                optionLabel={(branch) => `${branch.branchName} (${branch.ifscCode})`}
                                placeholder="Select Branch"
                                className="w-100"
                            />
                            {selectedBranch && (
                                <div className="mt-1 text-muted">
                                    <div>Address: {selectedBranch.address}</div>
                                    <div>Email: {selectedBranch.email}</div>
                                    <div>Phone: {selectedBranch.phoneNumber}</div>
                                </div>
                            )}
                        </div>

                        <div className="col-md-6">
                            <label className="form-label">PAN Number</label>
                            <input type="text" className="form-control" value={pan}
                                onChange={(e) => setPan(e.target.value)}/>
                        </div>

                        <div className="col-md-6">
                            <label className="form-label">Aadhar Number</label>
                            <input type="text" className="form-control" value={aadhar} 
                                onChange={(e) => setAadhar(e.target.value)} />
                        </div>
                    </div>

                    <div className="text-end mt-4">
                        <Button label="Create Account" icon="pi pi-check" onClick={postAccount}
                            disabled={!selectedType || !selectedBranch || !pan || !aadhar} />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreateAccount;
