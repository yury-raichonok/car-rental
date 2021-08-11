import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminBrandsComponent from '../../../components/adminPageComponents/cars/brandComponents/AdminBrandsComponent';

let component = () => ( <AdminBrandsComponent /> );

const AdminBrandsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminBrandsPage
