import React from 'react';
import ProfileRepairBillsHistoryComponent from '../../components/profilePageComponents/bills/ProfileRepairBillsHistoryComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfileRepairBillsHistoryComponent />);

const ProfileRepairBillsHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfileRepairBillsHistoryPage
