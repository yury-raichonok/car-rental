import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminPaymentBillsComponent from '../../../components/adminPageComponents/bills/AdminPaymentBillsComponent';

let component = () => ( <AdminPaymentBillsComponent /> );

const AdminPaymentBillsPage = (props) => {
  return (
    <div>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default AdminPaymentBillsPage
