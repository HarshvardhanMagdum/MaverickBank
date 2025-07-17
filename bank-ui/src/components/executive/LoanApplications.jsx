import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function LoanApplications() {
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [globalFilter, setGlobalFilter] = useState("");
    const [selectedApplication, setSelectedApplication] = useState(null);
    const [dialogVisible, setDialogVisible] = useState(false);

    const branchId = localStorage.getItem("branchId")

    const breadcrumbItems = [
        { label: "Loans", command: () => navigate("/executive/loans") },
        { label: "Applications" },
    ];
    const home = { icon: "pi pi-home", command: () => navigate("/executive") };

    useEffect(() => {
        fetchApplications();
    }, []);

    const fetchApplications = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/loanApply/get-by/branchId/${branchId}`, {
                headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
            });
            setApplications(res.data);
        } catch (err) {
            console.error("Error fetching loan applications.");
        }
    };

    const handleStatusChange = async (id, status) => {
        try {
            await axios.put(`http://localhost:8080/api/loanApply/put/status/${id}?status=${status}`,null,{
                headers: { "Authorization": "Bearer " + localStorage.getItem("token")},
            });
            fetchApplications()
        } catch {
            alert("Failed to update status.");
        }
    };

    const viewDetails = (application) => {
        setSelectedApplication(application);
        setDialogVisible(true);
    };

    const viewColumnTemplate = (rowData) => (
        <div className="d-flex justify-content-center">
            <button className="btn btn-info btn-sm w-100" onClick={() => viewDetails(rowData)}>View</button>
        </div>
    );

    const actionColumnTemplate = (rowData) => {
        const statusOptions = [
            { label: "APPROVED", value: "APPROVED" },
            { label: "REJECTED", value: "REJECTED" }
        ];

        return (
            <div className="d-flex justify-content-center">
                <Dropdown
                    value={null}
                    options={statusOptions}
                    onChange={(e) => handleStatusChange(rowData.id, e.value)}
                    placeholder="Update Status"
                    disabled={["APPROVED", "CANCELLED"].includes(rowData.status)}
                    className="w-100"
                />
            </div>
        );
    };

    const header = (
        <div className="d-flex justify-content-end align-items-center">
            <InputText
                value={globalFilter}
                onChange={(e) => setGlobalFilter(e.target.value)}
                placeholder="Search..."
            />
        </div>
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-3">
                <h3 className="text-center fw-bold mb-4">Loan Applications</h3>

                {applications.length === 0 ? (
                    <div className="alert alert-info">No loan applications submitted.</div>
                ) : (
                    <DataTable
                        value={applications}
                        paginator
                        rows={5}
                        globalFilter={globalFilter}
                        header={header}
                        className="p-datatable-sm"
                    >
                        <Column field="id" header="Application ID" className="text-center" />
                        <Column header="View Details" body={viewColumnTemplate} className="text-center" />
                        <Column field="status" header="Status" className="text-center" />
                        <Column header="Action" body={actionColumnTemplate} className="text-center" />
                    </DataTable>
                )}

                <Dialog header="Loan Application Details" visible={dialogVisible} onHide={() => setDialogVisible(false)} style={{ width: '30vw' }}>
                    {selectedApplication && (
                        <div className="p-fluid">
                            <p><strong>Loan Type:</strong> {selectedApplication.loanDetails.loanType}</p>
                            <p><strong>Principal:</strong> ₹{selectedApplication.loanDetails.principalAmount}</p>
                            <p><strong>Interest Rate:</strong> {selectedApplication.loanDetails.interestRate}%</p>
                            <p><strong>Term:</strong> {selectedApplication.loanDetails.termInMonth} months</p>
                            <p><strong>Total Repayable:</strong> ₹{selectedApplication.loanDetails.totalRepayableAmount}</p>
                            <p><strong>EMI:</strong> ₹{selectedApplication.loanDetails.emiAmount}</p>
                            <p><strong>Account ID:</strong> {selectedApplication.account.id}</p>
                            <p><strong>Status:</strong> {selectedApplication.status}</p>
                            <hr className="my-3" />
                            <h5 className="mb-3 fw-bold text-success">Customer Details</h5>
                            <p><strong>Name:</strong> {selectedApplication.account.customer.firstName} {selectedApplication.account.customer.lastName}</p>
                            <p><strong>Email:</strong> {selectedApplication.account.customer.email}</p>
                            <p><strong>Phone:</strong> {selectedApplication.account.customer.phoneNumber}</p>
                            <p><strong>DOB:</strong> {selectedApplication.account.customer.dateOfBirth}</p>
                        </div>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default LoanApplications;
