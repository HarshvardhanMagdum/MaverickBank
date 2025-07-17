import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Accounts() {
    const navigate = useNavigate();

    const [accounts, setAccounts] = useState([]);
    const [filter, setFilter] = useState('');
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [showAccountDialog, setShowAccountDialog] = useState(false);
    const [showCustomerDialog, setShowCustomerDialog] = useState(false);

    const breadcrumbItems = [{ label: 'Accounts' }];
    const home = { icon: 'pi pi-home', command: () => navigate('/manager') };

    useEffect(() => {
        const fetchAccounts = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/account/get-all`, {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem('token')
                    }
                });
                const filtered = res.data.filter(acc =>
                    acc.status !== 'REJECTED' && acc.status !== 'CLOSED'
                );
                setAccounts(filtered);
            } catch (err) {
                console.error('Failed to load accounts', err);
            }
        };
        fetchAccounts();
    }, []);

    const filteredAccounts = accounts.filter(acc =>
        `${acc.id} ${acc.accountType.type} ${acc.status} ${acc.branch.branchName}`
            .toLowerCase()
            .includes(filter.toLowerCase())
    );

    const header = (
        <div className="d-flex justify-content-end">
            <InputText placeholder="Search accounts..." value={filter} onChange={e => setFilter(e.target.value)} />
        </div>
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-3">
                <h2 className="text-center fw-bold fs-3 mb-4 main-title">All Accounts</h2>

                <DataTable
                    value={filteredAccounts}
                    paginator rows={5}
                    header={header}
                    className="p-datatable-sm table-bordered"
                >
                    <Column field="id" header="Account ID" style={{ textAlign: 'center' }}/>
                    <Column field="branch.branchName" header="Branch" sortable style={{ textAlign: 'center' }}/>
                    <Column field="accountType.type" header="Account Type" sortable style={{ textAlign: 'center' }}/>
                    <Column field="status" header="Status" sortable style={{ textAlign: 'center' }}/>
                    <Column
                        header="Account Details"
                        body={row => (
                            <Button label="View" className="p-button-text p-button-primary" onClick={() => {
                                setSelectedAccount(row);
                                setShowAccountDialog(true);
                            }} />
                        )}
                        style={{ textAlign: 'center' }}
                    />
                    <Column field="customer.firstName" header="Customer Name" style={{ textAlign: 'center' }}/>
                    <Column
                        header="Customer Details"
                        body={row => (
                            <Button label="View" className="p-button-text p-button-info" onClick={() => {
                                setSelectedAccount(row);
                                setShowCustomerDialog(true);
                            }} />
                        )}
                        style={{ textAlign: 'center' }}
                    />
                </DataTable>

                {/* Account Dialog */}
                <Dialog header="Account Details" visible={showAccountDialog} onHide={() => setShowAccountDialog(false)} style={{ width: '30vw' }}>
                    {selectedAccount && (
                        <div>
                            <p><strong>Account ID:</strong> {selectedAccount.id}</p>
                            <p><strong>Account Type:</strong> {selectedAccount.accountType.type}</p>
                            <p><strong>Branch:</strong> {selectedAccount.branch.branchName}</p>
                            <p><strong>Balance:</strong> ₹{selectedAccount.balance}</p>
                            <p><strong>Open Date:</strong> {selectedAccount.openDate}</p>
                            <p><strong>Status:</strong> {selectedAccount.status}</p>
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
                            <p><strong>Email:</strong> {selectedAccount.customer.email}</p>
                            <p><strong>Phone:</strong> {selectedAccount.customer.phoneNumber}</p>
                            <p><strong>DOB:</strong> {selectedAccount.customer.dateOfBirth}</p>
                        </div>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default Accounts;
