import React, { useState, useEffect } from 'react'
import { BreadCrumb } from 'primereact/breadcrumb'
import { Button } from 'primereact/button'
import { Dropdown } from 'primereact/dropdown'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useSelector } from 'react-redux'

function CustomerTransaction() {
    const [transactionType, setTransactionType] = useState("")
    const [accounts, setAccounts] = useState([])
    const [selectedAccount, setSelectedAccount] = useState(null)

    const [beneficiaries, setBeneficiaries] = useState([])
    const [selectedBeneficiary, setSelectedBeneficiary] = useState(null)
    const [noBeneficiary, setNoBeneficiary] = useState(false)

    const [amount, setAmount] = useState("")
    const [description, setDescription] = useState("")

    const [transferTypes, setTransferTypes] = useState([])
    const [selectedTransferType, setSelectedTransferType] = useState(null)

    const navigate = useNavigate()

    const allAccounts = useSelector(state => state.account.account)

    const breadcrumbItems = [{ label: 'Transaction' }]
    const home = { icon: 'pi pi-home', command: () => navigate("/customer") }

    useEffect(() => {
        filterActiveAccounts()
        if (transactionType === "TRANSFER") {
            fetchBeneficiaries()
            fetchTransferTypes()
        }
    }, [transactionType])

    const filterActiveAccounts = () => {
            const activeAccounts = allAccounts.filter(acc => acc.status === "ACTIVE")
            setAccounts(activeAccounts)
    }

    const fetchBeneficiaries = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/beneficiary/get", {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") }
            });
            setNoBeneficiary(res.data.length === 0)
            setBeneficiaries(res.data);
        } catch (err) {
            console.error("Error fetching beneficiaries", err)
        }
    }

    const fetchTransferTypes = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/enum/transfer/type/get");
            setTransferTypes(res.data);
        } catch (err) {
            console.error("Error fetching transfer types", err)
        }
    }

    const processTransaction = async () => {
        const transactionDto = { amount, description }
        try {
            if (transactionType === "DEPOSIT") {
                await axios.post(`http://localhost:8080/api/transaction/post/deposit/${selectedAccount.id}`, transactionDto, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                })
            } else if (transactionType === "WITHDRAW") {
                await axios.post(`http://localhost:8080/api/transaction/post/withdraw/${selectedAccount.id}`, transactionDto, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                })
            } else if (transactionType === "TRANSFER") {
                await axios.post(
                    `http://localhost:8080/api/transaction/post/transfer/${selectedAccount.id}/${selectedBeneficiary.id}?transferType=${selectedTransferType.type}`,
                    transactionDto, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                }
                )
            }
            setAmount("")
            setDescription("")
            setSelectedAccount(null)
            setSelectedBeneficiary(null)
            setSelectedTransferType(null)
            alert("Transaction successful")
        } catch (err) {
            alert("Transaction failed")
        }
    }

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card p-4 mt-2 shadow">
                <h3 className="text-center fw-bold mb-4 transaction-heading">Transaction</h3>

                <div className="d-flex justify-content-center gap-4 mb-4">
                    <Button label="Deposit" icon="pi pi-plus" onClick={() => setTransactionType("DEPOSIT")} />
                    <Button label="Withdraw" icon="pi pi-minus" className="p-button-warning" onClick={() => setTransactionType("WITHDRAW")} />
                    <Button label="Transfer" icon="pi pi-send" className="p-button-info" onClick={() => setTransactionType("TRANSFER")} />
                </div>

                {transactionType && (
                    <div className="row justify-content-center">
                        <div className="col-md-6">
                            <div className="mb-3">
                                <label>Select Account</label>
                                <Dropdown value={selectedAccount} onChange={(e) => setSelectedAccount(e.value)}
                                    options={accounts} optionLabel={(a) => `${a.id} - ${a.accountType.type}`}
                                    placeholder="Select Account" className="w-100"
                                />
                            </div>

                            {transactionType === "TRANSFER" && (
                                <>
                                    {noBeneficiary ? (
                                        <div className="alert alert-warning d-flex justify-content-between align-items-center">
                                            <span>No Beneficiaries available</span>
                                            <Button label="Go to Beneficiary" icon="pi pi-user-plus" className="p-button-sm"
                                                onClick={() => navigate("/customer/beneficiary")}
                                            />
                                        </div>
                                    ) : (
                                        <>
                                            <div className="mb-3">
                                                <label>Select Beneficiary</label>
                                                <Dropdown value={selectedBeneficiary} onChange={(e) => setSelectedBeneficiary(e.value)}
                                                    placeholder="Select Beneficiary" className="w-100" options={beneficiaries}
                                                    optionLabel={(b) => `${b.name} - ${b.accountNumber} - ${b.ifscCode} - ${b.branchName}`}
                                                />
                                            </div>
                                            <div className="mb-3 row">
                                                <div className="col-6">
                                                    <label>Amount</label>
                                                    <input type="number" className="form-control" value={amount}
                                                        onChange={(e) => setAmount(e.target.value)}
                                                    />
                                                </div>
                                                <div className="col-6">
                                                    <label>Transfer Type</label>
                                                    <Dropdown value={selectedTransferType} onChange={(e) => setSelectedTransferType(e.value)}
                                                        options={transferTypes} optionLabel={(t) => `${t.type} - ₹${t.charge.toFixed(2)}`}
                                                        placeholder="Select Type" className="w-100"
                                                    />
                                                </div>
                                            </div>
                                        </>
                                    )}
                                </>
                            )}

                            {transactionType !== "TRANSFER" && (
                                <div className="mb-3">
                                    <label>Amount</label>
                                    <input type="number" className="form-control" value={amount}
                                        onChange={(e) => setAmount(e.target.value)}
                                    />
                                </div>
                            )}

                            <div className="mb-3">
                                <label>Description</label>
                                <textarea className="form-control" rows={2} value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />
                            </div>

                            <Button
                                label="Process" className="p-button-success w-100 mt-3" onClick={processTransaction}
                                disabled={!selectedAccount || !amount ||
                                    (transactionType === "TRANSFER" && (!selectedBeneficiary || !selectedTransferType))
                                }
                            />
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default CustomerTransaction;
