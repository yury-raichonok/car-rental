import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminNewMessagesComponent from '../../../components/adminPageComponents/messages/AdminNewMessagesComponent';

const AdminMessagesPage = (props) => {

  let component = () => ( <AdminNewMessagesComponent /> );

  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminMessagesPage
