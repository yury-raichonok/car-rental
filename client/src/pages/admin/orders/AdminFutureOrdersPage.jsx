import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminFutureOrdersComponent from '../../../components/adminPageComponents/orders/AdminFutureOrdersComponent';

let component = () => ( <AdminFutureOrdersComponent /> );

const AdminOrdersPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminOrdersPage
