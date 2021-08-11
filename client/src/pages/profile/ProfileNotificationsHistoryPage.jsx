import React from 'react';
import ProfileNotificationsHistoryComponent from '../../components/profilePageComponents/notifications/ProfileNotificationsHistoryComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileNotificationsHistoryComponent />);

const ProfileNotificationsHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileNotificationsHistoryPage
