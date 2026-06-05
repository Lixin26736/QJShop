import request from '@/utils/request'

export const csApi = {
  // 用户端
  getMessages() {
    return request.get('/user/cs/messages')
  },

  sendMessage(content, image) {
    return request.post('/user/cs/send', { content, image })
  },

  // 管理端
  getUnread() {
    return request.get('/admin/cs/unread')
  },

  getUnreadCount() {
    return request.get('/admin/cs/unread-count')
  },

  getMessagesByUser(userId) {
    return request.get(`/admin/cs/user/${userId}`)
  },

  reply(userId, content) {
    return request.post('/admin/cs/reply', { userId, content })
  },

  markRead(id) {
    return request.put(`/admin/cs/${id}/read`)
  }
}
