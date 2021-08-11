import React from 'react';
import PageComponent from '../../components/pageComponents/PageComponent';
import ContactUsComponent from '../../components/contactUsPageComponent/ContactUsComponent';

let component = () => ( 
  <ContactUsComponent />
);

const ContactUsPage = (props) => {
  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ContactUsPage
