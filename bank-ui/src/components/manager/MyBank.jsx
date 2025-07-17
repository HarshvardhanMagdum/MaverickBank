import React, { useEffect, useState } from "react"
import { BreadCrumb } from "primereact/breadcrumb"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { Card } from "primereact/card"
import { Button } from "primereact/button"

function MyBank() {
    const navigate = useNavigate()
    const [branch, setBranch] = useState("")

    useEffect(() => {
        const fetchExecutiveDetails = async () => {
            try {
                const res = await axios.get("http://localhost:8080/api/manager/get-one", {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                })
                setBranch(res.data.branch);
            } catch (err) {
                console.error("Failed to fetch executive details", err);
            }
        }

        fetchExecutiveDetails()
    }, [])

    const home = { icon: "pi pi-home", command: () => navigate("/executive") }

    return (
        <div className="container mt-4">
            <BreadCrumb home={home} />

            <div className="card mt-4 p-4 shadow">
                        <h2 className="text-center mb-4 fw-bold mybranch-title">Maverick Bank</h2>

                        {/* Contact Info */}
                        <div className="row text-center mb-4">
                            <div className="col-md-4 mb-3">
                                <strong>Email:</strong><br />
                                maverickBank@mavk.com
                            </div>
                            <div className="col-md-4 mb-3">
                                <strong>Contact:</strong><br />
                                 04422230000
                            </div>
                            <div className="col-md-4 mb-3">
                                <strong>Address:</strong><br />
                                Chennai
                            </div>
                        </div>

                        {/* Navigation Cards */}
                        <div className="row text-center mt-4">
                            <div className="col-md-4 mb-3">
                                <Card
                                    className="p-3 h-100 shadow-sm mybranch-card"
                                    title="Accounts"
                                    style={{ cursor: 'pointer' }}
                                    onClick={() => navigate("/manager/accounts")}
                                >
                                    <p>View  accounts of this bank.</p>
                                    <Button label="Go" icon="pi pi-arrow-right" />
                                </Card>
                            </div>

                            <div className="col-md-4 mb-3">
                                <Card
                                    className="p-3 h-100 shadow-sm mybranch-card"
                                    title="Transactions"
                                    style={{ cursor: 'pointer' }}
                                    onClick={() => navigate("/manager/transactions")}
                                >
                                    <p>Review transaction history and activities of this bank.</p>
                                    <Button label="Go" icon="pi pi-arrow-right" />
                                </Card>
                            </div>

                            <div className="col-md-4 mb-3">
                                <Card
                                    className="p-3 h-100 shadow-sm mybranch-card"
                                    title="Loans"
                                    style={{ cursor: 'pointer' }}
                                    onClick={() => navigate("/manager/loans")}
                                >
                                    <p>View loans of this bank.</p>
                                    <Button label="Go" icon="pi pi-arrow-right" />
                                </Card>
                            </div>
                        </div>  
            </div>
        </div>
    );
}

export default MyBank
