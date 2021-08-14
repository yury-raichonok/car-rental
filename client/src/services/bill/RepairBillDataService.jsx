import axios from 'axios';

class RepairBillDataService {
  findAll(data) {
    return axios({
      'method':'POST',
      'url': `http://localhost:8080/repairbills`,
      'data': data,
      'headers' : {
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
        "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Credentials": "true",
        'Authorization':'Bearer '+ localStorage.getItem('token')
      },
      withCredentials: true,
    })
  }

  findUserBills(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url': `http://localhost:8080/repairbills/user?page=${page}&size=${size}&sort=${sort},${dir}`,
      'headers' : {
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
        "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Credentials": "true",
        'Authorization':'Bearer '+ localStorage.getItem('token')
      },
      withCredentials: true,
    })
  }

  findNewUserBills(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url': `http://localhost:8080/repairbills/user/new?page=${page}&size=${size}&sort=${sort},${dir}`,
      'headers' : {
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
        "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Credentials": "true",
        'Authorization':'Bearer '+ localStorage.getItem('token')
      },
      withCredentials: true,
    })
  }

  findUserRepairBillsAmount() {
    return axios({
      'method':'GET',
      'url': `http://localhost:8080/repairbills/user/new/amount`,
      'headers' : {
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
        "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Credentials": "true",
        'Authorization':'Bearer '+ localStorage.getItem('token')
      },
    })
  }

  payBill(id) {
    return axios({
      'method':'POST',
      'url': `http://localhost:8080/repairbills/pay/${id}`,
      'headers' : {
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
        "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Credentials": "true",
        'Authorization':'Bearer '+ localStorage.getItem('token')
      }
    })
  }
}

export default new RepairBillDataService();
