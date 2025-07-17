import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Branch() {
    const [branches, setBranches] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [showDialog, setShowDialog] = useState(false);
    const [form, setForm] = useState({
        branchName: "",
        ifscCode: "",
        address: "",
        email: "",
        phoneNumber: ""
    });
    const [msg, setMsg] = useState("");

    const navigate = useNavigate();

    const breadcrumbItems = [{ label: "Branch" }];
    const home = { icon: "pi pi-home", command: () => navigate("/manager") };

    useEffect(() => {
        fetchBranches();
    }, []);

    const fetchBranches = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/branch/get-all",{
                headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
            });
            setBranches(res.data);
        } catch (err) {
            console.error("Failed to fetch branches", err);
        }
    };

    const handleAddBranch = async () => {
        try {
            await axios.post("http://localhost:8080/api/branch/post", form,{
                headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
            });
            setMsg("Branch added successfully!");
            setShowDialog(false);
            fetchBranches();
            setForm({ branchName: "", ifscCode: "", address: "", email: "", phoneNumber: "" });
        } catch (err) {
            setMsg("Failed to add branch");
            console.error(err);
        }
    };

    const filtered = branches.filter(b =>
        `${b.branchName} ${b.ifscCode}`.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-2  manage-scroll-container">
                <h2 className="text-center fw-bold mb-4 main-title">Branch</h2>

                <div className="d-flex justify-content-between align-items-center mb-4">
                    <Button
                        label="Add Branch"
                        icon="pi pi-plus"
                        className="p-button-sm"
                        onClick={() => setShowDialog(true)}
                    />
                    <InputText
                        placeholder="Search branches..."
                        className="form-control w-25"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="row">
                    {filtered.map((branch) => (
                        <div className="col-md-6 col-lg-4 mb-3" key={branch.id}>
                            <Card title={branch.branchName} className="text-black loan-card">
                                <p><strong>IFSC:</strong> {branch.ifscCode}</p>
                                <div className="text-end">
                                    <Button
                                        label="Manage Branch"
                                        icon="pi pi-cog"
                        className="p-button-secondary p-button-rounded"
                                        onClick={() => navigate(`/manager/branch/manage/${branch.id}`)}
                                    />
                                </div>
                            </Card>
                        </div>
                    ))}
                </div>
            </div>

            {/* Add Branch Dialog */}
            <Dialog
                header="Add New Branch"
                visible={showDialog}
                onHide={() => setShowDialog(false)}
                style={{ width: '30vw' }}
            >
                {msg && <div className="alert alert-info">{msg}</div>}
                <div className="p-fluid">
                    <div className="mb-2">
                        <label>Branch Name</label>
                        <InputText value={form.branchName} onChange={(e) => setForm({ ...form, branchName: e.target.value })} className="w-100" />
                    </div>
                    <div className="mb-2">
                        <label>IFSC Code</label>
                        <InputText value={form.ifscCode} onChange={(e) => setForm({ ...form, ifscCode: e.target.value })} className="w-100" />
                    </div>
                    <div className="mb-2">
                        <label>Address</label>
                        <InputText value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} className="w-100" />
                    </div>
                    <div className="mb-2">
                        <label>Email</label>
                        <InputText value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} className="w-100" />
                    </div>
                    <div className="mb-2">
                        <label>Phone Number</label>
                        <InputText value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} className="w-100" />
                    </div>

                    <div className="text-end mt-3">
                        <Button label="Add" icon="pi pi-check" onClick={handleAddBranch} />
                    </div>
                </div>
            </Dialog>
        </div>
    );
}

export default Branch;
