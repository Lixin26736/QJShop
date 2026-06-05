import request from '@/utils/request'

export const userApi = {
  login(data) {
    return request.post('/api/admin/auth/login', data)
  },

  register(data) {
    return request.post('/api/admin/auth/register', data)
  },

  getUserInfo() {
    return request.get('/api/user/profile')
  },

  updateUserInfo(data) {
    return request.put('/api/user/profile', data)
  },

  logout() {
    return request.post('/api/admin/auth/logout')
  }
}
