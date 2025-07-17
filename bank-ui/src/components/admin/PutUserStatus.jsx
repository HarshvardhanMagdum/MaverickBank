import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Button } from "primereact/button";
import { BreadCrumb } from "primereact/breadcrumb";
import { fetchUserInfo } from "../../store/actions/UserInfoAction";
import { useDispatch } from "react-redux";

function PutUserStatus() {
    const { id, status } = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch()

    const newStatus = status === "ACTIVE" ? "INACTIVE" : "ACTIVE";

    const home = { icon: 'pi pi-home', command: () => navigate('/admin') };
    const breadcrumbItems = [
        { label: 'Manage User', command: () => navigate('/admin') },
        { label: 'Change User Status' }
    ];

    const handleStatusChange = async () => {
        try {
            await axios.put(
                `http://localhost:8080/api/user/put/status/${id}?status=${newStatus}`,
                {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token'),
                    },
                }
            );
            fetchUserInfo(dispatch)()
            navigate('/admin');
        } catch (error) {
            console.error("Error updating user status:", error);
        }
    };

    return (
        <div className="container py-4">
            <div className="row mb-3">
                <div className="col-lg-8 offset-lg-2">
                    <BreadCrumb model={breadcrumbItems} home={home} />
                </div>
            </div>
            <div className="row justify-content-center">
                <div className="col-lg-8">
                    <div className="card custom-card">
                        <div className="card-body text-center">
                            <h3 className="title-manage mb-5 ">Change User Status</h3>
                            <p>User ID: <strong>{id}</strong></p>
                            <p>Current Status: <strong>{status}</strong></p>
                            <p>New Status: <strong>{newStatus}</strong></p>
                            <div className="d-flex justify-content-center gap-3 mt-4">
                                <Button
                                    label={`Confirm ${newStatus}`}
                                    icon="pi pi-check"
                                    className={`p-button-${newStatus === "ACTIVE" ? "success" : "danger"}`}
                                    onClick={handleStatusChange}
                                />
                                <Button
                                    label="Cancel"
                                    icon="pi pi-times"
                                    className="p-button-secondary"
                                    onClick={() => navigate('/admin')}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PutUserStatus;
