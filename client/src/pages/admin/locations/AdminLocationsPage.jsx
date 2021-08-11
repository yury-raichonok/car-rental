import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminLocationsComponent from '../../../components/adminPageComponents/locations/AdminLocationsComponent';

let component = () => ( <AdminLocationsComponent /> );

const AdminLocationsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminLocationsPage
