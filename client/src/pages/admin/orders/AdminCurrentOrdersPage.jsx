import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminCurrentOrdersComponent from '../../../components/adminPageComponents/orders/AdminCurrentOrdersComponent';

let component = () => ( <AdminCurrentOrdersComponent /> );

const AdminOrdersPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminOrdersPage
