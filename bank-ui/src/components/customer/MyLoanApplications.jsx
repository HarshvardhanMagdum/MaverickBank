import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { Button } from 'primereact/button';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function MyLoanApplications() {
    const [applications, setApplications] = useState([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [selectedApplication, setSelectedApplication] = useState(null);
    const [dialogVisible, setDialogVisible] = useState(false);

    const navigate = useNavigate();
    const breadcrumbItems = [
        { label: 'Loan', command: () => navigate('/customer/loan') },
        { label: 'Applications' }
    ];
    const home = { icon: 'pi pi-home', command: () => navigate("/customer") };

    useEffect(() => {
        fetchApplications();
    }, []);

    const fetchApplications = async () => {
        try {
            const res = await axios.get('http://localhost:8080/api/loanApply/get-one', {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            });
            setApplications(res.data);
        } catch (err) {
            console.error('Error fetching loan applications');
        }
    };

    const showDetails = (application) => {
        setSelectedApplication(application);
        setDialogVisible(true);
    };

    const cancelApplication = async (id) => {
        if (window.confirm('Are you sure you want to cancel this application?')) {
            try {
                await axios.put(`http://localhost:8080/api/loanApply/put/status/cancelled/${id}`, {}, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                });
                fetchApplications();
                alert('Loan application cancelled.');
            } catch {
                alert('Failed to cancel loan application.');
            }
        }
    };

    const viewColumnTemplate = (rowData) => (
        <div className="d-flex justify-content-center">
            <Button label="View" className="p-button-sm p-button-info w-100" onClick={() => showDetails(rowData)} />
        </div>
    );

    const actionColumnTemplate = (rowData) => (
        <div className="d-flex justify-content-center">
            <Button label="Cancel" className="p-button-sm p-button-danger w-100" disabled={rowData.status !== 'PENDING'}
                onClick={() => cancelApplication(rowData.id)} />
        </div>
    );

    const header = (
        <div className="d-flex justify-content-end align-items-center">
            <InputText value={globalFilter} onChange={(e) => setGlobalFilter(e.target.value)} placeholder="Search..." />
        </div>
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card p-4 shadow mt-3">
                <h3 className="text-center fw-bold mb-4 Application-title">My Loan Applications</h3>

                {applications.length === 0 ? (
                    <div className="alert alert-info">You have not applied for any loan.</div>
                ) : (
                    <DataTable value={applications} paginator rows={5} header={header} globalFilter={globalFilter} className="p-datatable-sm" >
                        <Column field="id" header="Application ID" style={{ textAlign: 'center' }} 
                            headerStyle={{ textAlign: 'center' }} className="text-center" />
                        <Column field="loanDetails.loanType" header="Loan Type" style={{ textAlign: 'center' }} 
                            headerStyle={{ textAlign: 'center' }} className="text-center" />
                        <Column header="View Details" body={viewColumnTemplate} style={{ textAlign: 'center' }}
                            headerStyle={{ textAlign: 'center' }} className="text-center" />
                        <Column field="status" header="Status" sortable style={{ textAlign: 'center' }}
                            headerStyle={{ textAlign: 'center' }} className="text-center" />
                        <Column  header="Action" body={actionColumnTemplate} style={{ textAlign: 'center' }}
                            headerStyle={{ textAlign: 'center' }} className="text-center" />
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
                        </div>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default MyLoanApplications;
