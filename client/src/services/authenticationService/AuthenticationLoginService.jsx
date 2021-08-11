import axios from 'axios';

export const userLogin=(authRequest)=>{
  return axios({
    'method':'POST',
    'url':`${process.env.hostUrl||'http://localhost:8080'}/users/auth/login`,
    'data':authRequest,
    'headers' : {
      "Content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
      "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
      "Access-Control-Allow-Credentials": "true"
    }
  })
}