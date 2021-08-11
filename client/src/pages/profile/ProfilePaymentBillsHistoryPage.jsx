import React from 'react';
import ProfilePaymentBillsHistoryComponent from '../../components/profilePageComponents/bills/ProfilePaymentBillsHistoryComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfilePaymentBillsHistoryComponent />);

const ProfilePaymentBillsHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfilePaymentBillsHistoryPage
