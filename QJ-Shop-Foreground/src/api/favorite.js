import request from '@/utils/request'

export const favoriteApi = {
  list(params) {
    return request.get('/user/favorites', { params })
  },

  add(productId) {
    return request.post(`/user/favorites/${productId}`)
  },

  remove(productId) {
    return request.delete(`/user/favorites/${productId}`)
  },

  check(productId) {
    return request.get(`/user/favorites/check/${productId}`)
  }
}
