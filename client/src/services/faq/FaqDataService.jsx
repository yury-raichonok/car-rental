import axios from 'axios';

class FaqDataService {
    findAll() {
      return axios({
        'method':'GET',
        'url':"http://localhost:8080/faqs",
        'headers' : {
          "Content-type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE",
          "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
          "Access-Control-Allow-Credentials": "true",
          'Authorization':'Bearer '+ localStorage.getItem('token')
        },
        withCredentials: true,
      })
    }

    findAllPaged(page, size, sort, dir) {
      return axios({
        'method':'GET',
        'url':`http://localhost:8080/faqs/paged?page=${page}&size=${size}&sort=${sort},${dir}`,
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

    create(data) {
      return axios({
        'method': 'POST',
        'url': "http://localhost:8080/faqs",
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
        'url':`http://localhost:8080/faqs/${id}`,
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

    delete(id) {
      return axios({
        'method':'DELETE',
        'url':`http://localhost:8080/faqs/${id}`,
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

export default new FaqDataService();