import request from '@/utils/request'

export const addressApi = {
  list() {
    return request.get('/user/addresses')
  },

  getById(id) {
    return request.get(`/user/addresses/${id}`)
  },

  create(data) {
    return request.post('/user/addresses', data)
  },

  update(id, data) {
    return request.put(`/user/addresses/${id}`, data)
  },

  delete(id) {
    return request.delete(`/user/addresses/${id}`)
  },

  setDefault(id) {
    return request.put(`/user/addresses/${id}/default`)
  }
}
