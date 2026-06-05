import request from '@/utils/request'

export const favoriteApi = {
  list(params) {
    return request.get('/api/user/favorites', { params })
  },

  add(productId) {
    return request.post(`/api/user/favorites/${productId}`)
  },

  remove(productId) {
    return request.delete(`/api/user/favorites/${productId}`)
  },

  check(productId) {
    return request.get(`/api/user/favorites/check/${productId}`)
  }
}
