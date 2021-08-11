import React from 'react';
import ProfileComponent from '../../components/profilePageComponents/profile/ProfileComponent';
import ProfilePageComponent from '../../components/profilePageComponents/ProfilePageComponent';



const ProfilePage = (props) => {

  console.log(props.user)
  
  let component = () => ( 
    <ProfileComponent user={props.user} />
    
  );

  return (
    <div>
      <ProfilePageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default ProfilePage
