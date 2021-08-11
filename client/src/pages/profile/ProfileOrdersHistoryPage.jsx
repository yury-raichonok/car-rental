import React from 'react';
import ProfileOrdersHistoryComponent from '../../components/profilePageComponents/orders/ProfileOrdersHistoryComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileOrdersHistoryComponent />);

const ProfileOrdersHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileOrdersHistoryPage
