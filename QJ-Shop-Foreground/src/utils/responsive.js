import { ref, onMounted, onUnmounted } from 'vue'

export const useResponsive = () => {
  const isMobile = ref(false)
  const isTablet = ref(false)
  const isDesktop = ref(false)
  const screenWidth = ref(0)

  const checkScreen = () => {
    screenWidth.value = window.innerWidth
    isMobile.value = window.innerWidth <= 768
    isTablet.value = window.innerWidth > 768 && window.innerWidth <= 1024
    isDesktop.value = window.innerWidth > 1024
  }

  // 防抖优化
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

  return {
    isMobile,
    isTablet,
    isDesktop,
    screenWidth
  }
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
