import axios from 'axios';

export const fetchUserData=(authRequest)=>{
  return axios({
    method:'GET',
    url:`${process.env.hostUrl||'http://localhost:8080'}/users/auth/userinfo`,
    headers:{
      'Authorization':'Bearer '+ localStorage.getItem('token')
    }
  })
}