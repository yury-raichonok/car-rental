import React from 'react';
import ProfileNotificationsComponent from '../../components/profilePageComponents/notifications/ProfileNotificationsComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileNotificationsComponent />);

const ProfileNotificationsPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileNotificationsPage
