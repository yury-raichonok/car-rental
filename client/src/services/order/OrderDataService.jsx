import axios from 'axios';

class OrderDataService {

  findAll(data) {
    return axios({
      'method':'POST',
      'url': "http://localhost:8080/orders/search",
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

  findAllNew(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/new?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  findNewOrdersAmount() {
    return axios({
      'method':'GET',
      'url':"http://localhost:8080/orders/new/amount",
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

  findAllCurrent(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/current?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  findAllFuture(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/future?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  calculateTotalCost(data) {
    return axios({
      'method': 'POST',
      'url': "http://localhost:8080/orders/calculate",
      'data': data,
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

  createOrder(data) {
    return axios({
      'method': 'POST',
      'url': "http://localhost:8080/orders",
      'data': data,
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

  approveOrder(id) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/approve/${id}`,
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

  rejectOrder(id, data) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/reject/${id}`,
      'data': data,
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

  completeOrder(id) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/complete/${id}`,
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

  completeOrderWithPenalty(id, data) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/complete/${id}/penalty`,
      'data': data,
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

  update(id, data) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/${id}`,
      'data': data,
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

  startRentalPeriod(id) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/start/${id}`,
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

  cancelOrderAfterPayment(id, data) {
    return axios({
      'method':'PUT',
      'url':`http://localhost:8080/orders/cancel/${id}`,
      'data': data,
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

  findNewUserOrders(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/user?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  findUserOrdersHistory(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/user/history?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  exportOrderToPDF(id) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/orders/${id}/export`,
      'responseType': 'blob',
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

export default new OrderDataService();