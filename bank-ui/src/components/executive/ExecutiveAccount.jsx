import { useNavigate } from "react-router-dom";
import { BreadCrumb } from "primereact/breadcrumb";
import { Card } from "primereact/card";
import { Button } from "primereact/button";

function ExecutiveAccount() {
    const navigate = useNavigate();

    const breadcrumbItems = [{ label: "Accounts" }];
    const home = { icon: "pi pi-home", command: () => navigate("/executive") };

    return (
        <div className="container mt-4">
            <BreadCrumb model={breadcrumbItems} home={home} />

            <div className="card shadow p-4 mt-3">
                <h3 className="text-center fw-bold fs-2 mt-4 mb-5 main-title">Accounts Management</h3>

                <div className="row g-4 justify-content-center mt-4 mb-5">
                    {/* View All Accounts */}
                    <div className="col-md-4">
                        <Card title="View All Accounts" className="text-center h-100">
                            <p>Explore all customer accounts registered under your branch.</p>
                            <Button
                                label="View"
                                className="p-button-sm p-button-primary"
                                onClick={() => navigate("/executive/accounts/all")}
                            />
                        </Card>
                    </div>

                    {/* Pending Approval Accounts */}
                    <div className="col-md-4">
                        <Card title="Pending Approvals" className="text-center h-100">
                            <p>Review and approve newly requested customer accounts.</p>
                            <Button
                                label="Review"
                                className="p-button-sm p-button-warning"
                                onClick={() => navigate("/executive/accounts/pending")}
                            />
                        </Card>
                    </div>

                    {/* Closing Requested Accounts */}
                    <div className="col-md-4">
                        <Card title="Closing Requests" className="text-center h-100">
                            <p>Handle customer requests to close or cancel their accounts.</p>
                            <Button
                                label="Handle"
                                className="p-button-sm p-button-danger"
                                onClick={() => navigate("/executive/accounts/closing")}
                            />
                        </Card>
                    </div>
                </div>
                <div className="text-center mt-4">
                    <Button
                        label="Manage Account Types"
                        icon="pi pi-cog"
                        className="p-button-secondary p-button-rounded mb-3"
                        onClick={() => navigate("/executive/accounts/types")}
                    />
                </div>
            </div>
        </div>
    );
}

export default ExecutiveAccount;
