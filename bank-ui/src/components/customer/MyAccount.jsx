import React, { useEffect, useState } from "react"
import { BreadCrumb } from "primereact/breadcrumb"
import { Button } from "primereact/button"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Dialog } from "primereact/dialog" // 🆕 For the popup
import { useNavigate } from "react-router-dom"
import axios from "axios"

function MyAccount() {
    const navigate = useNavigate()
    const [account, setAccount] = useState([])
    localStorage.setItem("account", JSON.stringify(account))
    const [error, setError] = useState("")

    const [showTxnDialog, setShowTxnDialog] = useState(false)
    const [txnData, setTxnData] = useState([])
    const [showCloseDialog, setShowCloseDialog] = useState(false)
    const [selectedAccountId, setSelectedAccountId] = useState("")

    const home = { icon: "pi pi-home", command: () => navigate("/customer") }

    useEffect(() => {
        fetchAccount()
    }, [])

    const fetchAccount = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/account/get-one", {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            const filtered = res.data.filter(acc => acc.status !== 'REJECTED' && acc.status !== 'CLOSED')
            setAccount(filtered)
        } catch (err) {
            if (err.response.data.msg === "Customer has no account") {
                setError("You don't have an account. Create one to get started.")
            } else {
                setError("Failed to load account.")
            }
        }
    }

    const handleLast10Txn = async (accountId) => {
        try {
            const res = await axios.get(`http://localhost:8080/api/transaction/get-10/${accountId}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setTxnData(res.data)
            setShowTxnDialog(true)
        } catch (error) {
            console.error("Error fetching transactions:", error)
        }
    }

    const handleCloseAccount = async () => {
        if (!selectedAccountId) {
            setError("Please select an account to close.")
            return
        }

        try {
            await axios.put(
                `http://localhost:8080/api/account/put/status/${selectedAccountId}/?status=CLOSING_REQUESTED`,
                null,
                {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                }
            )
            alert("Account closure request submitted successfully.")
            setShowCloseDialog(false)
            fetchAccount()
        } catch (error) {
            alert("Failed to close account. Please try again.")
        }
    }


    return (
        <div className="container mt-4">
            <BreadCrumb home={home} />
            <div className="card mt-4">
                <div className="card-body">
                    <h2 className="mt-3 fw-bold text-center mb-4 title-account">My Account</h2>

                    { error!="" ?
                        <div className="alert alert-warning mt-3">
                            {error}{" "}
                            <Button
                                label="Create Account"
                                icon="pi pi-plus"
                                className="ms-2"
                                onClick={() => navigate("/customer/account/create")}
                            />
                        </div> : ""
                    }

                    { account.length > 0 ?
                        <div>
                            <DataTable value={account} className="mt-4">    
                                <Column field="id" header="Account ID" style={{ textAlign: 'center' }} />
                                <Column field="accountType.type" header="Account Type" style={{ textAlign: 'center' }} />
                                <Column field="branch.ifscCode" header="IFSC Code" style={{ textAlign: 'center' }} />
                                <Column field="branch.branchName" header="Branch Name" style={{ textAlign: 'center' }} />
                                <Column field="status" header="Status" style={{ textAlign: 'center' }} />
                                <Column header="Balance" body={(rowData) => `₹${rowData.balance.toFixed(2)}`} style={{ textAlign: 'center' }} />

                                <Column
                                    header="Last 10 Txns"
                                    body={(rowData) => (
                                        <Button
                                            label="View"
                                            icon="pi pi-eye"
                                            className="p-button-sm"
                                            onClick={() => handleLast10Txn(rowData.id)}
                                        />
                                    )}
                                    style={{ textAlign: 'center' }}
                                />
                            </DataTable>

                            <div className="text-end mt-3">
                                <div className="d-flex justify-content-between mt-3">
                                    <Button
                                        label="Request Account Closure"
                                        icon="pi pi-times-circle"
                                        className="p-button-danger"
                                        onClick={() => {
                                            setShowCloseDialog(true);
                                            setSelectedAccountId("");
                                        }}
                                    />
                                    <Button
                                        label="Create Another Account"
                                        icon="pi pi-plus"
                                        onClick={() => navigate("/customer/account/create")}
                                    />
                                </div>
                            </div>
                        </div> : ""
                    }

                    {/* Transactions Popup */}
                    <Dialog
                        header="Last 10 Transactions"
                        visible={showTxnDialog}
                        onHide={() => setShowTxnDialog(false)}
                        style={{ width: '60vw' }}
                        breakpoints={{ '960px': '95vw' }}
                    >
                        {txnData.length > 0 ? 
                            <DataTable value={txnData}>
                                <Column field="transactionDate" header="Date" />
                                <Column field="transactionType" header="Type" />
                                <Column field="amount" header="Amount" body={(rowData) => `₹${rowData.amount.toFixed(2)}`} />
                                <Column field="entryType" header="Entry" />
                                <Column field="description" header="Description" />
                                <Column field="balanceAfterTxn" header="Balance After" body={(rowData) => `₹${rowData.balanceAfterTxn.toFixed(2)}`} />
                            </DataTable> :
                            <div>No transactions found.</div>
                        }
                    </Dialog>

                    {/* Closing Request Popup */}
                    <Dialog
                        header="Request Account Closure"
                        visible={showCloseDialog}
                        onHide={() => setShowCloseDialog(false)}
                        style={{ width: '30vw' }}
                    >
                        <div className="mb-3">
                            <div>Select an account to close:</div>
                            <select
                                className="form-select mt-2"
                                value={selectedAccountId}
                                onChange={(e) => setSelectedAccountId(e.target.value)}
                            >
                                <option value="">-- Select Account --</option>
                                {account.map((acc) => (
                                    <option key={acc.id} value={acc.id}>
                                        Account.ID: {acc.id} - {acc.accountType.type}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="text-end">
                            <Button label="Submit" icon="pi pi-check" onClick={handleCloseAccount} />
                        </div>
                    </Dialog>

                </div>
            </div>
        </div>
    )
}

export default MyAccount