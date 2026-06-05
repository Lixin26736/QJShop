import request from '@/utils/request'

export const reviewApi = {
  listByProduct(productId, params) {
    return request.get(`/api/reviews/product/${productId}`, { params })
  },

  submit(data) {
    return request.post('/api/reviews', data)
  },

  adminPage(params) {
    return request.get('/api/admin/reviews/page', { params })
  },

  reply(id, reply) {
    return request.put(`/api/admin/reviews/${id}/reply`, { reply })
  },

  updateStatus(id, status) {
    return request.put(`/api/admin/reviews/${id}/status`, { status })
  },

  delete(id) {
    return request.delete(`/api/admin/reviews/${id}`)
  }
}
