import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminCarsComponent from '../../../components/adminPageComponents/cars/carComponents/AdminCarsComponent';

let component = () => ( <AdminCarsComponent /> );

const AdminCarsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminCarsPage
