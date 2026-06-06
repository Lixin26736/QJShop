import { ref, onMounted, onUnmounted } from 'vue'

// 设备检测（非响应式，一次性判断）
const ua = navigator.userAgent || ''
const isMobileUA = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini|Mobile|CriOS/i.test(ua)
const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0 || navigator.msMaxTouchPoints > 0

function detectMobile() {
  const w = window.innerWidth
  // 1. UA明确标识为移动设备 → 移动端
  if (isMobileUA) return true
  // 2. 有触摸屏且宽度 < 1024 → 移动端(平板或手机)
  if (hasTouch && w < 1024) return true
  // 3. 无触摸且宽度 < 600 → 小窗口,仍按移动端
  if (!hasTouch && w < 600) return true
  return false
}

function detectTablet() {
  const w = window.innerWidth
  // UA为iPad或平板且宽度在600-1200之间
  if (/iPad|Tablet|PlayBook/i.test(ua)) return true
  if (hasTouch && w >= 600 && w < 1200) return true
  return false
}

export const useResponsive = () => {
  const isMobile = ref(false)
  const isTablet = ref(false)
  const isDesktop = ref(false)
  const screenWidth = ref(0)

  const checkScreen = () => {
    screenWidth.value = window.innerWidth
    isMobile.value = detectMobile()
    isTablet.value = detectTablet()
    isDesktop.value = !isMobile.value && !isTablet.value
  }

  let resizeTimer = null
  const debouncedCheckScreen = () => {
    if (resizeTimer) clearTimeout(resizeTimer)
    resizeTimer = setTimeout(checkScreen, 100)
  }

  onMounted(() => {
    checkScreen()
    window.addEventListener('resize', debouncedCheckScreen, { passive: true })
  })

  onUnmounted(() => {
    if (resizeTimer) clearTimeout(resizeTimer)
    window.removeEventListener('resize', debouncedCheckScreen)
  })

  return { isMobile, isTablet, isDesktop, screenWidth }
}

export const getGridCols = (screenWidth) => {
  if (screenWidth <= 768) return 1
  if (screenWidth <= 1024) return 2
  return 4
}

export const getImageSize = (screenWidth) => {
  if (screenWidth <= 768) return { width: 150, height: 150 }
  if (screenWidth <= 1024) return { width: 180, height: 180 }
  return { width: 200, height: 200 }
}
