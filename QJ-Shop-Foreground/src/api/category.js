import request from '@/utils/request'

export const categoryApi = {
  getCategoryList() {
    return request.get('/api/admin/categories/first')
  },

  getCategoryTree() {
    return request.get('/api/admin/categories/first')
  },

  getCategoryDetail(id) {
    return request.get(`/api/admin/categories/${id}`)
  }
}
