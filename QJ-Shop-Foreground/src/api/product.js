import request from '@/utils/request'

export const productApi = {
  getProductList(params) {
    return request.get('/api/admin/products/page', { params })
  },

  getProductDetail(id) {
    return request.get(`/api/admin/products/${id}`)
  },

  getHotProducts() {
    return request.get('/api/admin/products/hot')
  },

  getNewProducts() {
    return request.get('/api/admin/products/new')
  },

  getProductsByCategory(categoryId, params) {
    return request.get(`/api/admin/products/category/${categoryId}`, { params })
  }
}
