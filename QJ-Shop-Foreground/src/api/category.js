import request from '@/utils/request'

export const categoryApi = {
  getCategoryList() {
    return request.get('/api/admin/categories/page')
  },

  getCategoryTree() {
    return request.get('/api/admin/categories/tree')
  },

  getCategoryDetail(id) {
    return request.get(`/api/admin/categories/${id}`)
  }
}
