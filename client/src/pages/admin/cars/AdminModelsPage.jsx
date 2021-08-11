import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminModelsComponent from '../../../components/adminPageComponents/cars/modelComponents/AdminModelsComponent';

let component = () => ( <AdminModelsComponent /> );

const AdminCModelsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminCModelsPage
