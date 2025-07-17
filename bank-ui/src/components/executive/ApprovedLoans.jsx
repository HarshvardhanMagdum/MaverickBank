import React, { useEffect, useState } from 'react';
import { BreadCrumb } from 'primereact/breadcrumb';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Dialog } from 'primereact/dialog';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function ApprovedLoans() {
  const [loans, setLoans] = useState([]);
  const [filter, setFilter] = useState('');
  const [statusOptions, setStatusOptions] = useState([]);
  const [selectedLoan, setSelectedLoan] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const [showRepayments, setShowRepayments] = useState(false);
  const [repayments, setRepayments] = useState([]);

  const navigate = useNavigate();
  const home = { icon: 'pi pi-home', command: () => navigate("/executive") };
  const breadcrumbItems = [
    { label: 'Loans', command: () => navigate('/executive/loans') },
    { label: 'Loan List' }
  ];

  const branchId = localStorage.getItem("branchId");

  useEffect(() => {
    fetchLoans();
    fetchStatusOptions();
  }, []);

  const fetchLoans = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/loan/get-by/branchId/${branchId}`, {
        headers: { Authorization: "Bearer " + localStorage.getItem("token") }
      });
      setLoans(res.data);
    } catch (err) {
      console.error("Error fetching loans", err);
    }
  };

  const fetchStatusOptions = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/enum/loan/status/get");
      setStatusOptions(res.data);
    } catch (err) {
      console.error("Error fetching status options");
    }
  };

  const handleStatusChange = async (loanId, status) => {
    try {
      await axios.put(`http://localhost:8080/api/loan/put/status/${loanId}?status=${status}`, {
        headers: { Authorization: "Bearer " + localStorage.getItem("token") }
      });
      fetchLoans();
    } catch (err) {
      alert("Failed to update status.");
    }
  };

  const loadRepayments = async (loanId) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/loanRepay/get-by/loanId/${loanId}`, {
        headers: { Authorization: "Bearer " + localStorage.getItem("token") }
      });
      setRepayments(res.data);
      setShowRepayments(true);
    } catch (err) {
      console.error("Failed to fetch repayments");
    }
  };

  const header = (
    <div className="d-flex justify-content-end mb-2">
      <InputText
        placeholder="Search loans..."
        value={filter}
        onChange={(e) => setFilter(e.target.value)}
      />
    </div>
  );

  return (
    <div className="container mt-2">
      <BreadCrumb model={breadcrumbItems} home={home} />
      <div className="card shadow p-4 mt-3">
        <h2 className="text-center fw-bold fs-3 mb-4">Approved Loans</h2>

        <DataTable
          value={loans}
          paginator rows={5}
          globalFilter={filter}
          header={header}
          className="p-datatable-sm"
        >
          <Column field="id" header="Loan ID" style={{ textAlign: 'center' }} />
          <Column header="Loan Details" body={(row) => (
            <Button label="Details" className="p-button-text"
              onClick={() => { setSelectedLoan(row), setShowDetails(true) }} />
          )} style={{ textAlign: 'center' }}
          />
          <Column field="balanceAmount" header="Outstanding" body={(row) => `₹${row.balanceAmount}`} style={{ textAlign: 'center' }} />
          <Column field="status" header="Status" body={(row) => (
            <Dropdown value={row.status} options={statusOptions} onChange={(e) => handleStatusChange(row.id, e.value)}
              disabled={row.status === "CLOSED"} className="w-100" />
          )} style={{ textAlign: 'center' }}
          />
          <Column
            field="startDate"
            header="Start Date"
            body={(row) => new Date(row.startDate).toLocaleDateString()}
            style={{ textAlign: 'center' }}
          />
          <Column
            field="endDate"
            header="End Date"
            body={(row) => new Date(row.endDate).toLocaleDateString()}
            style={{ textAlign: 'center' }}
          />
          <Column
            header="View Payments"
            body={(row) => (
              <Button
                label="View"
                className="p-button-text p-button-warning"
                onClick={() => loadRepayments(row.id)}
              />
            )}
            style={{ textAlign: 'center' }}
          />
        </DataTable>

        {/* Loan Details Dialog */}
        <Dialog header="Loan Details" visible={showDetails} onHide={() => setShowDetails(false)} style={{ width: '30vw' }}>
          {selectedLoan && (
            <div className="p-fluid">
              <p><strong>Loan ID:</strong> {selectedLoan.id}</p>
              <p><strong>Status:</strong> {selectedLoan.status}</p>
              <p><strong>Balance:</strong> ₹{selectedLoan.balanceAmount}</p>
              <p><strong>Start Date:</strong> {new Date(selectedLoan.startDate).toLocaleDateString()}</p>
              <p><strong>End Date:</strong> {new Date(selectedLoan.endDate).toLocaleDateString()}</p>
              <hr />
              <p><strong>Loan Type:</strong> {selectedLoan.loanApplication.loanDetails.loanType}</p>
              <p><strong>Principal:</strong> ₹{selectedLoan.loanApplication.loanDetails.principalAmount}</p>
              <p><strong>Interest Rate:</strong> {selectedLoan.loanApplication.loanDetails.interestRate}%</p>
              <p><strong>Term:</strong> {selectedLoan.loanApplication.loanDetails.termInMonth} months</p>
              <p><strong>Total Repayable:</strong> ₹{selectedLoan.loanApplication.loanDetails.totalRepayableAmount}</p>
              <p><strong>EMI:</strong> ₹{selectedLoan.loanApplication.loanDetails.emiAmount}</p>
              <p><strong>Application ID:</strong> {selectedLoan.loanApplication.id}</p>
              <p><strong>Account ID:</strong> {selectedLoan.loanApplication.account.id}</p>
              <hr className="my-3" />
              <h5 className="mb-3 fw-bold text-success">Customer Details</h5>
              <p><strong>Name:</strong> {selectedLoan.loanApplication.account.customer.firstName} {selectedLoan.loanApplication.account.customer.lastName}</p>
              <p><strong>Email:</strong> {selectedLoan.loanApplication.account.customer.email}</p>
              <p><strong>Phone:</strong> {selectedLoan.loanApplication.account.customer.phoneNumber}</p>
              <p><strong>DOB:</strong> {selectedLoan.loanApplication.account.customer.dateOfBirth}</p>
            </div>
          )}
        </Dialog>

        {/* Repayment Dialog */}
        <Dialog header="Loan Repayments" visible={showRepayments} onHide={() => setShowRepayments(false)} style={{ width: '25vw' }}>
          {repayments.length > 0 ? (
            <ul className="list-group">
              {repayments.map((r, idx) => (
                <li className="list-group-item d-flex justify-content-between" key={idx}>
                  <span>₹{r.repaymentAmount}</span>
                  <span>{new Date(r.repaymentDate).toLocaleDateString()}</span>
                </li>
              ))}
            </ul>
          ) : (
            <p>No repayment records found.</p>
          )}
        </Dialog>
      </div>
    </div>
  );
}

export default ApprovedLoans;
