import React from 'react';
import AdminPageComponent from '../../../components/adminPageComponents/AdminPageComponent';
import AdminAllMessagesComponent from '../../../components/adminPageComponents/messages/AdminAllMessagesComponent';

let component = () => ( <AdminAllMessagesComponent /> );

const AdminMessagesPage = (props) => {
  return (
    <>
      <AdminPageComponent setUser={props.setUser} user={props.user} child={component}/>
    </>
  )
}

export default AdminMessagesPage
