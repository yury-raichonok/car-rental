import React from 'react';
import ResetPasswordComponent from '../../components/loginPageComponents/ResetPasswordComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

const ResetPasswordPage = (props) => {

  let component = () => ( <ResetPasswordComponent {...props} setUser={props.setUser}/>);

  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ResetPasswordPage
