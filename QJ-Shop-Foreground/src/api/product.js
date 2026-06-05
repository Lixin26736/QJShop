import request from '@/utils/request'

export const productApi = {
  // 客户端商品接口
  getProductList(params) {
    return request.get('/products/page', { params })
  },

  searchProducts(params) {
    return request.get('/products/search', { params })
  },

  getProductDetail(id) {
    return request.get(`/products/${id}`)
  },

  getHotProducts() {
    return request.get('/products/hot')
  },

  getNewProducts() {
    return request.get('/products/new')
  },

  // 管理端商品接口
  adminPage(params) {
    return request.get('/admin/products/page', { params })
  },

  adminGetById(id) {
    return request.get(`/admin/products/${id}`)
  },

  adminCreate(data) {
    return request.post('/admin/products', data)
  },

  adminUpdate(id, data) {
    return request.put(`/admin/products/${id}`, data)
  },

  adminDelete(id) {
    return request.delete(`/admin/products/${id}`)
  },

  adminExport(params) {
    return request.get('/admin/products/export', { params, responseType: 'blob' })
  }
}
