import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Loans() {
    const [loans, setLoans] = useState([]);
    const [filter, setFilter] = useState("");
    const [selectedLoan, setSelectedLoan] = useState(null);
    const [repayments, setRepayments] = useState([]);
    const [showLoanDialog, setShowLoanDialog] = useState(false);
    const [showCustomerDialog, setShowCustomerDialog] = useState(false);
    const [showRepaymentsDialog, setShowRepaymentsDialog] = useState(false);

    const navigate = useNavigate();
    const home = { icon: 'pi pi-home', command: () => navigate('/manager') };
    const breadcrumbItems = [{ label: "Loans" }];

    useEffect(() => {
    const fetchAllLoans = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/loan/get-all", {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setLoans(res.data);
        } catch (err) {
            console.error("Error loading loans", err);
        }
    };

    fetchAllLoans();
}, []);


    const fetchRepayments = async (loanId) => {
        try {
            const res = await axios.get(`http://localhost:8080/api/loanRepay/get-by/loanId/${loanId}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setRepayments(res.data);
            setShowRepaymentsDialog(true);
        } catch (err) {
            console.error("Error fetching repayments", err);
        }
    };

    const filteredLoans = loans.filter((loan) =>
        `${loan.id} ${loan.loanApplication?.loanDetails?.loanType} ${loan.loanApplication?.customer?.firstName}`
            .toLowerCase()
            .includes(filter.toLowerCase())
    );

    const header = (
        <div className="d-flex justify-content-end mb-2">
            <InputText placeholder="Search loans..." value={filter} onChange={(e) => setFilter(e.target.value)} />
        </div>
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card p-4 shadow mt-3">
                <h2 className="text-center fw-bold fs-3 mb-4 main-title">All Loans</h2>

                <DataTable value={filteredLoans} paginator rows={6} header={header} className="p-datatable-sm">
                    <Column field="id" header="Loan ID" style={{ textAlign: 'center' }} />
                    <Column
                        field="loanApplication.loanDetails.loanType"
                        header="Loan Type"
                        style={{ textAlign: 'center' }}
                    />
                    <Column
                        field="status"
                        header="Loan Type"
                        style={{ textAlign: 'center' }}
                    />
                    <Column
                        header="Loan Details"
                        body={(row) => (
                            <Button label="View" className="p-button-text p-button-primary" onClick={() => {
                                setSelectedLoan(row);
                                setShowLoanDialog(true);
                            }} />
                        )}
                        style={{ textAlign: 'center' }}
                    />
                    <Column
                        field="loanApplication.customer.firstName"
                        header="Customer Name"
                        body={(row) => (
                            `${row.loanApplication.account.customer.firstName}`
                        )}
                        style={{ textAlign: 'center' }}
                    />
                    <Column
                        header="Customer Details"
                        body={(row) => (
                            <Button label="View" className="p-button-text p-button-info" onClick={() => {
                                setSelectedLoan(row);
                                setShowCustomerDialog(true);
                            }} />
                        )}
                        style={{ textAlign: 'center' }}
                    />
                    <Column
                        header="View Payments"
                        body={(row) => (
                            <Button label="View" className="p-button-text p-button-warning" onClick={() => {
                                fetchRepayments(row.id);
                            }} />
                        )}
                        style={{ textAlign: 'center' }}
                    />
                </DataTable>

                {/* Loan Details Dialog */}
                <Dialog header="Loan Details" visible={showLoanDialog} onHide={() => setShowLoanDialog(false)} style={{ width: '30vw' }}>
                    {selectedLoan && (
                        <div>
                            <p><strong>Loan ID:</strong> {selectedLoan.id}</p>
                            <p><strong>Status:</strong> {selectedLoan.status}</p>
                            <p><strong>Balance:</strong> ₹{selectedLoan.balanceAmount}</p>
                            <p><strong>Start Date:</strong> {new Date(selectedLoan.startDate).toLocaleDateString()}</p>
                            <p><strong>End Date:</strong> {new Date(selectedLoan.endDate).toLocaleDateString()}</p>
                            <hr />
                            <p><strong>Loan Type:</strong> {selectedLoan.loanApplication.loanDetails.loanType}</p>
                            <p><strong>Principal:</strong> ₹{selectedLoan.loanApplication.loanDetails.principalAmount}</p>
                            <p><strong>Interest Rate:</strong> {selectedLoan.loanApplication.loanDetails.interestRate}%</p>
                            <p><strong>Term:</strong> {selectedLoan.loanApplication.loanDetails.termInMonth} months</p>
                            <p><strong>Total Repayable:</strong> ₹{selectedLoan.loanApplication.loanDetails.totalRepayableAmount}</p>
                            <p><strong>EMI:</strong> ₹{selectedLoan.loanApplication.loanDetails.emiAmount}</p>
                        </div>
                    )}
                </Dialog>

                {/* Customer Details Dialog */}
                <Dialog header="Customer Details" visible={showCustomerDialog} onHide={() => setShowCustomerDialog(false)} style={{ width: '30vw' }}>
                    {selectedLoan && (
                        <div>
                            <p><strong>Customer ID:</strong> {selectedLoan.loanApplication.account.customer.id}</p>
                            <p><strong>Name:</strong> {selectedLoan.loanApplication.account.customer.firstName} {selectedLoan.loanApplication.account.customer.lastName}</p>
                            <p><strong>Email:</strong> {selectedLoan.loanApplication.account.customer.email}</p>
                            <p><strong>Phone:</strong> {selectedLoan.loanApplication.account.customer.phoneNumber}</p>
                            <p><strong>DOB:</strong> {selectedLoan.loanApplication.account.customer.dateOfBirth}</p>
                        </div>
                    )}
                </Dialog>

                {/* Repayments Dialog */}
                <Dialog header="Loan Repayments" visible={showRepaymentsDialog} onHide={() => setShowRepaymentsDialog(false)} style={{ width: '25vw' }}>
                    {repayments.length > 0 ? (
                        <ul className="list-group">
                            {repayments.map((repay, idx) => (
                                <li key={idx} className="list-group-item d-flex justify-content-between">
                                    <span>₹{repay.repaymentAmount}</span>
                                    <span>{new Date(repay.repaymentDate).toLocaleDateString()}</span>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>No repayments found.</p>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default Loans;
