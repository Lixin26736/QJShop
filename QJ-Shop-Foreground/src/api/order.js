import request from '@/utils/request'

export const orderApi = {
  createOrder(data) {
    return request.post('/api/admin/orders', data)
  },

  getOrderList(params) {
    return request.get('/api/admin/orders/page', { params })
  },

  getOrderDetail(id) {
    return request.get(`/api/admin/orders/${id}`)
  },

  cancelOrder(id) {
    return request.put(`/api/admin/orders/${id}/cancel`)
  },

  payOrder(id, data) {
    return request.put(`/api/admin/orders/${id}/pay`, data)
  }
}
