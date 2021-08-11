import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminNewOrdersComponent from '../../../components/adminPageComponents/orders/AdminNewOrdersComponent';

let component = () => ( <AdminNewOrdersComponent /> );

const AdminOrdersPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminOrdersPage
