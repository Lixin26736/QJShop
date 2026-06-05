import request from '@/utils/request'

export const csApi = {
  getMessages() {
    return request.get('/api/user/cs/messages')
  },

  sendMessage(content, image) {
    return request.post('/api/user/cs/send', { content, image })
  },

  getUnread() {
    return request.get('/api/admin/cs/unread')
  },

  getUnreadCount() {
    return request.get('/api/admin/cs/unread-count')
  },

  getMessagesByUser(userId) {
    return request.get(`/api/admin/cs/user/${userId}`)
  },

  reply(userId, content) {
    return request.post('/api/admin/cs/reply', { userId, content })
  },

  markRead(id) {
    return request.put(`/api/admin/cs/${id}/read`)
  }
}
