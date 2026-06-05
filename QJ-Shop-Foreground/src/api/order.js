import request from '@/utils/request'

export const orderApi = {
  // 客户端订单接口
  createOrder(data) {
    return request.post('/user/orders', data)
  },

  getOrderList(params) {
    return request.get('/user/orders', { params })
  },

  getOrderDetail(id) {
    return request.get(`/user/orders/${id}`)
  },

  cancelOrder(id) {
    return request.put(`/user/orders/${id}/cancel`)
  },

  payOrder(id) {
    return request.put(`/user/orders/${id}/pay`)
  },

  // 管理端订单接口
  adminPage(params) {
    return request.get('/admin/orders/page', { params })
  },

  adminGetDetail(id) {
    return request.get(`/admin/orders/${id}`)
  },

  adminCreate(data) {
    return request.post('/admin/orders', data)
  },

  adminUpdate(id, data) {
    return request.put(`/admin/orders/${id}`, data)
  },

  adminDelete(id) {
    return request.delete(`/admin/orders/${id}`)
  },

  adminShip(id) {
    return request.put(`/admin/orders/${id}/ship`)
  },

  adminComplete(id) {
    return request.put(`/admin/orders/${id}/complete`)
  },

  adminExport(params) {
    return request.get('/admin/orders/export', { params, responseType: 'blob' })
  }
}
