import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminRepairBillsComponent from '../../../components/adminPageComponents/bills/AdminRepairBillsComponent';

let component = () => ( <AdminRepairBillsComponent /> );

const AdminRepairBillsPage = (props) => {
  return (
    <div>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default AdminRepairBillsPage
