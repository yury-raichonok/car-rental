import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminFaqsComponent from '../../../components/adminPageComponents/faqs/AdminFaqsComponent';

let component = () => ( <AdminFaqsComponent /> );

const AdminFaqsPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminFaqsPage
