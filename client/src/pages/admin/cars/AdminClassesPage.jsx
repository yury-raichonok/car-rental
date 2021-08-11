import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminClassesComponent from '../../../components/adminPageComponents/cars/carClassComponents/AdminClassesComponent';

let component = () => ( <AdminClassesComponent /> );

const AdminClassesPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminClassesPage
