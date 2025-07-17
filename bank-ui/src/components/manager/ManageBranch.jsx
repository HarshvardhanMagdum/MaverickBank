import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

function ManageBranch() {
    const navigate = useNavigate();
    const { id } = useParams(); // branchId
    const [branch, setBranch] = useState({});
    const [executives, setExecutives] = useState([]);
    const [showEditDialog, setShowEditDialog] = useState(false);
    const [showAddDialog, setShowAddDialog] = useState(false);
    const [editForm, setEditForm] = useState({});
    const [newExecutive, setNewExecutive] = useState({
        firstName: "", lastName: "", email: "", phoneNumber: "",
        address: "", gender: "", dateOfBirth: "",
        username: "", password: ""
    });

    const home = { icon: 'pi pi-home', command: () => navigate('/manager') };
    const breadcrumbItems = [
        { label: 'Branch', command: () => navigate('/manager/branch') },
        { label: 'Manage Branch' }
    ];

    useEffect(() => {
        fetchBranch();
        fetchExecutives();
    }, [id]);

    const fetchBranch = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/branch/get-one/${id}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setBranch(res.data);
            setEditForm(res.data);
        } catch (err) {
            console.error("Failed to fetch branch data", err);
        }
    };

    const fetchExecutives = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/executive/get/${id}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            const activeExecs = res.data.filter(e => e.user.userStatus === "ACTIVE");
            setExecutives(activeExecs);
        } catch (err) {
            console.error("Failed to fetch executives", err);
        }
    };

    const updateBranch = async () => {
        try {
            await axios.put(`http://localhost:8080/api/branch/put/${id}`, editForm, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setShowEditDialog(false);
            fetchBranch();
        } catch (err) {
            alert("Failed to update branch.");
        }
    };

    const addExecutive = async () => {
        try {
            await axios.post(`http://localhost:8080/api/executive/post/${id}`, newExecutive, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setShowAddDialog(false);
            fetchExecutives();
        } catch (err) {
            alert("Failed to add executive.");
        }
    };

    return (
        <div className="container mt-4">
            <BreadCrumb model={breadcrumbItems} home={home} />

            <div className="card mt-4 p-4 shadow">
                <h2 className="text-center mb-4 fw-bold main-title">{branch.branchName}</h2>

                <div className="row text-center mb-4">
                    <div className="col-md-4"><strong>Email:</strong><br />{branch.email}</div>
                    <div className="col-md-4"><strong>Contact:</strong><br />{branch.phoneNumber}</div>
                    <div className="col-md-4"><strong>Address:</strong><br />{branch.address}</div>
                </div>

                <div className="text-center mb-3">
                    <Button label="Edit Branch Details" icon="pi pi-pencil" onClick={() => setShowEditDialog(true)} />
                </div>

                <h5 className="fw-bold mt-4">Branch Executives:</h5>
                <DataTable value={executives} className="p-datatable-sm" paginator rows={5}>
                    <Column field="id" header="Executive ID" style={{ textAlign: "center" }} />
                    <Column header="Name" body={e => `${e.firstName} ${e.lastName}`} style={{ textAlign: "center" }}/>
                    <Column field="email" header="Email" style={{ textAlign: "center" }}/>
                    <Column field="phoneNumber" header="Phone" style={{ textAlign: "center" }}/>
                </DataTable>
                <div className="text-end mb-2 mt-1">
                    <Button label="Add Executive" icon="pi pi-user-plus" onClick={() => setShowAddDialog(true)} />
                </div>
            </div>

            {/* Edit Branch Dialog */}
            <Dialog header="Edit Branch" visible={showEditDialog} onHide={() => setShowEditDialog(false)} style={{ width: '30vw' }}>
                <div className="p-fluid">
                    <label>Branch Name</label>
                    <InputText value={editForm.branchName} onChange={e => setEditForm({ ...editForm, branchName: e.target.value })} />
                    <label className="mt-2">IFSC Code</label>
                    <InputText value={editForm.ifscCode} onChange={e => setEditForm({ ...editForm, ifscCode: e.target.value })} />
                    <label className="mt-2">Email</label>
                    <InputText value={editForm.email} onChange={e => setEditForm({ ...editForm, email: e.target.value })} />
                    <label className="mt-2">Phone</label>
                    <InputText value={editForm.phoneNumber} onChange={e => setEditForm({ ...editForm, phoneNumber: e.target.value })} />
                    <label className="mt-2">Address</label>
                    <InputText value={editForm.address} onChange={e => setEditForm({ ...editForm, address: e.target.value })} />
                    <div className="text-end mt-3">
                        <Button label="Update" icon="pi pi-check" onClick={updateBranch} />
                    </div>
                </div>
            </Dialog>

            {/* Add Executive Dialog */}
            <Dialog header="Add Executive" visible={showAddDialog} onHide={() => setShowAddDialog(false)} style={{ width: '30vw' }}>
                <div className="p-fluid">
                    {["firstName", "lastName", "email", "phoneNumber", "address", "gender", "dateOfBirth", "username", "password"].map((field, idx) => (
                        <div className="mb-2" key={idx}>
                            <label>{field.charAt(0).toUpperCase() + field.slice(1)}</label>
                            <InputText
                                type={field === "dateOfBirth" ? "date" : "text"}
                                value={newExecutive[field]}
                                onChange={e => setNewExecutive({ ...newExecutive, [field]: e.target.value })}
                            />
                        </div>
                    ))}
                    <div className="text-end mt-2">
                        <Button label="Add" icon="pi pi-plus" onClick={addExecutive} />
                    </div>
                </div>
            </Dialog>
        </div>
    );
}

export default ManageBranch;
