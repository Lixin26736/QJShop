import { ref } from 'vue'

const API_BASE = import.meta.env.VITE_API_BASE_URL || ''

// 调色板 - 用于生成占位图背景色
const COLORS = [
  ['#667eea', '#764ba2'], ['#f093fb', '#f5576c'], ['#4facfe', '#00f2fe'],
  ['#43e97b', '#38f9d7'], ['#fa709a', '#fee140'], ['#a18cd1', '#fbc2eb'],
  ['#fad0c4', '#ffd1ff'], ['#ffecd2', '#fcb69f'], ['#ff9a9e', '#fecfef'],
  ['#a1c4fd', '#c2e9fb'], ['#d4fc79', '#96e6a1'], ['#84fab0', '#8fd3f4']
]

/**
 * 获取完整图片URL
 */
export function getImageUrl(path) {
  if (!path || path === 'null' || path === 'NULL' || path === 'undefined') return ''
  if (path.startsWith('http') || path.startsWith('data:')) return path
  return `${API_BASE}${path}`
}

/**
 * 生成占位图SVG (data URI)
 */
export function getPlaceholder(name, id, width = 300, height = 200) {
  const idx = ((id || 0) * 7 + (name || '').length * 13) % COLORS.length
  const [c1, c2] = COLORS[idx]
  const initial = (name || '商').charAt(0)
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}">
  <defs><linearGradient id="g" x1="0%" y1="0%" x2="100%" y2="100%">
    <stop offset="0%" style="stop-color:${c1}"/><stop offset="100%" style="stop-color:${c2}"/>
  </linearGradient></defs>
  <rect width="${width}" height="${height}" fill="url(#g)"/>
  <text x="50%" y="45%" text-anchor="middle" fill="white" font-size="${Math.min(width, height) * 0.15}" font-family="sans-serif" opacity="0.9">${initial}</text>
  <text x="50%" y="68%" text-anchor="middle" fill="white" font-size="${Math.min(width, height) * 0.065}" font-family="sans-serif" opacity="0.75">QJ商城</text>
</svg>`
  return 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svg)))
}

/**
 * 图片加载失败处理
 */
export function useImageFallback(id, name) {
  const imgError = ref(false)
  const imgSrc = ref('')

  const handleError = () => {
    imgError.value = true
    imgSrc.value = getPlaceholder(name, id)
  }

  const setImage = (src) => {
    const url = getImageUrl(src)
    if (!url) {
      imgSrc.value = getPlaceholder(name, id)
      imgError.value = true
    } else {
      imgSrc.value = url
      imgError.value = false
    }
  }

  return { imgSrc, imgError, handleError, setImage }
}
