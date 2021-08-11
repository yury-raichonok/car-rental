import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminNewRequestsComponent from '../../../components/adminPageComponents/requests/AdminNewRequestsComponent';

let component = () => ( <AdminNewRequestsComponent /> );

const AdminNewRequestsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminNewRequestsPage
