import axios from 'axios';

class ModelDataService {
  getAll() {
    return axios({
      'method':'GET',
      'url':"http://localhost:8080/models",
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

  getModelsListWithBrandName(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/models/list?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  getModelsByBrandName(brandName) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/models/brand/${brandName}`,
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

  getModelsByBrandId(id) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/models/brand/id/${id}`,
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
      'url': "http://localhost:8080/models",
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
      'url':`http://localhost:8080/models/${id}`,
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
}

export default new ModelDataService();