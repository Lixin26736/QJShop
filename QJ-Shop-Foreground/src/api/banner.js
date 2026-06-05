import request from '@/utils/request'

export const bannerApi = {
  list(position) {
    return request.get('/api/banners', { params: { position } })
  },

  adminPage(params) {
    return request.get('/api/admin/banners/page', { params })
  },

  getById(id) {
    return request.get(`/api/admin/banners/${id}`)
  },

  create(data) {
    return request.post('/api/admin/banners', data)
  },

  update(id, data) {
    return request.put(`/api/admin/banners/${id}`, data)
  },

  delete(id) {
    return request.delete(`/api/admin/banners/${id}`)
  }
}
