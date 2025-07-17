import { BreadCrumb } from 'primereact/breadcrumb';
import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

function RegisterUser() {
    const navigate = useNavigate();

    const breadcrumbItems = [
        { label: 'Manage User', command: () => navigate('/admin') },
        { label: 'Add New User' }
    ];
    const home = { icon: 'pi pi-home', command: () => navigate('/admin') };

    // States for all fields
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthday, setBirthday] = useState('');
    const [gender, setGender] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [address, setAddress] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('');
    const [branchId, setBranchId] = useState('');
    const [branches, setBranches] = useState([]);
    const [msg, setMsg] = useState('');

    useEffect(() => {
        if (role === 'EXECUTIVE') {
            axios.get('http://localhost:8080/api/branch/get-all', {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })
            .then(res => setBranches(res.data))
            .catch(() => setBranches([]));
        }
    }, [role]);

    const postUser = async (e) => {
        e.preventDefault();

        const userData = {
            firstName, lastName, email, phoneNumber: phone,
            address, gender, dateOfBirth: birthday,
            username, password
        };

        try {
            if (role === 'MANAGER') {
                await axios.post('http://localhost:8080/api/manager/post', userData,{
                    headers : { Authorization: 'Bearer ' + localStorage.getItem('token') }
                });
            } else if (role === 'EXECUTIVE') {
                await axios.post(`http://localhost:8080/api/executive/post/${branchId}`, userData, {
                    headers : { Authorization: 'Bearer ' + localStorage.getItem('token') }
                });
            } else if (role === 'CUSTOMER') {
                await axios.post('http://localhost:8080/api/customer/post', userData, {
                    headers : { Authorization: 'Bearer ' + localStorage.getItem('token') }
                });
            } else {
                setMsg("Please select a valid role.");
                return;
            }

            setMsg("User registered successfully!");
        } catch (err) {
            setMsg("Something went wrong!");
        }
    };

    return (
        <div className="container-fluid py-4">
            <div className="row mb-3">
                <div className="col-12 col-lg-10 offset-lg-1">
                    <BreadCrumb model={breadcrumbItems} home={home} />
                </div>
            </div>

            <div className="row justify-content-center">
                <div className="col-12 col-lg-10">
                    <div className="card shadow rounded-4 p-4">
                        <h2 className="text-center fw-bold mb-4 text-primary title-manage                                                                                                                                       ">Register New User</h2>

                        {msg && <div className="alert alert-info text-center">{msg}</div>}

                        <form className="row g-3" onSubmit={postUser}>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="First Name" onChange={e => setFirstName(e.target.value)} />
                                <label>First Name</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="Last Name" onChange={e => setLastName(e.target.value)} />
                                <label>Last Name</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="date" className="form-control" placeholder="Birthday" onChange={e => setBirthday(e.target.value)} />
                                <label>Birthday</label>
                            </div>
                            <div className="col-md-4 d-flex align-items-center">
                                <label className='me-3'>Gender:</label>
                                <div className='form-check me-2'>
                                    <input className="form-check-input" type="radio" name="gender" value="MALE" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Male</label>
                                </div>
                                <div className='form-check me-2'>
                                    <input className="form-check-input" type="radio" name="gender" value="FEMALE" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Female</label>
                                </div>
                                <div className='form-check'>
                                    <input className="form-check-input" type="radio" name="gender" value="OTHER" onChange={e => setGender(e.target.value)} />
                                    <label className="form-check-label">Other</label>
                                </div>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="email" className="form-control" placeholder="Email" onChange={e => setEmail(e.target.value)} />
                                <label>Email</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="tel" className="form-control" placeholder="Phone" onChange={e => setPhone(e.target.value)} />
                                <label>Phone</label>
                            </div>
                            <div className="col-md-12 form-floating">
                                <input type="text" className="form-control" placeholder="Address" onChange={e => setAddress(e.target.value)} />
                                <label>Address</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="Username" onChange={e => setUsername(e.target.value)} />
                                <label>Username</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <input type="text" className="form-control" placeholder="Password" onChange={e => setPassword(e.target.value)} />
                                <label>Password</label>
                            </div>
                            <div className="col-md-4 form-floating">
                                <select className="form-select" onChange={e => setRole(e.target.value)}>
                                    <option value="">-- Select Role --</option>
                                    <option value="CUSTOMER">CUSTOMER</option>
                                    <option value="EXECUTIVE">EXECUTIVE</option>
                                    <option value="MANAGER">MANAGER</option>
                                </select>
                                <label>Role</label>
                            </div>

                            {role === 'EXECUTIVE' && (
                                <div className="col-md-12 form-floating">
                                    <select className="form-select" onChange={e => setBranchId(e.target.value)}>
                                        <option value="">-- Select Branch --</option>
                                        {branches.map(branch => (
                                            <option key={branch.id} value={branch.id}>
                                                {branch.branchName} - {branch.ifscCode}
                                            </option>
                                        ))}
                                    </select>
                                    <label>Select Branch</label>
                                </div>
                            )}

                            <div className="d-grid gap-2 col-4 mx-auto mt-3">
                                <button type="submit" className="btn btn-primary rounded-pill button-register">Register</button>
                            </div>

                            <div className="text-center mt-2">
                                Back to Admin? <Link to="/admin">Dashboard</Link>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RegisterUser;
    