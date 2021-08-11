import axios from 'axios';

class BrandDataService {

  getAll() {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/brands/all`,
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

  findAllPaged(page, size, sort, dir) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/brands/all/paged?page=${page}&size=${size}&sort=${sort},${dir}`,
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

  getAllWithRentalOffers() {
    return axios({
      'method':'GET',
      'url':"http://localhost:8080/brands",
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

  uploadImage(id, file) {
    return axios({
      'method': 'POST',
      'url': `http://localhost:8080/brands/${id}/upload/image`,
      'data': file,
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

  get(id) {
    return axios({
      'method':'GET',
      'url':`http://localhost:8080/brands/${id}`,
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
      'url': "http://localhost:8080/brands",
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
      'url':`http://localhost:8080/brands/${id}`,
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

export default new BrandDataService();
