import React from 'react';
import LoginComponent from '../../components/loginPageComponents/LoginComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

const LoginPage = (props) => {

  let component = () => ( <LoginComponent setUser={props.setUser}/>);

  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default LoginPage
