import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminRequestsComponent from '../../../components/adminPageComponents/requests/AdminRequestsComponent';

let component = () => ( <AdminRequestsComponent /> );

const AdminRequestsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminRequestsPage
