import { useNavigate } from 'react-router-dom';
import { BreadCrumb } from 'primereact/breadcrumb';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';

function CustomerLoan() {
    const navigate = useNavigate();

    const breadcrumbItems = [{ label: 'Loan' }];
    const home = { icon: 'pi pi-home', command: () => navigate('/customer') };

    return (
        <div className="container mt-2">
            <BreadCrumb model={breadcrumbItems} home={home} />
            <div className="card shadow p-4 mt-3">
                <h3 className="text-center fw-bold fs-3 mt-4 mb-5 Loan-title">Loan</h3>

                <div className="row g-4 justify-content-center">
                    <div className="col-md-4">
                        <Card title="My Loan" className="text-center h-100">
                            <p>See all your active loans.</p>
                            <Button label="Summary" className="p-button-sm p-button-primary" onClick={() => navigate('/customer/loan/MyLoan')} />
                        </Card>
                    </div>
                    <div className="col-md-4">
                        <Card title="Apply for Loan" className="text-center h-100">
                            <p>Start a new loan application.</p>
                            <Button label="Apply" className="p-button-sm p-button-success" onClick={() => navigate('/customer/loan/apply')} />
                        </Card>
                    </div>
                    <div className="col-md-4">
                        <Card title="View Applications" className="text-center h-100">
                            <p>Check status of your loan applications.</p>
                            <Button label="View" className="p-button-sm p-button-info" onClick={() => navigate('/customer/loan/applications')} />
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CustomerLoan
