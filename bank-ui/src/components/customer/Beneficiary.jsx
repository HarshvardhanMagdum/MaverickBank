import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

function Beneficiary() {
    const navigate = useNavigate();
    const [beneficiaries, setBeneficiaries] = useState([]);
    const [showDialog, setShowDialog] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [selectedBeneficiary, setSelectedBeneficiary] = useState(null);
    const [form, setForm] = useState({
        name: "", accountNumber: "", ifscCode: "", branchName: "", description: ""
    });

    const breadcrumbItems = [{ label: 'Beneficiary' }];
    const home = { icon: 'pi pi-home', command: () => navigate('/customer') };

    const fetchBeneficiaries = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/beneficiary/get", {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setBeneficiaries(res.data);
        } catch {
            alert("Failed to fetch beneficiaries.");
        }
    };

    useEffect(() => {
        fetchBeneficiaries();
    }, []);

    const openAddDialog = () => {
        setForm({ name: "", accountNumber: "", ifscCode: "", branchName: "", description: "" });
        setIsEdit(false);
        setShowDialog(true);
    };

    const openEditDialog = (beneficiary) => {
        setSelectedBeneficiary(beneficiary);
        setForm({ ...beneficiary });
        setIsEdit(true);
        setShowDialog(true);
    };

    const handleSubmit = async () => {
        try {
            if (isEdit) {
                await axios.put(`http://localhost:8080/api/beneficiary/put/${selectedBeneficiary.id}/${selectedBeneficiary.customer.id}`, form, {
                    headers: { Authorization: "Bearer " + localStorage.getItem("token") }
                });
                alert("Beneficiary updated");
            } else {
                await axios.post(`http://localhost:8080/api/beneficiary/post`, form, {
                    headers: { Authorization: "Bearer " + localStorage.getItem("token") }
                });
                alert("Beneficiary added");
            }
            setShowDialog(false);
            fetchBeneficiaries();
        } catch {
            alert("Error saving beneficiary.");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this beneficiary?")) {
            try {
                await axios.delete(`http://localhost:8080/api/beneficiary/delete/${id}`, {
                    headers: { Authorization: "Bearer " + localStorage.getItem("token") }
                });
                alert("Deleted successfully.");
                fetchBeneficiaries();
            } catch {
                alert("Error deleting.");
            }
        }
    };

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card p-4 shadow mt-2">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h4 className="fw-bold beneficiary-title">My Beneficiaries: </h4>
                    <Button label="Add Beneficiary" icon="pi pi-user-plus" onClick={openAddDialog} />
                </div>

                {beneficiaries.length === 0 ? (
                    <div className="alert alert-info">No beneficiaries added yet.</div>
                ) : (
                    <DataTable value={beneficiaries} paginator rows={5} className="mt-3">
                        <Column field="name" header="Name" style={{ textAlign: 'center' }} />
                        <Column field="accountNumber" header="Account Number" style={{ textAlign: 'center' }} />
                        <Column field="ifscCode" header="IFSC" style={{ textAlign: 'center' }} />
                        <Column field="branchName" header="Branch" style={{ textAlign: 'center' }} />
                        <Column field="description" header="Description" style={{ textAlign: 'center' }} />
                        <Column
                            header="Actions"
                            body={(rowData) => (
                                <>
                                    <Button icon="pi pi-pencil" className="p-button-text p-button-sm me-2" onClick={() => openEditDialog(rowData)} />
                                    <Button icon="pi pi-trash" className="p-button-text p-button-danger p-button-sm" onClick={() => handleDelete(rowData.id)} />
                                </>
                            )}
                        />
                    </DataTable>
                )}

                <Dialog header={isEdit ? "Edit Beneficiary" : "Add Beneficiary"} visible={showDialog} onHide={() => setShowDialog(false)} style={{ width: '30vw' }} modal>
                    <div className="p-fluid">
                        <div className="mb-2">
                            <label>Name</label>
                            <input className="form-control" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
                        </div>
                        <div className="mb-2">
                            <label>Account Number</label>
                            <input className="form-control" value={form.accountNumber} onChange={(e) => setForm({ ...form, accountNumber: e.target.value })} />
                        </div>
                        <div className="mb-2">
                            <label>IFSC Code</label>
                            <input className="form-control" value={form.ifscCode} onChange={(e) => setForm({ ...form, ifscCode: e.target.value })} />
                        </div>
                        <div className="mb-2">
                            <label>Branch Name</label>
                            <input className="form-control" value={form.branchName} onChange={(e) => setForm({ ...form, branchName: e.target.value })} />
                        </div>
                        <div className="mb-2">
                            <label>Description</label>
                            <textarea className="form-control" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
                        </div>
                        <Button label={isEdit ? "Update" : "Add"} onClick={handleSubmit} className="w-100 mt-2" />
                    </div>
                </Dialog>
            </div>
        </div>
    );
}

export default Beneficiary;
