import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminAllOrdersComponent from '../../../components/adminPageComponents/orders/AdminAllOrdersComponent';

let component = () => ( <AdminAllOrdersComponent /> );

const AdminOrdersPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminOrdersPage
