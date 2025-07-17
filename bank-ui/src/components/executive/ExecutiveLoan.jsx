import { useNavigate } from "react-router-dom";
import { BreadCrumb } from "primereact/breadcrumb";
import { Card } from "primereact/card";
import { Button } from "primereact/button";

function ExecutiveLoan() {
    const navigate = useNavigate();

    const home = { icon: "pi pi-home", command: () => navigate("/executive") };
    const breadcrumbItems = [{ label: "Loans" }];

    return (
        <div className="container mt-4">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-3">
                <h3 className="text-center fw-bold fs-2 mt-4 mb-5 Loan-title">Loans Management</h3>

                <div className="row g-4 justify-content-center mt-4 mb-5">
                    <div className="col-md-4">
                        <Card title="Loan Details" className="text-center h-100">
                            <p>Manage loan types - add, edit or delete loan schemes.</p>
                            <Button
                                label="Manage"
                                className="p-button-sm p-button-primary"
                                onClick={() => navigate("/executive/loans/loanDetails")}
                            />
                        </Card>
                    </div>

                    <div className="col-md-4">
                        <Card title="Loan Applications" className="text-center h-100">
                            <p>Review loan applications submitted by customers.</p>
                            <Button
                                label="View"
                                className="p-button-sm p-button-info"
                                onClick={() => navigate("/executive/loans/applications")}
                            />
                        </Card>
                    </div>

                    <div className="col-md-4">
                        <Card title="Approved Loans" className="text-center h-100">
                            <p>Track all approved and active loans.</p>
                            <Button
                                label="Track"
                                className="p-button-sm p-button-success"
                                onClick={() => navigate("/executive/loans/approvedLoans")}
                            />
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ExecutiveLoan;
