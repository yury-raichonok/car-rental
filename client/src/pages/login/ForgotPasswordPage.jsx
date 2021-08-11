import React from 'react';
import ForgotPasswordComponent from '../../components/loginPageComponents/ForgotPasswordComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

const ForgotPasswordPage = (props) => {

  let component = () => ( <ForgotPasswordComponent setUser={props.setUser}/>);

  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ForgotPasswordPage
