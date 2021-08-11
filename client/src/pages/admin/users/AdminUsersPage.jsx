import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminUsersComponent from '../../../components/adminPageComponents/users/AdminUsersComponent';

const AdminUsersPage = (props) => {

  let component = () => ( <AdminUsersComponent user={props.user} /> );

  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminUsersPage
