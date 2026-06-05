import request from '@/utils/request'

export const bannerApi = {
  list(position) {
    return request.get('/banners', { params: { position } })
  },

  // Admin APIs
  adminPage(params) {
    return request.get('/admin/banners/page', { params })
  },

  getById(id) {
    return request.get(`/admin/banners/${id}`)
  },

  create(data) {
    return request.post('/admin/banners', data)
  },

  update(id, data) {
    return request.put(`/admin/banners/${id}`, data)
  },

  delete(id) {
    return request.delete(`/admin/banners/${id}`)
  }
}
