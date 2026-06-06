import request from '@/utils/request'

export const orderApi = {
  getOrderCounts() {
    return request.get('/api/user/orders/counts')
  },

  createOrder(data) {
    return request.post('/api/user/orders', data)
  },

  getOrderList(params) {
    return request.get('/api/user/orders', { params })
  },

  getOrderDetail(id) {
    return request.get(`/api/user/orders/${id}`)
  },

  cancelOrder(id) {
    return request.put(`/api/user/orders/${id}/cancel`)
  },

  payOrder(id) {
    return request.put(`/api/user/orders/${id}/pay`)
  },

  // Admin
  adminPage(params) {
    return request.get('/api/admin/orders/page', { params })
  },

  adminGetDetail(id) {
    return request.get(`/api/admin/orders/${id}`)
  },

  adminCreate(data) {
    return request.post('/api/admin/orders', data)
  },

  adminUpdate(id, data) {
    return request.put(`/api/admin/orders/${id}`, data)
  },

  adminDelete(id) {
    return request.delete(`/api/admin/orders/${id}`)
  },

  adminShip(id) {
    return request.put(`/api/admin/orders/${id}/ship`)
  },

  adminComplete(id) {
    return request.put(`/api/admin/orders/${id}/complete`)
  },

  adminExport(params) {
    return request.get('/api/admin/orders/export', { params, responseType: 'blob' })
  }
}
