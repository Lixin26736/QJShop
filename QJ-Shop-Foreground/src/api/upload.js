import request from '@/utils/request'
import axios from 'axios'

const API_BASE = '' // request.js already adds /api prefix, but for upload we need multipart

export const uploadApi = {
  // 上传单张图片 (返回 { url, name })
  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    const token = localStorage.getItem('token')
    const headers = token ? { 'Authorization': `Bearer ${token}` } : {}
    return axios.post('/api/upload/image', formData, { headers }).then(res => {
      if (res.data.code === 200) return res.data.data
      throw new Error(res.data.message || '上传失败')
    })
  },

  // 上传多张图片
  uploadImages(files) {
    const formData = new FormData()
    files.forEach(file => formData.append('files', file))
    const token = localStorage.getItem('token')
    const headers = token ? { 'Authorization': `Bearer ${token}` } : {}
    return axios.post('/api/upload/images', formData, { headers }).then(res => {
      if (res.data.code === 200) return res.data.data
      throw new Error(res.data.message || '上传失败')
    })
  },

  // 获取完整图片URL
  getImageUrl(path) {
    if (!path) return ''
    if (path.startsWith('http') || path.startsWith('data:')) return path
    const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
    return `${baseUrl}${path}`
  }
}
