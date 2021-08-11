import './App.css';
import { useState, useEffect } from 'react';
import { Route, BrowserRouter, Switch, useHistory } from 'react-router-dom';
import AboutAppPage from './pages/about/AboutAppPage';
import AdminDashboardPage from './pages/admin/dashboard/AdminDashboardPage';
import BrandsPage from './pages/brands/BrandsPage';
import CarPage from './pages/car/CarPage';
import ContactUsPage from './pages/contact/ContactUsPage';
import HomePage from './pages/home/HomePage';
import ErrorPage from './pages/error/ErrorPage';
import ProfilePage from './pages/profile/ProfilePage';
import ProfileOrdersPage from './pages/profile/ProfileOrdersPage';
import ProfileOrdersHistoryPage from './pages/profile/ProfileOrdersHistoryPage';
import ProfileRepairBillsPage from './pages/profile/ProfileRepairBillsPage';
import ProfilePaymentBillsPage from './pages/profile/ProfilePaymentBillsPage';
import ProfileRepairBillsHistoryPage from './pages/profile/ProfileRepairBillsHistoryPage';
import ProfilePaymentBillsHistoryPage from './pages/profile/ProfilePaymentBillsHistoryPage';
import ProfileNotificationsPage from './pages/profile/ProfileNotificationsPage';
import ProfileNotificationsHistoryPage from './pages/profile/ProfileNotificationsHistoryPage';
import RegisterPage from './pages/register/RegisterPage';
import SearchCarPage from './pages/searchCar/SearchCarPage';
import AdminPaymentBillsPage from './pages/admin/bills/AdminPaymentBillsPage';
import AdminRepairBillsPage from './pages/admin/bills/AdminRepairBillsPage';
import AdminCarsPage from './pages/admin/cars/AdminCarsPage';
import AdminBrandsPage from './pages/admin/cars/AdminBrandsPage';
import AdminModelsPage from './pages/admin/cars/AdminModelsPage';
import AdminClassesPage from './pages/admin/cars/AdminClassesPage';
import AdminFaqsPage from './pages/admin/faqs/AdminFaqsPage';
import AdminLocationsPage from './pages/admin/locations/AdminLocationsPage';
import AdminNewMessagesPage from './pages/admin/messages/AdminNewMessagesPage';
import AdminAllMessagesPage from './pages/admin/messages/AdminAllMessagesPage';
import AdminRequestsPage from './pages/admin/requests/AdminRequestsPage';
import AdminNewRequestsPage from './pages/admin/requests/AdminNewRequestsPage';
import AdminAllOrdersPage from './pages/admin/orders/AdminAllOrdersPage';
import AdminNewOrdersPage from './pages/admin/orders/AdminNewOrdersPage';
import AdminCurrentOrdersPage from './pages/admin/orders/AdminCurrentOrdersPage';
import AdminFutureOrdersPage from './pages/admin/orders/AdminFutureOrdersPage';
import AdminUsersPage from './pages/admin/users/AdminUsersPage';
import LoginPage from './pages/login/LoginPage';
import { fetchUserData } from './services/authenticationService/AuthenticationService';
import ForgotPasswordPage from './pages/login/ForgotPasswordPage';
import ResetPasswordPage from './pages/login/ResetPasswordPage';
import {SWRConfig} from 'swr';
import axios from 'axios';

function App() {

  let history = useHistory();

  const [user, setUser] = useState({});

  useEffect(() => {
    fetchUserData().then((res) => {
      setUser(res.data);
    }).catch((err) => {
      localStorage.clear();
    })
  },[])

  const headers = {
    "Content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
    "Access-Control-Allow-Credentials": "true",
    'Authorization':'Bearer '+ localStorage.getItem('token')
  }

  return (
    <div className="App">
      <BrowserRouter history={history}>
        <Switch>
          <SWRConfig value={{fetcher: (url) => axios.get(url, {headers: {headers}}).then(r => r.data)}}>
            <Route exact path="/login" component={() => <LoginPage setUser={setUser}/>}/>
            <Route exact path="/error" component={() => <ErrorPage setUser={setUser}/>}/>
            <Route exact path="/forgot" component={() => <ForgotPasswordPage setUser={setUser}/>}/>
            <Route exact path="/forgot/reset/:id" component={(props) => <ResetPasswordPage {...props} setUser={setUser} />}/>
            <Route exact path="/" component={() => <HomePage setUser={setUser} user={user} />} />
            <Route exact path="/about" component={() => <AboutAppPage setUser={setUser} user={user} />} />
            <Route exact path="/admin" component={() => <AdminDashboardPage setUser={setUser} user={user} />} />
            <Route exact path="/brands" component={() => <BrandsPage setUser={setUser} user={user} />} />
            <Route exact path="/contact" component={() => <ContactUsPage setUser={setUser} user={user} />} />
            <Route exact path="/search/cars/:id" component={(props) => <CarPage {...props} setUser={setUser} user={user} />} />
            <Route exact path="/register" component={() => <RegisterPage setUser={setUser} user={user} />} />
            <Route exact path="/profile" component={() => <ProfilePage setUser={setUser} user={user} />} />
            <Route exact path="/profile/orders" component={() => <ProfileOrdersPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/orders/history" component={() => <ProfileOrdersHistoryPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/paymentbills" component={() => <ProfilePaymentBillsPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/paymentbills/history" component={() => <ProfilePaymentBillsHistoryPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/repairbills" component={() => <ProfileRepairBillsPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/repairbills/history" component={() => <ProfileRepairBillsHistoryPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/notifications" component={() => <ProfileNotificationsPage setUser={setUser} user={user} />} />
            <Route exact path="/profile/notifications/history" component={() => <ProfileNotificationsHistoryPage setUser={setUser} user={user} />} />
            <Route exact path="/search" component={(props) => <SearchCarPage setUser={setUser} user={user} {...props}/>} />
            <Route exact path="/admin/paymentbills" component={() => <AdminPaymentBillsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/repairbills" component={() => <AdminRepairBillsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/cars" component={() => <AdminCarsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/brands" component={() => <AdminBrandsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/models" component={() => <AdminModelsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/classes" component={() => <AdminClassesPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/faqs" component={() => <AdminFaqsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/locations" component={() => <AdminLocationsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/messages/new" component={() => <AdminNewMessagesPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/messages/" component={() => <AdminAllMessagesPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/requests/" component={() => <AdminRequestsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/requests/new" component={() => <AdminNewRequestsPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/orders" component={() => <AdminAllOrdersPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/orders/new" component={() => <AdminNewOrdersPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/orders/current" component={() => <AdminCurrentOrdersPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/orders/future" component={() => <AdminFutureOrdersPage setUser={setUser} user={user} />} />
            <Route exact path="/admin/users" component={() => <AdminUsersPage setUser={setUser} user={user} />} />
          </SWRConfig>
        </Switch>      
      </BrowserRouter>
    </div>
  );
}

export default App;
