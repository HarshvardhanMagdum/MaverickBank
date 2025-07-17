import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Card } from "primereact/card";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AccountTypes() {
    const navigate = useNavigate();
    const [accountTypes, setAccountTypes] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [visibleDialog, setVisibleDialog] = useState(false);
    const [formData, setFormData] = useState({ id: null, type: "", initialDeposit: "" });
    const [isEdit, setIsEdit] = useState(false);

    const breadcrumbItems = [
        { label: "Accounts", command: () => navigate("/executive/accounts") },
        { label: "Manage Account Types" }
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/executive") };

    useEffect(() => {
        loadAccountTypes();
    }, []);

    const loadAccountTypes = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/accountType/get-all",{
                headers: { 'Authorization' : `Bearer ${localStorage.getItem("token")}` }
            });
            setAccountTypes(res.data);
        } catch (err) {
            console.error("Failed to load account types", err);
        }
    };

    const openNewDialog = () => {
        setFormData({ id: null, type: "", initialDeposit: "" });
        setIsEdit(false);
        setVisibleDialog(true);
    };

    const openEditDialog = (data) => {
        setFormData({ id: data.id, type: data.type, initialDeposit: data.initialDeposit });
        setIsEdit(true);
        setVisibleDialog(true);
    };

    const saveAccountType = async () => {
        try {
            if (isEdit) {
                await axios.put(`http://localhost:8080/api/accountType/put/${formData.id}`, {
                    type: formData.type,
                    initialDeposit: formData.initialDeposit
                },{
                    headers: { 'Authorization' : `Bearer ${localStorage.getItem("token")}` }
                });
            } else {
                await axios.post(`http://localhost:8080/api/accountType/post`, {
                    type: formData.type,
                    initialDeposit: formData.initialDeposit
                },{
                    headers: { 'Authorization' : `Bearer ${localStorage.getItem("token")}` }
                });
            }
            setVisibleDialog(false);
            loadAccountTypes();
        } catch (err) {
            alert("Failed to save account type.");
        }
    };

    const filteredTypes = accountTypes.filter(acc =>
        acc.type.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="container mt-3">
            <BreadCrumb model={breadcrumbItems} home={home} />

            <div className="card shadow p-4 mt-3">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h2 className="fw-bold main-title">Manage Account Types</h2>
                    <Button label="Add New" icon="pi pi-plus" className="p-button-sm" onClick={openNewDialog} />
                </div>

                <div className="mb-3">
                    <InputText className="form-control" placeholder="Search by type..."
                        value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
                </div>

                <div className="row">
                    {filteredTypes.map((accType) => (
                        <div className="col-md-4 mb-4" key={accType.id}>
                            <Card title={`Type CODE: ${accType.id}`} className="h-100 text-center loan-card">
                                <p><strong>Type:</strong> {accType.type}</p>
                                <p><strong>Initial Deposit:</strong> ₹{accType.initialDeposit}</p>
                                <Button label="Edit" icon="pi pi-pencil"
                                    className="p-button-sm" onClick={() => openEditDialog(accType)} />
                            </Card>
                        </div>
                    ))}
                </div>
            </div>

            {/* Dialog for Add/Edit */}
            <Dialog
                header={isEdit ? "Edit Account Type" : "Add Account Type"}
                visible={visibleDialog}
                onHide={() => setVisibleDialog(false)}
                style={{ width: "30vw" }}
                className="p-fluid"
            >
                <div className="mb-3">
                    <label className="form-label">Type</label>
                    <InputText className="form-control" value={formData.type}
                        onChange={(e) => setFormData({ ...formData, type: e.target.value })} />
                </div>
                <div className="mb-3">
                    <label className="form-label">Initial Deposit</label>
                    <InputText className="form-control" type="number" value={formData.initialDeposit}
                        onChange={(e) => setFormData({ ...formData, initialDeposit: e.target.value })} />
                </div>
                <Button label="Save" className="mt-2" onClick={saveAccountType} />
            </Dialog>
        </div>
    );
}

export default AccountTypes;
