import React from 'react';
import ProfileOrdersComponent from '../../components/profilePageComponents/orders/ProfileOrdersComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileOrdersComponent />);

const ProfileOrdersPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileOrdersPage
