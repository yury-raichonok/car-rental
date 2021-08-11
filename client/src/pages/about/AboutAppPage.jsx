import React from 'react';
import PageComponent from '../../components/pageComponents/PageComponent';
import AboutUsPageComponent from '../../components/aboutUsPageComponents/AboutUsPageComponent';

let component = () => ( 
    <AboutUsPageComponent />
  );

const AboutAppPage = (props) => {
  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default AboutAppPage
