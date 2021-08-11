import React from 'react';
import ProfilePaymentBillsComponent from '../../components/profilePageComponents/bills/ProfilePaymentBillsComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';

let component = () => ( <ProfilePaymentBillsComponent />);

const ProfilePaymentBillsHistoryPage = (props) => {
  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfilePaymentBillsHistoryPage
