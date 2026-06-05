import request from '@/utils/request'

export const reviewApi = {
  listByProduct(productId, params) {
    return request.get(`/reviews/product/${productId}`, { params })
  },

  submit(data) {
    return request.post('/reviews', data)
  },

  // Admin APIs
  adminPage(params) {
    return request.get('/admin/reviews/page', { params })
  },

  reply(id, reply) {
    return request.put(`/admin/reviews/${id}/reply`, { reply })
  },

  updateStatus(id, status) {
    return request.put(`/admin/reviews/${id}/status`, { status })
  },

  delete(id) {
    return request.delete(`/admin/reviews/${id}`)
  }
}
