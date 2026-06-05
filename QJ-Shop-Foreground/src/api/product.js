import request from '@/utils/request'

export const productApi = {
  getProductList(params) {
    return request.get('/api/products/page', { params })
  },

  searchProducts(params) {
    return request.get('/api/products/search', { params })
  },

  getProductDetail(id) {
    return request.get(`/api/products/${id}`)
  },

  getHotProducts() {
    return request.get('/api/products/hot')
  },

  getNewProducts() {
    return request.get('/api/products/new')
  },

  // Admin
  adminPage(params) {
    return request.get('/api/admin/products/page', { params })
  },

  adminGetById(id) {
    return request.get(`/api/admin/products/${id}`)
  },

  adminCreate(data) {
    return request.post('/api/admin/products', data)
  },

  adminUpdate(id, data) {
    return request.put(`/api/admin/products/${id}`, data)
  },

  adminDelete(id) {
    return request.delete(`/api/admin/products/${id}`)
  },

  adminExport(params) {
    return request.get('/api/admin/products/export', { params, responseType: 'blob' })
  }
}
