import axios from 'axios';

export default axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
    "Access-Control-Allow-Credentials": "true",
    'Authorization':'Bearer '+ localStorage.getItem('token')
  }
})