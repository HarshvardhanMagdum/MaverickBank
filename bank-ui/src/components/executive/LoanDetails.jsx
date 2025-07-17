import React, { useEffect, useState } from "react";
import { BreadCrumb } from "primereact/breadcrumb";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function ExecutiveLoanDetails() {
    const [loanList, setLoanList] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedLoan, setSelectedLoan] = useState(null);
    const [showDetails, setShowDetails] = useState(false);
    const [showAddEditDialog, setShowAddEditDialog] = useState(false);
    const [loanForm, setLoanForm] = useState({ loanType: "", principalAmount: "", interestRate: "", termInMonth: "" });
    const [loanTypes, setLoanTypes] = useState([]);

    const navigate = useNavigate()
    const home = { icon: 'pi pi-home', command: () => navigate('/executive') };
    const breadcrumbItems = [
        { label: 'Loans', command: () => navigate('/executive/loans') },
        { label: 'Loan Details' }
    ];

    useEffect(() => {
        fetchLoans();
        fetchLoanTypes();
    }, []);


    const fetchLoanTypes = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/enum/loan/type/get");
            setLoanTypes(res.data); // assuming res.data is an array like ["HOME", "CAR", "EDUCATION"]
        } catch (err) {
            console.error("Error fetching loan types", err);
        }
    };

    const fetchLoans = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/loanDetails/get/all");
            setLoanList(res.data);
        } catch (err) {
            console.log("Error fetching loan details", err);
        }
    };

    const handleSave = async () => {
        const payload = {
            loanType: loanForm.loanType,
            principalAmount: loanForm.principalAmount,
            interestRate: loanForm.interestRate,
            termInMonth: loanForm.termInMonth
        };

        try {
            if (loanForm.id) {
                await axios.put(`http://localhost:8080/api/loanDetails/put/${loanForm.id}`, payload, {
                    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
                });
            } else {
                await axios.post("http://localhost:8080/api/loanDetails/post", payload, {
                    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
                });
            }
            setShowAddEditDialog(false);
            fetchLoans();
        } catch (error) {
            console.log("Error saving loan", error);
        }
    };

    const filteredLoans = loanList.filter((loan) =>
        loan.loanType.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow mt-1 p-4">
                <h3 className="text-center fw-bold mb-2 Loan-title">Loan Details</h3>

                <div className="d-flex justify-content-between align-items-center mb-2">
                    <InputText
                        className="form-control w-25"
                        placeholder="Search by Loan Type"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <Button
                        label="Add New"
                        icon="pi pi-plus"
                        className="p-button-sm"
                        onClick={() => {
                            setLoanForm({ loanType: "", principalAmount: "", interestRate: "", termInMonth: "" });
                            setShowAddEditDialog(true);
                        }}
                    />
                </div>

                <div className="loan-scroll-container">
                    <div className="row">
                        {filteredLoans.map((loan) => (
                            <div className="col-md-6 col-lg-4 mb-2" key={loan.id}>
                                <Card className="shadow-sm loan-card" title={loan.loanType}>
                                    <p className="text-muted"><strong>Loan Code:</strong> #{loan.id}</p>
                                    <p><strong>Principal Amount:</strong> ₹{loan.principalAmount}</p>
                                    <p><strong>Interest Rate:</strong> {loan.interestRate}%</p>
                                    <p><strong>Term:</strong> {loan.termInMonth} months</p>

                                    <div className="d-flex justify-content-between mb-2">
                                        <Button
                                            label="Details"
                                            icon="pi pi-eye"
                                            className="p-button-text"
                                            onClick={() => {
                                                setSelectedLoan(loan);
                                                setShowDetails(true);
                                            }}
                                        />
                                        <div className="d-flex gap-2">
                                            <Button
                                                label="Edit"
                                                icon="pi pi-pencil"
                                                className="p-button-sm p-button-warning"
                                                onClick={() => {
                                                    setLoanForm(loan);
                                                    setShowAddEditDialog(true);
                                                }}
                                            />
                                        </div>
                                    </div>
                                </Card>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Add / Edit Loan Dialog */}
                <Dialog
                    header={loanForm.id ? "Edit Loan" : "Add New Loan"}
                    visible={showAddEditDialog}
                    onHide={() => setShowAddEditDialog(false)}
                    style={{ width: '30vw' }}
                >
                    <div className="p-fluid">
                        <div className="mb-2">
                            <label>Loan Type</label>
                            <Dropdown
                                value={loanForm.loanType}
                                options={loanTypes}
                                onChange={(e) => setLoanForm({ ...loanForm, loanType: e.value })}
                                placeholder="Select Loan Type"
                                className="w-100"
                            />
                        </div>
                        <div className="mb-2">
                            <label>Principal Amount</label>
                            <input
                                type="number"
                                className="form-control"
                                value={loanForm.principalAmount}
                                onChange={(e) => setLoanForm({ ...loanForm, principalAmount: e.target.value })}
                            />
                        </div>
                        <div className="mb-2">
                            <label>Interest Rate (%)</label>
                            <input
                                type="number"
                                className="form-control"
                                value={loanForm.interestRate}
                                onChange={(e) => setLoanForm({ ...loanForm, interestRate: e.target.value })}
                            />
                        </div>
                        <div className="mb-2">
                            <label>Term (Months)</label>
                            <input
                                type="number"
                                className="form-control"
                                value={loanForm.termInMonth}
                                onChange={(e) => setLoanForm({ ...loanForm, termInMonth: e.target.value })}
                            />
                        </div>

                        <div className="text-end">
                            <Button label="Save" icon="pi pi-check" onClick={handleSave} />
                        </div>
                    </div>
                </Dialog>

                {/* Details Popup */}
                <Dialog
                    header="Loan Details"
                    visible={showDetails}
                    onHide={() => setShowDetails(false)}
                    style={{ width: '30vw' }}
                    modal
                >
                    {selectedLoan && (
                        <div className="p-fluid">
                            <p><strong>Loan Type:</strong> {selectedLoan.loanType}</p>
                            <p><strong>Principal Amount:</strong> ₹{selectedLoan.principalAmount}</p>
                            <p><strong>Interest Rate:</strong> {selectedLoan.interestRate}%</p>
                            <p><strong>Term:</strong> {selectedLoan.termInMonth} months</p>
                            <p><strong>Total Repayable:</strong> ₹{selectedLoan.totalRepayableAmount}</p>
                            <p><strong>EMI:</strong> ₹{selectedLoan.emiAmount}</p>
                        </div>
                    )}
                </Dialog>
            </div>
        </div>
    );
}

export default ExecutiveLoanDetails;
