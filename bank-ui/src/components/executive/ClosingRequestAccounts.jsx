import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function ClosingRequestAccounts() {
    const navigate = useNavigate();
    const branchId = localStorage.getItem('branchId');

    const [accounts, setAccounts] = useState([]);
    const [filter, setFilter] = useState('');
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [showAccountDialog, setShowAccountDialog] = useState(false);
    const [showCustomerDialog, setShowCustomerDialog] = useState(false);

    const breadcrumbItems = [
        { label: 'Accounts', command: () => navigate('/executive/accounts') },
        { label: 'Closing Requests' }
    ];
    const home = { icon: 'pi pi-home', command: () => navigate('/executive') };

    useEffect(() => {
        const fetchAccounts = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/account/get/${branchId}`,{
                    headers: { 'Authorization' : "Bearer " +localStorage.getItem("token")}
                });
                const filtered = res.data.filter(acc => acc.status === 'CLOSING_REQUESTED');
                setAccounts(filtered);
            } catch (err) {
                console.error('Failed to load accounts', err);
            }
        };
        fetchAccounts();
    }, [branchId]);

    const updateStatus = async (accountId, newStatus) => {
        try {
            await axios.put(`http://localhost:8080/api/account/put/status/${accountId}/?status=${newStatus}`,{
                headers: { 'Authorization' : "Bearer " +localStorage.getItem("token")}
            });
            const updated = accounts.filter(acc => acc.id !== accountId);
            setAccounts(updated);
        } catch (err) {
            alert('Failed to update status');
        }
    };

    const filteredAccounts = accounts.filter(acc =>
        `${acc.id} ${acc.accountType.type}`.toLowerCase().includes(filter.toLowerCase())
    );

    const header = (
        <div className="d-flex justify-content-end mb-2">
            <InputText placeholder="Search accounts..." value={filter} onChange={e => setFilter(e.target.value)} />
        </div>
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-3">
                <h2 className="text-center fw-bold fs-3 mb-4 main-title">Closing Account Requests</h2>

                <DataTable
                    value={filteredAccounts}
                    paginator rows={5}
                    header={header}
                    className="p-datatable-sm table-bordered"
                >
                    <Column field="id" header="Account ID" style={{ textAlign: 'center' }} />
                    <Column field="accountType.type" header="Account Type" sortable style={{ textAlign: 'center' }} />
                    <Column header="Account Details" body={row =>
                        <Button label="View" className="p-button-text p-button-primary" onClick={() => {
                            setSelectedAccount(row);
                            setShowAccountDialog(true);
                        }} />
                    } style={{ textAlign: 'center' }} />
                    <Column header="Customer Details" body={row =>
                        <Button label="View" className="p-button-text p-button-info" onClick={() => {
                            setSelectedAccount(row);
                            setShowCustomerDialog(true);
                        }} />
                    } style={{ textAlign: 'center' }} />
                    <Column header="Action" body={row =>
                        <div className="d-flex gap-2 justify-content-center">
                            <Button label="Approve" className="p-button-sm p-button-success" onClick={() => updateStatus(row.id, 'CLOSED')} />
                            <Button label="Reject" className="p-button-sm p-button-danger" onClick={() => updateStatus(row.id, 'ACTIVE')} />
                        </div>
                    } style={{ textAlign: 'center' }} />
                </DataTable>

                {/* Account Dialog */}
                <Dialog header="Account Details" visible={showAccountDialog} onHide={() => setShowAccountDialog(false)} style={{ width: '30vw' }}>
                    {selectedAccount && (
                        <div>
                            <p><strong>Account ID:</strong> {selectedAccount.id}</p>
                            <p><strong>Account Type:</strong> {selectedAccount.accountType.type}</p>
                            <p><strong>Balance:</strong> ₹{selectedAccount.balance}</p>
                            <p><strong>Open Date:</strong> {selectedAccount.openDate}</p>
                            <p><strong>PAN:</strong> {selectedAccount.panNumber}</p>
                            <p><strong>Aadhar:</strong> {selectedAccount.aadharNumber}</p>
                        </div>
                    )}
                </Dialog>

                {/* Customer Dialog */}
                <Dialog header="Customer Details" visible={showCustomerDialog} onHide={() => setShowCustomerDialog(false)} style={{ width: '30vw' }}>
                    {selectedAccount && selectedAccount.customer && (
                        <div>
                            <p><strong>Customer ID:</strong> {selectedAccount.customer.id}</p>
                            <p><strong>Name:</strong> {selectedAccount.customer.firstName} {selectedAccount.customer.lastName}</p>
                            <p><strong>Email:</strong> {selectedAccount.customer.user.email}</p>
                            <p><strong>Phone:</strong> {selectedAccount.customer.phoneNumber}</p>
                            <p><strong>DOB:</strong> {selectedAccount.customer.dob}</p>
                        </div>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default ClosingRequestAccounts;
