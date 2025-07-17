import { useEffect, useState } from 'react'
import { BreadCrumb } from 'primereact/breadcrumb'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { InputText } from 'primereact/inputtext'
import { Dialog } from 'primereact/dialog'
import { Button } from 'primereact/button'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'

function MyLoan() {
  const [loans, setLoans] = useState([])
  const [filter, setFilter] = useState('')
  const [detailsLoan, setDetailsLoan] = useState(null)
  const [showDetailsDialog, setShowDetailsDialog] = useState(false)
  const [showRepayDialog, setShowRepayDialog] = useState(false)
  const [repayLoan, setRepayLoan] = useState(null)
  const [repayAmount, setRepayAmount] = useState('')

  const navigate = useNavigate()
  const breadcrumbItems = [
    { label: 'Loan', command: () => navigate('/customer/loan') },
    { label: 'My Loan' }
  ]
  const home = { icon: 'pi pi-home', command: () => navigate('/customer') }

  useEffect(() => {
    fetchLoans()
  }, [])

  const fetchLoans = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/loan/get-one', {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
      });
      setLoans(res.data)
    } catch (e) {
      console.error('Failed to fetch loans', e)
    }
  }

  const showDetails = loan => {
    setDetailsLoan(loan)
    setShowDetailsDialog(true)
  }

  const showRepayment = loan => {
    setRepayLoan(loan)
    setRepayAmount(loan.loanApplication.loanDetails.emiAmount);
    setShowRepayDialog(true)
  }

  const repayLoanAmount = async () => {
    try {
      await axios.post(`http://localhost:8080/api/loanRepay/post/${repayLoan.id}?amount=${repayAmount}`, {}, {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
      });
      alert("Repayment successful")
      setShowRepayDialog(false)
      fetchLoans()
    } catch (err) {
      if (err.response.data.msg == "You cannot pay more than the loan balance amount") {
        alert(err.response.data.msg)
      } else {
        alert("Repayment failed")
      }
    }
  }

  const header = (
    <div className="d-flex justify-content-end mb-2">
      <InputText placeholder="Search loans..." value={filter} onChange={e => setFilter(e.target.value)} />
    </div>
  )

  return (
    <div className="container mt-2">
      <BreadCrumb model={breadcrumbItems} home={home} />
      <div className="card shadow p-4 mt-3">
        <h2 className="text-center fw-bold fs-3 mb-4 myloan-title">My Loan</h2>

        <DataTable value={loans} paginator rows={5} globalFilter={filter}
          header={header} className="p-datatable-sm table-bordered"
        >
          <Column field="id" header="Loan ID" style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column field="loanApplication.loanDetails.loanType" header="Type" sortable style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column header="Loan Details" style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} body={row =>
            <Button label="Details" className="p-button-text" onClick={() => showDetails(row)} />
          } />
          <Column field="balanceAmount" header="Outstanding" body={row => `₹${row.balanceAmount}`} style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column field="status" header="Status" sortable style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column field="startDate" header="Start Date" style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column field="endDate" header="End Date" style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} />
          <Column header="Repayment" style={{ textAlign: 'center' }} headerStyle={{ textAlign: 'center' }} body={row =>
            <Button label="Repay" className="p-button-text p-button-warning" onClick={() => showRepayment(row)} />
          } />
        </DataTable>

        {/* Loan Details Dialog */}
        <Dialog header="Loan Detail" visible={showDetailsDialog} onHide={() => setShowDetailsDialog(false)} style={{ width: '30vw' }}>
          {detailsLoan && (
            <div className="p-fluid">
              <p><strong>Loan ID:</strong> {detailsLoan.id}</p>
              <p><strong>Status:</strong> {detailsLoan.status}</p>
              <p><strong>Balance:</strong> ₹{detailsLoan.balanceAmount}</p>
              <p><strong>Start Date:</strong> {detailsLoan.startDate}</p>
              <p><strong>End Date:</strong> {detailsLoan.endDate}</p>
              <hr />
              <p><strong>Loan Code:</strong> #{detailsLoan.loanApplication.loanDetails.id}</p>
              <p><strong>Loan Type:</strong> {detailsLoan.loanApplication.loanDetails.loanType}</p>
              <p><strong>Principal:</strong> ₹{detailsLoan.loanApplication.loanDetails.principalAmount}</p>
              <p><strong>Interest Rate:</strong> {detailsLoan.loanApplication.loanDetails.interestRate}%</p>
              <p><strong>Term:</strong> {detailsLoan.loanApplication.loanDetails.termInMonth} months</p>
              <p><strong>Total Repayable:</strong> ₹{detailsLoan.loanApplication.loanDetails.totalRepayableAmount}</p>
              <p><strong>EMI:</strong> ₹{detailsLoan.loanApplication.loanDetails.emiAmount}</p>
              <p><strong>Application ID:</strong> {detailsLoan.loanApplication.id}</p>
              <p><strong>Account ID:</strong> {detailsLoan.loanApplication.account.id}</p>
            </div>
          )}
        </Dialog>

        {/* Repayment Dialog */}
        <Dialog header="Repay Loan" visible={showRepayDialog} onHide={() => setShowRepayDialog(false)} style={{ width: '25vw' }}>
          {repayLoan && (
            <div className="p-fluid">
              <p className="mb-2"><strong>Loan ID:</strong> {repayLoan.id}</p>
              <p><strong>Account ID:</strong> {repayLoan.loanApplication.account.id}</p>
              <p className="text-danger">Amount will be debited from this account</p>
              <p>Emi Amount:</p>
              <InputText value={`₹${repayAmount}`} placeholder="Enter amount" keyfilter="pint" className="w-100" />
              <Button label="Repay" className="mt-3 w-100 p-button-success" onClick={repayLoanAmount} />
            </div>
          )}
        </Dialog>
      </div>
    </div>
  )
}

export default MyLoan
