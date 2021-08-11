import React from 'react';
import RegisterComponent from '../../components/registerPageComponents/RegisterComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

let component = () => ( 
    <div>
      <RegisterComponent />
    </div>
  );

const RegisterPage = (props) => {
  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default RegisterPage
