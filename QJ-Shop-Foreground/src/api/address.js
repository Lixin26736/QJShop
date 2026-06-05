import request from '@/utils/request'

export const addressApi = {
  list() {
    return request.get('/api/user/addresses')
  },

  getById(id) {
    return request.get(`/api/user/addresses/${id}`)
  },

  create(data) {
    return request.post('/api/user/addresses', data)
  },

  update(id, data) {
    return request.put(`/api/user/addresses/${id}`, data)
  },

  delete(id) {
    return request.delete(`/api/user/addresses/${id}`)
  },

  setDefault(id) {
    return request.put(`/api/user/addresses/${id}/default`)
  }
}
