import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminDashboardComponent from '../../../components/adminPageComponents/dashboard/AdminDashboardComponent';

let component = () => ( <AdminDashboardComponent /> );

const AdminDashboardPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminDashboardPage
