import React from 'react';
import ProfileRepairBillsComponent from '../../components/profilePageComponents/bills/ProfileRepairBillsComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileRepairBillsComponent />);

const ProfileRepairBillsHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileRepairBillsHistoryPage
