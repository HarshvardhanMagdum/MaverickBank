import Login from './components/Login'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import CustomerSignUp from './components/customer/CustomerSignup'
import CustomerDashboard from './components/customer/CustomerDashboard'
import AdminDashboard from './components/admin/AdminDashboard'
import Complaints from './components/admin/Complaints'
import ManageUser from './components/admin/ManageUser'
import AddNewUser from './components/admin/AddNewUser'
import PutUserStatus from './components/admin/PutUserStatus'
import MyAccount from './components/customer/MyAccount'
import CustomerProfile from './components/customer/CustomerProfile'
import CreateAccount from './components/customer/CreateAccount'
import Report from './components/customer/Report'
import Beneficiary from './components/customer/Beneficiary'
import LoanApply from './components/customer/LoanApply'
import MyLoanApplications from './components/customer/MyLoanApplications'
import MyLoan from './components/customer/MyLoan'
import ExecutiveDashboard from './components/executive/ExecutiveDashboard'
import ExecutiveAccount from './components/executive/ExecutiveAccount'
import ExecutiveProfile from './components/executive/ExecutiveProfile'
import MyBranch from './components/executive/MyBranch'
import CustomerTransaction from './components/customer/CustomerTransaction'
import ExecutiveTransaction from './components/executive/ExecutiveTransaction'
import ExecutiveLoan from './components/executive/ExecutiveLoan'
import CustomerLoan from './components/customer/CustomerLoan'
import LoanDetails from './components/executive/LoanDetails'
import LoanApplications from './components/executive/LoanApplications'
import ApprovedLoans from './components/executive/ApprovedLoans'
import AllAccounts from './components/executive/AllAccounts'
import PendingAccounts from './components/executive/PendingAccounts'
import ClosingRequestAccounts from './components/executive/ClosingRequestAccounts'
import AccountCreation from './components/executive/AccountCreation'
import AddCustomer from './components/executive/AddCustomer'
import AccountTypes from './components/executive/AccountTypes'
import ManagerDashboard from './components/manager/ManagerDashboard'
import ManagerProfile from './components/manager/ManagerProfile'
import MyBank from './components/manager/MyBank'
import Branch from './components/manager/Branch'
import Accounts from './components/manager/Accounts'
import Transactions from './components/manager/Transactions'
import Loans from './components/manager/Loans'
import ManageBranch from './components/manager/ManageBranch'
  
function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<CustomerSignUp />} />
        <Route path="/customer" element={<CustomerDashboard/>}>
          <Route index element={<MyAccount/>}/>
          <Route path="account/create" element={<CreateAccount/>}/>
          <Route path="profile" element={<CustomerProfile/>}/>
          <Route path="transaction" element={<CustomerTransaction/>} />
          <Route path="report" element={<Report/>} />
          <Route path="beneficiary" element={<Beneficiary />} />
          <Route path="loan" element={<CustomerLoan/>} />
          <Route path="loan/applications" element={<MyLoanApplications/>}/>
          <Route path="loan/apply" element={<LoanApply/>}/>
          <Route path="loan/MyLoan" element={<MyLoan/>}/>
        </Route>
        <Route path="/executive" element={<ExecutiveDashboard/>}>
          <Route index element={<MyBranch/>}/>
          <Route path="accounts" element={<ExecutiveAccount/>}/>
          <Route path="accounts/all" element={<AllAccounts/>}/>
          <Route path="accounts/pending" element={<PendingAccounts/>}/>
          <Route path="accounts/closing" element={<ClosingRequestAccounts/>}/>
          <Route path="accounts/types" element={<AccountTypes/>}/>
          <Route path="transactions" element={<ExecutiveTransaction/>}/>
          <Route path="loans" element={<ExecutiveLoan/>}/>
          <Route path="loans/loanDetails" element={<LoanDetails/>}/>
          <Route path="loans/applications" element={<LoanApplications/>}/>
          <Route path="loans/approvedLoans" element={<ApprovedLoans/>}/>
          <Route path="createAccount" element={<AccountCreation/>}/>
          <Route path="addCustomer" element={<AddCustomer/>}/>
          <Route path="profile" element={<ExecutiveProfile/>}/>
        </Route>
        <Route path="/admin" element={<AdminDashboard/>}>
          <Route index element={<ManageUser/>}/>
          <Route path="addUser" element={<AddNewUser/>}/>
          <Route path="putUserStatus/:id/:status" element={<PutUserStatus/>}/>
          <Route path="complaints" element={<Complaints />} />
        </Route>
        <Route path="/manager" element={<ManagerDashboard/>}>
          <Route index element={<MyBank/>}/>
          <Route path="accounts" element={<Accounts/>}/>
          <Route path="transactions" element={<Transactions/>}/>
          <Route path="loans" element={<Loans/>}/>
          <Route path="branch" element={<Branch/>}/>
          <Route path="branch/manage/:id" element={<ManageBranch/>}/>"
          <Route path="profile" element={<ManagerProfile/>}/>
        </Route>
        {/* <Route path="/loan" element={<LoanApplicationForm/>}/> */}
      </Routes>
    </BrowserRouter>
  )
}

export default App
