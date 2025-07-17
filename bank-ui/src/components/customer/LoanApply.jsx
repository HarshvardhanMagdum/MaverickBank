import React, { use, useEffect, useState } from 'react'
import { BreadCrumb } from 'primereact/breadcrumb'
import { Card } from 'primereact/card'
import { Button } from 'primereact/button'
import { Dialog } from 'primereact/dialog'
import { Dropdown } from 'primereact/dropdown'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import {    useSelector } from 'react-redux'

function LoanApply() {
    const [loanList, setLoanList] = useState([])
    const [selectedLoan, setSelectedLoan] = useState(null)
    const [showDetails, setShowDetails] = useState(false)
    const [accounts, setAccounts] = useState([])
    const [selectedAccounts, setSelectedAccounts] = useState({})
    const [searchTerm, setSearchTerm] = useState("")

    const navigate = useNavigate()
    const allAccounts = useSelector(state => state.account.account)

    const breadcrumbItems = [
        { label: 'Loan', command: () => navigate('/customer/loan') },
        { label: 'Apply Loan' }
    ]
    const home = { icon: 'pi pi-home', command: () => navigate('/customer') }

    useEffect(() => {
        fetchLoans()
        filterActiveAccounts()
    }, [])

    const fetchLoans = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/loanDetails/get/all", {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            })
            setLoanList(res.data)
        } catch (err) {
            console.error("Failed to fetch loan details", err)
        }
    }

    const filterActiveAccounts = () => {
        try {
            const activeAccounts = allAccounts.filter(acc => acc.status === "ACTIVE");
            setAccounts(activeAccounts)
        } catch (err) {
            console.error("Failed to fetch accounts", err)
        }
    }

    const applyLoan = async (loanId) => {
        const account = selectedAccounts[loanId]
        if (!account) {
            alert("Please select an account to apply.")
            return
        }
        try {
            await axios.post(
                `http://localhost:8080/api/loanApply/post/${loanId}/${account.id}`,
                {},
                {
                    headers: { Authorization: "Bearer " + localStorage.getItem("token") }
                }
            )
            alert("Loan applied successfully.")
        } catch (err) {
            alert("Loan application failed.")
        }
    }

    const filteredLoans = loanList.filter(loan =>
        loan.loanType.toLowerCase().includes(searchTerm.toLowerCase())
    )

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow mt-2 p-4">
                <h3 className="text-center fw-bold mb-2 Apply-title">Loan Details</h3>

                <div className="d-flex justify-content-end mb-2"    >
                    <input type="text" className="form-control w-25" placeholder="Search by Loan Type" value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)} />
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
                                    <Button label="Details" icon="pi pi-eye" className="p-button-text"
                                        onClick={() => {setSelectedLoan(loan), setShowDetails(true)}} />
                                    <div className="mt-3">
                                        <Dropdown value={selectedAccounts[loan.id] || null}
                                            onChange={(e) => setSelectedAccounts(sa => ({ ...sa, [loan.id]: e.value }))}
                                            options={accounts} optionLabel={(a) => `${a.id} - ${a.accountType.type}`}
                                            placeholder="Select Account ID to apply" className="w-100" />
                                    </div>
                                    <Button label="Apply" className="p-button-sm p-button-success mt-2 w-100" onClick={() => applyLoan(loan.id)}/>
                                </Card>
                            </div>
                        ))}
                    </div>
                </div>

                <Dialog header="Loan Details" visible={showDetails} onHide={() => setShowDetails(false)} style={{ width: '30vw' }} modal>
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

export default LoanApply;
