# QJ-Shop-Foreground 项目检查文档\n`\n## 1. 项目基本信息\n`\n| 项目 | 值 |\n|------|------|\n| name | QJ-Shop-Foreground |\n| version | 0.0.0 |\n| type | module (ESM) |\n| 框架 | Vue 3.5.31 |\n| 构建工具 | Vite 8.0.3 |\n| UI库(管理端) | Element Plus 2.13.7 |\n| UI库(客户端) | Vant 4.9.24 |\n| 状态管理 | Pinia 3.0.4 |\n| 路由 | Vue Router 5.0.4 |\n| HTTP客户端 | Axios 1.15.0 |\n| Node版本要求 | ^20.19.0 || >=22.12.0 |\n`\n---\n`\n## 2. package.json\n`\n`json\n{
  "name": "QJ-Shop-Foreground",
  "version": "0.0.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "axios": "^1.15.0",
    "element-plus": "^2.13.7",
    "pinia": "^3.0.4",
    "vant": "^4.9.24",
    "vue": "^3.5.31",
    "vue-router": "^5.0.4"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^6.0.5",
    "terser": "^5.46.1",
    "vite": "^8.0.3",
    "vite-plugin-vue-devtools": "^8.1.1"
  },
  "engines": {
    "node": "^20.19.0 || >=22.12.0"
  }
}
\n`\n`\n---\n`\n## 3. vite.config.js\n`\n`javascript\nimport {fileURLToPath, URL} from 'node:url'
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
                              plugins: [
                                vue(),
                                vueDevTools(),
                              ],
                              resolve: {
                                alias: {
                                  '@': fileURLToPath(new URL('./src', import.meta.url))
                                },
                              },
                              server: {
                                host: '0.0.0.0', // 允许外部访问
                                port: 3000,
                                proxy: {
                                  '/api': {
                                    target: 'http://localhost:8080',
                                    changeOrigin: true,
                                    rewrite: (path) => path.replace(/^\/api/, '')
                                  }
                                }
                              },
                              build: {
                                minify: 'terser',
                                terserOptions: {
                                  compress: {
                                    drop_console: true,
                                    drop_debugger: true
                                  }
                                },
                                rollupOptions: {
                                  output: {
                                    // 将 manualChunks 改为函数形式
                                    manualChunks(id) {
                                      // element-plus 相关
                                      if (id.includes('element-plus')) {
                                        return 'element-plus'
                                      }
                                      // vant 相关
                                      if (id.includes('vant')) {
                                        return 'vant'
                                      }
                                      // vue 核心库
                                      if (id.includes('vue') || id.includes('vue-router') || id.includes('pinia')) {
                                        return 'vue-vendor'
                                      }
                                      // 其他 node_modules 依赖
                                      if (id.includes('node_modules')) {
                                        return 'vendor'
                                      }
                                    }
                                  }
                                },
                                chunkSizeWarningLimit: 1000
                              },
                              optimizeDeps: {
                                include: ['vue', 'vue-router', 'pinia', 'axios', 'element-plus', 'vant']
                              }
                            })\n`\n`\n---\n`\n## 4. .env 配置\n`\n`\n# API基础URL配置
VITE_API_BASE_URL=http://localhost:8080
\n`\n`\n---\n`\n## 5. index.html\n`\n`html\n<!DOCTYPE html>
<html lang="">
  <head>
    <meta charset="UTF-8">
    <link rel="icon" href="/favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vite App</title>
  </head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.js"></script>
  </body>
</html>
\n`\n`\n---\n`\n## 6. 所有源文件\n`\n### App.vue\n`\n`vue\n<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  width: 100%;
  min-height: 100vh;
}
</style>
\n`\n`\n### api\category.js\n`\n`javascript\nimport request from '@/utils/request'

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
\n`\n`\n### api\order.js\n`\n`javascript\nimport request from '@/utils/request'

export const orderApi = {
  createOrder(data) {
    return request.post('/api/admin/orders', data)
  },

  getOrderList(params) {
    return request.get('/api/admin/orders/page', { params })
  },

  getOrderDetail(id) {
    return request.get(`/api/admin/orders/${id}`)
  },

  cancelOrder(id) {
    return request.put(`/api/admin/orders/${id}/cancel`)
  },

  payOrder(id, data) {
    return request.put(`/api/admin/orders/${id}/pay`, data)
  }
}
\n`\n`\n### api\product.js\n`\n`javascript\nimport request from '@/utils/request'

export const productApi = {
  getProductList(params) {
    return request.get('/api/admin/products/page', { params })
  },

  getProductDetail(id) {
    return request.get(`/api/admin/products/${id}`)
  },

  getHotProducts() {
    return request.get('/api/admin/products/hot')
  },

  getNewProducts() {
    return request.get('/api/admin/products/new')
  },

  getProductsByCategory(categoryId, params) {
    return request.get(`/api/admin/products/category/${categoryId}`, { params })
  }
}
\n`\n`\n### api\user.js\n`\n`javascript\nimport request from '@/utils/request'

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
\n`\n`\n### assets\base.css\n`\n`css\n/* color palette from <https://github.com/vuejs/theme> */
:root {
  --vt-c-white: #ffffff;
  --vt-c-white-soft: #f8f8f8;
  --vt-c-white-mute: #f2f2f2;

  --vt-c-black: #181818;
  --vt-c-black-soft: #222222;
  --vt-c-black-mute: #282828;

  --vt-c-indigo: #2c3e50;

  --vt-c-divider-light-1: rgba(60, 60, 60, 0.29);
  --vt-c-divider-light-2: rgba(60, 60, 60, 0.12);
  --vt-c-divider-dark-1: rgba(84, 84, 84, 0.65);
  --vt-c-divider-dark-2: rgba(84, 84, 84, 0.48);

  --vt-c-text-light-1: var(--vt-c-indigo);
  --vt-c-text-light-2: rgba(60, 60, 60, 0.66);
  --vt-c-text-dark-1: var(--vt-c-white);
  --vt-c-text-dark-2: rgba(235, 235, 235, 0.64);
}

/* semantic color variables for this project */
:root {
  --color-background: var(--vt-c-white);
  --color-background-soft: var(--vt-c-white-soft);
  --color-background-mute: var(--vt-c-white-mute);

  --color-border: var(--vt-c-divider-light-2);
  --color-border-hover: var(--vt-c-divider-light-1);

  --color-heading: var(--vt-c-text-light-1);
  --color-text: var(--vt-c-text-light-1);

  --section-gap: 160px;
}

@media (prefers-color-scheme: dark) {
  :root {
    --color-background: var(--vt-c-black);
    --color-background-soft: var(--vt-c-black-soft);
    --color-background-mute: var(--vt-c-black-mute);

    --color-border: var(--vt-c-divider-dark-2);
    --color-border-hover: var(--vt-c-divider-dark-1);

    --color-heading: var(--vt-c-text-dark-1);
    --color-text: var(--vt-c-text-dark-2);
  }
}

*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  font-weight: normal;
}

body {
  min-height: 100vh;
  color: var(--color-text);
  background: var(--color-background);
  transition:
    color 0.5s,
    background-color 0.5s;
  line-height: 1.6;
  font-family:
    Inter,
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    Roboto,
    Oxygen,
    Ubuntu,
    Cantarell,
    'Fira Sans',
    'Droid Sans',
    'Helvetica Neue',
    sans-serif;
  font-size: 15px;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
\n`\n`\n### assets\main.css\n`\n`css\n@import './base.css';

#app {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem;
  font-weight: normal;
}

a,
.green {
  text-decoration: none;
  color: hsla(160, 100%, 37%, 1);
  transition: 0.4s;
  padding: 3px;
}

@media (hover: hover) {
  a:hover {
    background-color: hsla(160, 100%, 37%, 0.2);
  }
}

@media (min-width: 1024px) {
  body {
    display: flex;
    place-items: center;
  }

  #app {
    display: grid;
    grid-template-columns: 1fr 1fr;
    padding: 0 2rem;
  }
}
\n`\n`\n### assets\responsive.css\n`\n`css\n/* 响应式布局变量 */
:root {
  /* 断点 */
  --breakpoint-mobile: 768px;
  --breakpoint-tablet: 1024px;
  --breakpoint-desktop: 1200px;

  /* 间距 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;

  /* 字体大小 */
  --font-size-xs: 12px;
  --font-size-sm: 14px;
  --font-size-md: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 20px;
}

/* 性能优化：GPU加速 */
.gpu-accelerate {
  transform: translateZ(0);
  will-change: transform;
}

/* 响应式工具类 */
@media screen and (max-width: 768px) {
  .hide-mobile { display: none !important; }
  .show-mobile { display: block !important; }
}

@media screen and (min-width: 769px) and (max-width: 1024px) {
  .hide-tablet { display: none !important; }
  .show-tablet { display: block !important; }
}

@media screen and (min-width: 1025px) {
  .hide-desktop { display: none !important; }
  .show-desktop { display: block !important; }
}

/* 响应式容器 */
.container {
  width: 100%;
  padding: 0 var(--spacing-md);
  margin: 0 auto;
}

@media screen and (min-width: 769px) {
  .container {
    max-width: 750px;
  }
}

@media screen and (min-width: 1025px) {
  .container {
    max-width: 970px;
  }
}

@media screen and (min-width: 1200px) {
  .container {
    max-width: 1170px;
  }
}

/* 响应式网格 */
.grid {
  display: grid;
  gap: var(--spacing-md);
}

.grid-cols-1 { grid-template-columns: repeat(1, 1fr); }
.grid-cols-2 { grid-template-columns: repeat(2, 1fr); }
.grid-cols-3 { grid-template-columns: repeat(3, 1fr); }
.grid-cols-4 { grid-template-columns: repeat(4, 1fr); }

@media screen and (max-width: 768px) {
  .grid-cols-2, .grid-cols-3, .grid-cols-4 {
    grid-template-columns: repeat(1, 1fr);
  }
}

@media screen and (min-width: 769px) and (max-width: 1024px) {
  .grid-cols-3, .grid-cols-4 {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* 响应式弹性布局 */
.flex-responsive {
  display: flex;
  flex-wrap: wrap;
}

@media screen and (max-width: 768px) {
  .flex-responsive {
    flex-direction: column;
  }
}

/* 图片懒加载占位 */
img[loading="lazy"] {
  background: #f5f5f5;
}

/* 平滑滚动 */
@media (prefers-reduced-motion: no-preference) {
  html {
    scroll-behavior: smooth;
  }
}

/* 触摸优化 */
@media (hover: none) and (pointer: coarse) {
  button, a, input, select, textarea {
    min-height: 44px;
    min-width: 44px;
  }
}
\n`\n`\n### main.js\n`\n`javascript\nimport './assets/responsive.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './store'

import Vant from 'vant'
import 'vant/lib/index.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(pinia)
app.use(Vant)
app.use(ElementPlus)

app.mount('#app')
\n`\n`\n### router\index.js\n`\n`javascript\nimport { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    redirect: '/client'
  },
  // 客户端登录
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/client/Login.vue'),
    meta: { title: '登录' }
  },
  // 客户端注册
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/client/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/client',
    component: () => import('@/views/client/Layout.vue'),
    children: [
      {
        path: '',
        redirect: '/client/home'
      },
      {
        path: 'home',
        name: 'ClientHome',
        component: () => import('@/views/client/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'category',
        name: 'ClientCategory',
        component: () => import('@/views/client/Category.vue'),
        meta: { title: '分类' }
      },
      {
        path: 'cart',
        name: 'ClientCart',
        component: () => import('@/views/client/Cart.vue'),
        meta: { title: '购物车', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'ClientProfile',
        component: () => import('@/views/client/Profile.vue'),
        meta: { title: '个人信息', requiresAuth: true }
      },
      {
        path: 'settings',
        name: 'UserSettings',
        component: () => import('@/views/client/UserSettings.vue'),
        meta: { title: '账号设置', requiresAuth: true }
      }
    ]
  },
  // 管理员登录
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/Login.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('@/views/admin/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'product',
        name: 'AdminProduct',
        component: () => import('@/views/admin/Product.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: 'order',
        name: 'AdminOrder',
        component: () => import('@/views/admin/Order.vue'),
        meta: { title: '订单管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title + ' - QJ商城'
  }

  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  const isAdmin = userStore.userInfo?.role === 1

  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    if (!isLoggedIn) {
      // 未登录,跳转到登录页
      if (to.meta.requiresAdmin) {
        // 管理员页面,跳转到管理员登录
        next({
          path: '/admin/login',
          query: { redirect: to.fullPath }
        })
      } else {
        // 客户端页面,跳转到客户端登录
        next({
          path: '/login',
          query: { redirect: to.fullPath }
        })
      }
      return
    }

    // 检查是否需要管理员权限
    if (to.meta.requiresAdmin && !isAdmin) {
      // 不是管理员,跳转到客户端首页
      next('/client/home')
      return
    }
  }

  // 如果已登录,访问登录页时跳转到对应首页
  if (isLoggedIn) {
    if (to.path === '/login' || to.path === '/register') {
      next('/client/home')
      return
    }
    if (to.path === '/admin/login') {
      if (isAdmin) {
        next('/admin/dashboard')
      } else {
        next('/client/home')
      }
      return
    }
  }

  next()
})

export default router
\n`\n`\n### store\cart.js\n`\n`javascript\nimport { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref(JSON.parse(localStorage.getItem('cartItems') || '[]'))

  const cartCount = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.quantity, 0)
  })

  const cartTotal = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0)
  })

  const addToCart = (product) => {
    const existingItem = cartItems.value.find(item => item.id === product.id)
    if (existingItem) {
      existingItem.quantity += 1
    } else {
      cartItems.value.push({ ...product, quantity: 1 })
    }
    saveCart()
  }

  const removeFromCart = (productId) => {
    const index = cartItems.value.findIndex(item => item.id === productId)
    if (index > -1) {
      cartItems.value.splice(index, 1)
      saveCart()
    }
  }

  const updateQuantity = (productId, quantity) => {
    const item = cartItems.value.find(item => item.id === productId)
    if (item) {
      item.quantity = quantity
      saveCart()
    }
  }

  const clearCart = () => {
    cartItems.value = []
    saveCart()
  }

  const saveCart = () => {
    localStorage.setItem('cartItems', JSON.stringify(cartItems.value))
  }

  return {
    cartItems,
    cartCount,
    cartTotal,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart
  }
})
\n`\n`\n### store\index.js\n`\n`javascript\nimport { createPinia } from 'pinia'

const pinia = createPinia()

export default pinia
\n`\n`\n### store\user.js\n`\n`javascript\nimport { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const isLoggedIn = ref(!!token.value)

  const setToken = (newToken) => {
    token.value = newToken
    isLoggedIn.value = !!newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    if (info) {
      localStorage.setItem('userInfo', JSON.stringify(info))
    } else {
      localStorage.removeItem('userInfo')
    }
  }

  const logout = () => {
    setToken('')
    setUserInfo(null)
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    setToken,
    setUserInfo,
    logout
  }
})
\n`\n`\n### utils\envConfig.js\n`\n`javascript\n/**
 * 环境配置工具类
 * 用于管理不同环境下的API地址配置
 */

// 环境类型
const ENV_TYPE = {
  DEVELOPMENT: 'development',  // 本地开发
  EMULATOR: 'emulator',        // 模拟器
  REAL_DEVICE: 'real_device'   // 真机
}

// 当前环境 - 根据需要修改这个值
const CURRENT_ENV = ENV_TYPE.REAL_DEVICE

// 不同环境的配置
const ENV_CONFIG = {
  [ENV_TYPE.DEVELOPMENT]: {
    // 本地开发环境 - 使用localhost
    API_BASE_URL: 'http://localhost:3000',
    BACKEND_URL: 'http://localhost:8080'
  },
  [ENV_TYPE.EMULATOR]: {
    // 模拟器环境 - 使用VirtualBox Host-Only网络IP
    API_BASE_URL: 'http://192.168.56.1:3000',
    BACKEND_URL: 'http://192.168.56.1:8080'
  },
  [ENV_TYPE.REAL_DEVICE]: {
    // 真机环境 - 使用真实局域网IP
    // 请根据你的实际IP地址修改这里
    API_BASE_URL: 'http://192.168.3.191:3000',
    BACKEND_URL: 'http://192.168.3.191:8080'
  }
}

/**
 * 获取当前环境的API基础URL
 * @returns {string} API基础URL
 */
export function getApiBaseUrl() {
  return ENV_CONFIG[CURRENT_ENV].API_BASE_URL
}

/**
 * 获取当前环境的后端URL
 * @returns {string} 后端URL
 */
export function getBackendUrl() {
  return ENV_CONFIG[CURRENT_ENV].BACKEND_URL
}

/**
 * 获取当前环境类型
 * @returns {string} 环境类型
 */
export function getCurrentEnv() {
  return CURRENT_ENV
}

/**
 * 判断是否为开发环境
 * @returns {boolean}
 */
export function isDevelopment() {
  return CURRENT_ENV === ENV_TYPE.DEVELOPMENT
}

/**
 * 判断是否为模拟器环境
 * @returns {boolean}
 */
export function isEmulator() {
  return CURRENT_ENV === ENV_TYPE.EMULATOR
}

/**
 * 判断是否为真机环境
 * @returns {boolean}
 */
export function isRealDevice() {
  return CURRENT_ENV === ENV_TYPE.REAL_DEVICE
}

// 导出所有配置
export default {
  ENV_TYPE,
  CURRENT_ENV,
  ENV_CONFIG,
  getApiBaseUrl,
  getBackendUrl,
  getCurrentEnv,
  isDevelopment,
  isEmulator,
  isRealDevice
}
\n`\n`\n### utils\request.js\n`\n`javascript\nimport axios from 'axios'
import { useUserStore } from '@/store/user'
import { showToast } from 'vant'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    // 如果是blob类型(文件下载),直接返回数据
    if (response.config.responseType === 'blob') {
      return response.data
    }
    
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else {
      showToast(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          const userStore = useUserStore()
          userStore.logout()
          showToast('登录已过期,请重新登录')
          break
        case 403:
          showToast('没有权限访问')
          break
        case 404:
          showToast('请求资源不存在')
          break
        case 500:
          showToast('服务器错误')
          break
        default:
          showToast(error.response.data.message || '请求失败')
      }
    } else {
      showToast('网络连接失败')
    }
    return Promise.reject(error)
  }
)

export default request
\n`\n`\n### utils\responsive.js\n`\n`javascript\nimport { ref, onMounted, onUnmounted } from 'vue'

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
\n`\n`\n### views\admin\Dashboard.vue\n`\n`vue\n<template>
  <div class="dashboard">
    <el-row :gutter="gutter">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409eff">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount || 0 }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67c23a">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.productCount || 0 }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6a23c">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.orderCount || 0 }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f56c6c">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ stats.totalAmount || 0 }}</div>
              <div class="stat-label">销售总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="gutter" style="margin-top: 20px">
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>最近订单</span>
          </template>
          <el-table :data="recentOrders" style="width: 100%">
            <el-table-column prop="orderNo" label="订单号" :width="isMobile ? 120 : 180" />
            <el-table-column prop="totalAmount" label="金额" width="100">
              <template #default="scope">¥{{ scope.row.totalAmount }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>热销商品</span>
          </template>
          <el-table :data="hotProducts" style="width: 100%">
            <el-table-column prop="name" label="商品名称" />
            <el-table-column prop="sales" label="销量" width="80" />
            <el-table-column prop="price" label="价格" width="100">
              <template #default="scope">¥{{ scope.row.price }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { User, Goods, Document, Money } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useResponsive } from '@/utils/responsive'

const { isMobile, isTablet } = useResponsive()

const stats = ref({})
const recentOrders = ref([])
const hotProducts = ref([])

const gutter = computed(() => isMobile.value ? 10 : 20)

const loadDashboardData = async () => {
  try {
    const res = await request.get('/api/admin/dashboard/stats')
    stats.value = res || {}
    recentOrders.value = res.recentOrders || []
    hotProducts.value = res.hotProducts || []
  } catch (error) {
    console.error('加载仪表盘数据失败:', error)
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

@media screen and (max-width: 768px) {
  .dashboard {
    padding: 10px;
  }
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 30px;
}

@media screen and (max-width: 768px) {
  .stat-icon {
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

@media screen and (max-width: 768px) {
  .stat-value {
    font-size: 20px;
  }
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

@media screen and (max-width: 768px) {
  .stat-label {
    font-size: 12px;
  }
}
</style>
\n`\n`\n### views\admin\Layout.vue\n`\n`vue\n<template>
  <el-container class="admin-layout">
    <!-- 移动端抽屉菜单 -->
    <el-drawer
      v-if="isMobile"
      v-model="drawerVisible"
      direction="ltr"
      :size="200"
      title="QJ商城管理后台"
    >
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/product">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
      </el-menu>
    </el-drawer>

    <!-- PC/平板端侧边栏 -->
    <el-aside v-if="!isMobile" :width="isCollapse ? '64px' : sidebarWidth" class="admin-aside">
      <div class="logo">
        <h2 v-show="!isCollapse">QJ商城管理后台</h2>
        <h2 v-show="isCollapse">QJ</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        :collapse="isCollapse"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/product">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="header-content">
          <div class="header-left">
            <!-- 移动端菜单按钮 -->
            <el-icon v-if="isMobile" class="menu-btn" @click="drawerVisible = true">
              <Menu />
            </el-icon>
            <!-- PC/平板端折叠按钮 -->
            <el-icon v-else class="collapse-btn" @click="toggleCollapse">
              <Expand v-if="isCollapse" />
              <Fold v-else />
            </el-icon>
            <div class="breadcrumb" :class="{ 'hide-mobile': isMobile }">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </div>
          <div class="user-info">
            <el-dropdown>
              <span class="el-dropdown-link">
                <el-avatar :size="30" :src="userInfo.avatar" />
                <span v-if="!isMobile">{{ userInfo.nickname || '管理员' }}</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { DataAnalysis, User, Goods, Document, Menu, ArrowDown, Expand, Fold } from '@element-plus/icons-vue'
import { useResponsive } from '@/utils/responsive'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const { isMobile, isTablet } = useResponsive()

const isCollapse = ref(false)
const drawerVisible = ref(false)

const sidebarWidth = computed(() => isTablet.value ? '180px' : '200px')

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '')
const userInfo = computed(() => userStore.userInfo)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleMenuSelect = () => {
  drawerVisible.value = false
}

const goToProfile = () => {
  router.push('/client/profile')
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/client/home')
    })
    .catch(() => {})
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.admin-aside {
  background-color: #545c64;
  color: #fff;
  transition: width 0.3s;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #434a50;
}

.logo h2 {
  font-size: 16px;
  margin: 0;
  color: #fff;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.menu-btn,
.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s;
}

.menu-btn:hover,
.collapse-btn:hover {
  color: #409eff;
}

.breadcrumb {
  display: flex;
  align-items: center;
}

.hide-mobile {
  display: none !important;
}

.user-info {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

.el-main {
  background-color: #f0f2f5;
  overflow-y: auto;
}

@media screen and (max-width: 768px) {
  .el-header {
    padding: 0 10px;
  }
}
</style>
\n`\n`\n### views\admin\Login.vue\n`\n`vue\n<template>
  <div class="admin-login">
    <div class="login-container">
      <h1 class="title">QJ商城管理后台</h1>
      
      <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref(null)

const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      const res = await request.post('/api/auth/login', form.value)
      
      // 检查是否为管理员
      if (res.user.role !== 1) {
        ElMessage.error('用户名或密码错误')
        return
      }
      
      // 保存用户信息到store
      userStore.setToken(res.token)
      userStore.setUserInfo(res.user)
      
      ElMessage.success('登录成功')
      
      // 跳转到管理后台
      const redirect = route.query.redirect || '/admin/dashboard'
      router.push(redirect)
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.admin-login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
  color: #333;
}

.login-form {
  width: 100%;
}
</style>
\n`\n`\n### views\admin\Order.vue\n`\n`vue\n<template>
  <div class="order-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="success" @click="handleExport">导出Excel</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="订单号" clearable @clear="handleSearch" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select style="width:100px;" v-model="searchForm.status" placeholder="请选择" clearable @clear="handleSearch">
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="orderList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="payType" label="支付方式" width="100">
          <template #default="scope">
            {{ scope.row.payType === 1 ? '微信' : '支付宝' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleDeliver(scope.row)" v-if="scope.row.status === 1">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const orderList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  orderNo: '',
  status: null
})

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/orders/page', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        orderNo: searchForm.value.orderNo,
        status: searchForm.value.status
      }
    })
    orderList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

const handleReset = () => {
  searchForm.value = {
    orderNo: '',
    status: null
  }
  currentPage.value = 1
  loadOrders()
}

const handleExport = async () => {
  try {
    ElMessage.info('正在导出...')
    const res = await request.get('/api/admin/orders/export', {
      params: {
        orderNo: searchForm.value.orderNo,
        status: searchForm.value.status
      },
      responseType: 'blob'
    })

    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `订单数据_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const handleView = (row) => {
  ElMessage.info(`查看订单: ${row.orderNo}`)
}

const handleDeliver = (row) => {
  ElMessage.info(`发货订单: ${row.orderNo}`)
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
\n`\n`\n### views\admin\Product.vue\n`\n`vue\n<template>
  <div class="product-management">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" style="margin-bottom: 20px">
      <el-breadcrumb-item>
        <el-link @click="goToLevel(0)" :type="currentLevel === 0 ? 'primary' : 'default'">商品管理</el-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentLevel >= 1 && selectedFirstCategory">
        <el-link @click="goToLevel(1)" :type="currentLevel === 1 ? 'primary' : 'default'">{{ selectedFirstCategory.name }}</el-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentLevel >= 2 && selectedSecondCategory">
        <span>{{ selectedSecondCategory.name }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 一级分类列表 -->
    <el-card v-if="currentLevel === 0">
      <template #header>
        <div class="card-header">
          <span>一级分类</span>
          <el-button type="primary" size="small" @click="handleAddFirstCategory">添加一级分类</el-button>
        </div>
      </template>
      <el-table :data="firstCategories" style="width: 100%" v-loading="loading" @row-click="selectFirstCategory">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="scope">
            <el-icon v-if="scope.row.icon"><component :is="scope.row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click.stop="selectFirstCategory(scope.row)">查看子分类</el-button>
            <el-button size="small" @click.stop="handleEditFirstCategory(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDeleteFirstCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 二级分类列表 -->
    <el-card v-else-if="currentLevel === 1">
      <template #header>
        <div class="card-header">
          <span>{{ selectedFirstCategory?.name }} - 二级分类</span>
          <el-button type="primary" size="small" @click="handleAddSecondCategory">添加二级分类</el-button>
        </div>
      </template>
      <el-table :data="secondCategories" style="width: 100%" v-loading="loading" @row-click="selectSecondCategory">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="商品数量" width="100">
          <template #default="scope">
            <el-tag>{{ scope.row.productCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click.stop="selectSecondCategory(scope.row)">查看商品</el-button>
            <el-button size="small" @click.stop="handleEditSecondCategory(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDeleteSecondCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 商品列表 -->
    <el-card v-else-if="currentLevel === 2">
      <template #header>
        <div class="card-header">
          <span>{{ selectedSecondCategory?.name }} - 商品列表</span>
          <div class="header-actions">
            <el-button type="success" size="small" @click="handleExportProduct">导出Excel</el-button>
            <el-button type="primary" size="small" @click="handleAddProduct">添加商品</el-button>
          </div>
        </div>
      </template>

      <!-- 商品搜索表单 -->
      <el-form :inline="true" :model="productSearchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="productSearchForm.keyword" placeholder="商品名称" clearable @clear="handleProductSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="productSearchForm.status" placeholder="请选择" clearable @clear="handleProductSearch">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleProductSearch">查询</el-button>
          <el-button @click="handleProductReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="productList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" width="200" />
        <el-table-column prop="mainImage" label="主图" width="100">
          <template #default="scope">
            <el-image :src="scope.row.mainImage" style="width: 60px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEditProduct(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteProduct(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadProducts"
        @current-change="loadProducts"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const currentLevel = ref(0) // 0: 一级分类, 1: 二级分类, 2: 商品列表
const loading = ref(false)

// 一级分类
const firstCategories = ref([])
const selectedFirstCategory = ref(null)

// 二级分类
const secondCategories = ref([])
const selectedSecondCategory = ref(null)

// 商品
const productList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const productSearchForm = ref({
  keyword: '',
  status: null
})

// 加载一级分类
const loadFirstCategories = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/categories/first')
    firstCategories.value = res?.records || res || []
  } catch (error) {
    ElMessage.error('加载一级分类失败')
  } finally {
    loading.value = false
  }
}

// 加载二级分类
const loadSecondCategories = async (parentId) => {
  loading.value = true
  try {
    const res = await request.get(`/api/admin/categories/second/${parentId}`)
    secondCategories.value = res?.records || res || []
  } catch (error) {
    ElMessage.error('加载二级分类失败')
  } finally {
    loading.value = false
  }
}

// 加载商品
const loadProducts = async () => {
  if (!selectedSecondCategory.value) return
  loading.value = true
  try {
    const res = await request.get('/api/admin/products/page', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        categoryId: selectedSecondCategory.value.id,
        keyword: productSearchForm.value.keyword,
        status: productSearchForm.value.status
      }
    })
    productList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

// 商品搜索
const handleProductSearch = () => {
  currentPage.value = 1
  loadProducts()
}

// 商品搜索重置
const handleProductReset = () => {
  productSearchForm.value = {
    keyword: '',
    status: null
  }
  currentPage.value = 1
  loadProducts()
}

// 导出商品Excel
const handleExportProduct = async () => {
  if (!selectedSecondCategory.value) return
  try {
    ElMessage.info('正在导出...')
    const res = await request.get('/api/admin/products/export', {
      params: {
        categoryId: selectedSecondCategory.value.id,
        keyword: productSearchForm.value.keyword,
        status: productSearchForm.value.status
      },
      responseType: 'blob'
    })

    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `商品数据_${selectedSecondCategory.value.name}_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 选择一级分类
const selectFirstCategory = (row) => {
  selectedFirstCategory.value = row
  currentLevel.value = 1
  loadSecondCategories(row.id)
}

// 选择二级分类
const selectSecondCategory = (row) => {
  selectedSecondCategory.value = row
  currentLevel.value = 2
  currentPage.value = 1
  loadProducts()
}

// 导航层级切换
const goToLevel = (level) => {
  currentLevel.value = level
  if (level === 0) {
    selectedFirstCategory.value = null
    selectedSecondCategory.value = null
    loadFirstCategories()
  } else if (level === 1) {
    selectedSecondCategory.value = null
    if (selectedFirstCategory.value) {
      loadSecondCategories(selectedFirstCategory.value.id)
    }
  }
}

// 一级分类操作
const handleAddFirstCategory = () => {
  ElMessage.info('添加一级分类功能待实现')
}

const handleEditFirstCategory = (row) => {
  ElMessage.info(`编辑一级分类: ${row.name}`)
}

const handleDeleteFirstCategory = (row) => {
  ElMessageBox.confirm('确定要删除该一级分类吗?删除后其下的二级分类和商品也将被删除!', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/categories/${row.id}`)
        ElMessage.success('删除成功')
        await loadFirstCategories()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 二级分类操作
const handleAddSecondCategory = () => {
  ElMessage.info('添加二级分类功能待实现')
}

const handleEditSecondCategory = (row) => {
  ElMessage.info(`编辑二级分类: ${row.name}`)
}

const handleDeleteSecondCategory = (row) => {
  ElMessageBox.confirm('确定要删除该二级分类吗?删除后其下的商品也将被删除!', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/categories/${row.id}`)
        ElMessage.success('删除成功')
        await loadSecondCategories(selectedFirstCategory.value.id)
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 商品操作
const handleAddProduct = () => {
  ElMessage.info('添加商品功能待实现')
}

const handleEditProduct = (row) => {
  ElMessage.info(`编辑商品: ${row.name}`)
}

const handleDeleteProduct = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/products/${row.id}`)
        ElMessage.success('删除成功')
        loadProducts()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  loadFirstCategories()
})
</script>

<style scoped>
.product-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}

.el-table {
  cursor: pointer;
}

.el-table__row:hover {
  background-color: #f5f7fa;
}
</style>
\n`\n`\n### views\admin\User.vue\n`\n`vue\n<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div class="header-actions">
            <el-button type="success" @click="handleExport">导出Excel</el-button>
            <el-button type="primary" @click="handleAdd">添加用户</el-button>
          </div>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="用户名/昵称/手机号" clearable @clear="handleSearch" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select style="width:100px;" v-model="searchForm.role" placeholder="请选择" clearable @clear="handleSearch">
            <el-option label="管理员" :value="1" />
            <el-option label="普通用户" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select style="width:100px;" v-model="searchForm.status" placeholder="请选择" clearable @clear="handleSearch">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="userList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.role === 1 ? 'danger' : 'success'">
              {{ scope.row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadUsers"
        @current-change="loadUsers"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const userList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  keyword: '',
  role: null,
  status: null
})

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/users/page', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        keyword: searchForm.value.keyword,
        role: searchForm.value.role,
        status: searchForm.value.status
      }
    })
    userList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

const handleReset = () => {
  searchForm.value = {
    keyword: '',
    role: null,
    status: null
  }
  currentPage.value = 1
  loadUsers()
}

const handleExport = async () => {
  try {
    ElMessage.info('正在导出...')
    const res = await request.get('/api/admin/users/export', {
      params: {
        keyword: searchForm.value.keyword,
        role: searchForm.value.role,
        status: searchForm.value.status
      },
      responseType: 'blob'
    })
    
    // 创建下载链接
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `用户数据_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleAdd = () => {
  ElMessage.info('添加用户功能待实现')
}

const handleEdit = (row) => {
  ElMessage.info(`编辑用户: ${row.username}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/users/${row.id}`)
        ElMessage.success('删除成功')
        loadUsers()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}
</style>
\n`\n`\n### views\client\Cart.vue\n`\n`vue\n<template>
  <div class="cart">
    <van-nav-bar title="购物车" />

    <div v-if="cartItems.length === 0" class="empty-cart">
      <van-empty description="购物车是空的" />
      <van-button type="primary" @click="goToHome">去逛逛</van-button>
    </div>

    <div v-else class="cart-content">
      <van-checkbox-group v-model="checkedItems">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <van-checkbox :name="item.id" />
          <div class="item-image">
            <img :src="item.mainImage" :alt="item.name" />
          </div>
          <div class="item-info">
            <div class="item-name">{{ item.name }}</div>
            <div class="item-price">¥{{ item.price }}</div>
            <van-stepper v-model="item.quantity" min="1" @change="updateQuantity(item.id, item.quantity)" />
          </div>
          <van-icon name="delete" class="delete-icon" @click="removeFromCart(item.id)" />
        </div>
      </van-checkbox-group>

      <van-submit-bar :price="totalPrice * 100" button-text="提交订单" @submit="onSubmit">
        <van-checkbox v-model="allChecked" @click="toggleAll">全选</van-checkbox>
      </van-submit-bar>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/store/cart'
import { showToast } from 'vant'

const router = useRouter()
const cartStore = useCartStore()

const cartItems = computed(() => cartStore.cartItems)
const checkedItems = ref([])

const allChecked = computed({
  get: () => checkedItems.value.length === cartItems.value.length && cartItems.value.length > 0,
  set: () => {}
})

const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => checkedItems.value.includes(item.id))
    .reduce((total, item) => total + item.price * item.quantity, 0)
})

const toggleAll = () => {
  if (allChecked.value) {
    checkedItems.value = []
  } else {
    checkedItems.value = cartItems.value.map(item => item.id)
  }
}

const updateQuantity = (productId, quantity) => {
  cartStore.updateQuantity(productId, quantity)
}

const removeFromCart = (productId) => {
  cartStore.removeFromCart(productId)
  checkedItems.value = checkedItems.value.filter(id => id !== productId)
  showToast('已移除')
}

const onSubmit = () => {
  if (checkedItems.value.length === 0) {
    showToast('请选择商品')
    return
  }
  router.push({ name: 'OrderCreate', query: { items: checkedItems.value.join(',') } })
}

const goToHome = () => {
  router.push('/client/home')
}
</script>

<style scoped>
.cart {
  background: #f7f8fa;
  min-height: 100vh;
}

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
}

.cart-content {
  padding-bottom: 100px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: #fff;
  margin-bottom: 10px;
}

.item-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.item-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.delete-icon {
  color: #999;
  font-size: 20px;
  cursor: pointer;
}
</style>
\n`\n`\n### views\client\Category.vue\n`\n`vue\n<template>
  <div class="category">
    <van-nav-bar title="商品分类" />

    <div class="category-container">
      <!-- 一级分类侧边栏 -->
      <van-sidebar v-model="activeFirstCategory" @change="onFirstCategoryChange">
        <van-sidebar-item v-for="item in firstCategories" :key="item.id" :title="item.name" />
      </van-sidebar>

      <!-- 右侧内容区 -->
      <div class="content-area">
        <!-- 二级分类标签 -->
        <div v-if="secondCategories.length > 0" class="second-category-tabs">
          <van-tabs v-model:active="activeSecondCategory" @change="onSecondCategoryChange">
            <van-tab v-for="item in secondCategories" :key="item.id" :title="item.name" />
          </van-tabs>
        </div>

        <!-- 商品列表 -->
        <div class="product-list">
          <div v-if="products.length === 0" class="empty-tip">
            <van-empty description="暂无商品" />
          </div>
          <van-grid v-else :column-num="2" :gutter="10">
            <van-grid-item v-for="product in products" :key="product.id">
              <div class="product-card" @click="goToProduct(product.id)">
                <img :src="product.mainImage" :alt="product.name" loading="lazy" />
                <div class="product-info">
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-price">¥{{ product.price }}</div>
                </div>
              </div>
            </van-grid-item>
          </van-grid>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

const activeFirstCategory = ref(0)
const activeSecondCategory = ref(0)
const firstCategories = ref([])
const secondCategories = ref([])
const products = ref([])

// 加载一级分类
const loadFirstCategories = async () => {
  try {
    const res = await request.get('/api/admin/categories/first')
    console.log('一级分类数据:', res)
    firstCategories.value = res?.records || res || []
    if (firstCategories.value.length > 0) {
      const queryId = route.query.id
      if (queryId) {
        // 查找对应的一级分类
        const index = firstCategories.value.findIndex(c => c.id === Number(queryId))
        if (index > -1) {
          activeFirstCategory.value = index
        }
      }
      loadSecondCategories(firstCategories.value[activeFirstCategory.value].id)
    }
  } catch (error) {
    console.error('加载一级分类失败:', error)
  }
}

// 加载二级分类
const loadSecondCategories = async (parentId) => {
  try {
    const res = await request.get(`/api/admin/categories/second/${parentId}`)
    console.log('二级分类数据:', res)
    secondCategories.value = res?.records || res || []
    if (secondCategories.value.length > 0) {
      activeSecondCategory.value = 0
      loadProducts(secondCategories.value[0].id)
    } else {
      // 如果没有二级分类,直接加载一级分类下的商品
      loadProducts(parentId)
    }
  } catch (error) {
    console.error('加载二级分类失败:', error)
  }
}

// 加载商品
const loadProducts = async (categoryId) => {
  try {
    const res = await request.get('/api/admin/products/page', {
      params: {
        pageNum: 1,
        pageSize: 100,
        categoryId: categoryId
      }
    })
    console.log('商品数据:', res)
    products.value = res?.records || res || []
  } catch (error) {
    console.error('加载商品失败:', error)
  }
}

// 一级分类切换
const onFirstCategoryChange = (index) => {
  if (firstCategories.value[index]) {
    loadSecondCategories(firstCategories.value[index].id)
  }
}

// 二级分类切换
const onSecondCategoryChange = (index) => {
  if (secondCategories.value[index]) {
    loadProducts(secondCategories.value[index].id)
  }
}

const goToProduct = (productId) => {
  router.push({ name: 'ProductDetail', params: { id: productId } })
}

watch(() => route.query.id, (newId) => {
  if (newId && firstCategories.value.length > 0) {
    const index = firstCategories.value.findIndex(c => c.id === Number(newId))
    if (index > -1) {
      activeFirstCategory.value = index
      loadSecondCategories(firstCategories.value[index].id)
    }
  }
})

onMounted(() => {
  loadFirstCategories()
})
</script>

<style scoped>
.category {
  background: #f7f8fa;
  min-height: 100vh;
}

.category-container {
  display: flex;
  height: calc(100vh - 96px);
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.second-category-tabs {
  background: #fff;
  border-bottom: 1px solid #eee;
}

.product-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background: #fff;
}

.empty-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.product-card {
  cursor: pointer;
}

.product-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info {
  padding: 5px;
}

.product-name {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
}
</style>
\n`\n`\n### views\client\Home.vue\n`\n`vue\n<template>
  <div class="home">
    <!-- 移动端导航栏 -->
    <van-nav-bar v-if="isMobile" title="QJ商城" />

    <!-- Banner轮播 -->
    <van-swipe class="banner" :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="item in banners" :key="item.id">
        <img :src="item.image" :alt="item.title" loading="lazy" />
      </van-swipe-item>
    </van-swipe>

    <!-- 分类导航 -->
    <van-grid :column-num="categoryCols" class="category-nav">
      <van-grid-item v-for="item in categories" :key="item.id" :icon="item.icon" :text="item.name" @click="goToCategory(item.id)" />
    </van-grid>

    <!-- 热门商品 -->
    <div class="section">
      <div class="section-title">
        <van-icon name="fire-o" color="#ff5722" />
        <span>热门商品</span>
      </div>
      <div class="product-grid" :style="{ gridTemplateColumns: `repeat(${productCols}, 1fr)` }">
        <div v-for="product in hotProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="product.mainImage" :alt="product.name" loading="lazy" />
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">¥{{ product.price }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 新品推荐 -->
    <div class="section">
      <div class="section-title">
        <van-icon name="new-o" color="#1989fa" />
        <span>新品推荐</span>
      </div>
      <div class="product-grid" :style="{ gridTemplateColumns: `repeat(${productCols}, 1fr)` }">
        <div v-for="product in newProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="product.mainImage" :alt="product.name" loading="lazy" />
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">¥{{ product.price }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { productApi } from '@/api/product'
import { categoryApi } from '@/api/category'
import { useResponsive } from '@/utils/responsive'

const router = useRouter()
const { isMobile, isTablet, isDesktop } = useResponsive()

const banners = ref([
  { id: 1, image: 'https://via.placeholder.com/375x200', title: 'Banner 1' },
  { id: 2, image: 'https://via.placeholder.com/375x200', title: 'Banner 2' }
])

const categories = ref([])
const hotProducts = ref([])
const newProducts = ref([])

const categoryCols = computed(() => {
  if (isMobile.value) return 4
  if (isTablet.value) return 6
  return 8
})

const productCols = computed(() => {
  if (isMobile.value) return 2
  if (isTablet.value) return 3
  return 4
})

const loadData = async () => {
  try {
    const [categoryRes, hotRes, newRes] = await Promise.all([
      categoryApi.getCategoryList(),
      productApi.getHotProducts(),
      productApi.getNewProducts()
    ])
    categories.value = categoryRes || []
    hotProducts.value = hotRes || []
    newProducts.value = newRes || []
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const goToCategory = (categoryId) => {
  router.push({ path: '/client/category', query: { id: categoryId } })
}

const goToProduct = (productId) => {
  router.push({ name: 'ProductDetail', params: { id: productId } })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home {
  background: #f7f8fa;
  min-height: 100vh;
}

.banner img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

@media screen and (min-width: 769px) {
  .banner img {
    height: 300px;
  }
}

@media screen and (min-width: 1025px) {
  .banner img {
    height: 400px;
  }
}

.category-nav {
  margin: 10px 0;
}

.section {
  margin: 10px;
  background: #fff;
  border-radius: 8px;
  padding: 10px;
}

@media screen and (min-width: 769px) {
  .section {
    margin: 20px auto;
    padding: 20px;
    max-width: 1200px;
  }
}

.section-title {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

@media screen and (min-width: 769px) {
  .section-title {
    font-size: 18px;
    margin-bottom: 15px;
  }
}

.product-grid {
  display: grid;
  gap: 10px;
}

.product-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

@media screen and (min-width: 769px) {
  .product-card img {
    height: 180px;
  }
}

@media screen and (min-width: 1025px) {
  .product-card img {
    height: 200px;
  }
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 5px;
}

.product-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
}
</style>
\n`\n`\n### views\client\Layout.vue\n`\n`vue\n<template>
  <div class="client-layout">
    <!-- PC端顶部导航 -->
    <header v-if="!isMobile" class="client-header">
      <div class="header-container">
        <div class="logo">QJ商城</div>
        <nav class="nav-menu">
          <router-link to="/client/home" class="nav-item" :class="{ active: $route.path === '/client/home' }">首页</router-link>
          <router-link to="/client/category" class="nav-item" :class="{ active: $route.path === '/client/category' }">分类</router-link>
          <router-link to="/client/cart" class="nav-item" :class="{ active: $route.path === '/client/cart' }">
            购物车
            <span v-if="cartCount" class="badge">{{ cartCount }}</span>
          </router-link>
          <router-link to="/client/profile" class="nav-item" :class="{ active: $route.path === '/client/profile' }">我的</router-link>
        </nav>
      </div>
    </header>
    
    <main class="client-main" :class="{ 'with-header': !isMobile }">
      <router-view />
    </main>
    
    <!-- 移动端底部导航 -->
    <van-tabbar v-if="isMobile" v-model="active" route>
      <van-tabbar-item to="/client/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/client/category" icon="apps-o">分类</van-tabbar-item>
      <van-tabbar-item to="/client/cart" icon="shopping-cart-o" :badge="cartCount">购物车</van-tabbar-item>
      <van-tabbar-item to="/client/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/store/cart'
import { useResponsive } from '@/utils/responsive'

const route = useRoute()
const cartStore = useCartStore()
const { isMobile } = useResponsive()

const active = ref(0)
const cartCount = computed(() => cartStore.cartCount)
</script>

<style scoped>
.client-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.client-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #1989fa;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-item {
  text-decoration: none;
  color: #333;
  font-size: 16px;
  position: relative;
  padding: 8px 0;
  transition: color 0.3s;
}

.nav-item:hover,
.nav-item.active {
  color: #1989fa;
}

.nav-item .badge {
  position: absolute;
  top: 0;
  right: -10px;
  background: #ff5722;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

.client-main {
  flex: 1;
}

.client-main.with-header {
  padding-top: 0;
}

@media screen and (max-width: 768px) {
  .client-main {
    padding-bottom: 50px;
  }
}
</style>
\n`\n`\n### views\client\Login.vue\n`\n`vue\n<template>
  <div class="login">
    <van-nav-bar title="登录" />

    <div class="login-form">
      <van-form @submit="handleLogin">
        <van-cell-group inset>
          <van-field
            v-model="form.username"
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
          />
          <van-field
            v-model="form.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
        </van-cell-group>

        <div class="form-actions">
          <van-button round block type="primary" native-type="submit" :loading="loading">
            登录
          </van-button>
          <van-button round block plain @click="goToRegister">
            没有账号?去注册
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await request.post('/api/auth/login', form.value)
    
    // 保存用户信息到store
    userStore.setToken(res.token)
    userStore.setUserInfo(res.user)
    
    showToast('登录成功')
    
    // 跳转到之前的页面或首页
    const redirect = route.query.redirect || '/client/home'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
    showToast(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login {
  min-height: 100vh;
  background: #f7f8fa;
}

.login-form {
  padding: 20px;
}

.form-actions {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
\n`\n`\n### views\client\Profile.vue\n`\n`vue\n<template>
  <div class="profile">
    <van-nav-bar title="个人中心" />

    <div class="user-info" v-if="isLoggedIn">
      <div class="avatar">
        <img :src="avatarUrl" alt="头像" />
      </div>
      <div class="info">
        <div class="nickname">{{ userInfo.nickname || '未设置昵称' }}</div>
        <div class="phone">{{ userInfo.phone || '未绑定手机' }}</div>
      </div>
    </div>

    <div class="user-info" v-else @click="goToLogin">
      <div class="avatar">
        <van-icon name="user-circle-o" size="40" />
      </div>
      <div class="info">
        <div class="login-text">点击登录</div>
      </div>
    </div>

    <van-cell-group inset class="menu-group">
      <van-cell title="我的订单" icon="orders-o" is-link @click="goToOrders" />
      <van-cell title="收货地址" icon="location-o" is-link @click="goToAddress" />
      <van-cell title="我的收藏" icon="star-o" is-link @click="goToFavorite" />
      <van-cell title="账号设置" icon="setting-o" is-link @click="goToSettings" />
    </van-cell-group>

    <van-cell-group inset class="menu-group" v-if="isLoggedIn">
      <van-cell title="退出登录" icon="close" @click="handleLogout" />
    </van-cell-group>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { showConfirmDialog, showToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = computed(() => userStore.userInfo)

// 计算头像URL，确保正确显示
const avatarUrl = computed(() => {
  const avatar = userInfo.value.avatar
  if (!avatar) {
    return 'https://via.placeholder.com/80'
  }
  // 如果是base64格式或完整的http/https URL，直接返回
  if (avatar.startsWith('data:') || avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  // 如果是相对路径，需要拼接服务器地址
  // 假设服务器地址存储在环境变量或配置中
  const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
  return `${baseUrl}${avatar}`
})

const goToLogin = () => {
  router.push({ name: 'Login' })
}

const goToOrders = () => {
  if (!isLoggedIn.value) {
    showToast('请先登录')
    return
  }
  router.push({ name: 'OrderList' })
}

const goToAddress = () => {
  if (!isLoggedIn.value) {
    showToast('请先登录')
    return
  }
  router.push({ name: 'AddressList' })
}

const goToFavorite = () => {
  if (!isLoggedIn.value) {
    showToast('请先登录')
    return
  }
  router.push({ name: 'FavoriteList' })
}

const goToSettings = () => {
  if (!isLoggedIn.value) {
    showToast('请先登录')
    return
  }
  router.push('/client/settings')
}

const handleLogout = () => {
  showConfirmDialog({
    title: '提示',
    message: '确定要退出登录吗?'
  })
    .then(() => {
      userStore.logout()
      showToast('已退出登录')
    })
    .catch(() => {})
}
</script>

<style scoped>
.profile {
  background: #f7f8fa;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  cursor: pointer;
}

.avatar img {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
}

.info {
  flex: 1;
}

.nickname {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.phone {
  font-size: 14px;
  opacity: 0.8;
}

.login-text {
  font-size: 16px;
}

.menu-group {
  margin: 10px 0;
}
</style>
\n`\n`\n### views\client\Register.vue\n`\n`vue\n<template>
  <div class="register">
    <van-nav-bar title="注册" />

    <div class="register-form">
      <van-form @submit="handleRegister">
        <van-cell-group inset>
          <van-field
            v-model="form.username"
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
          />
          <van-field
            v-model="form.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
          <van-field
            v-model="form.confirmPassword"
            type="password"
            name="confirmPassword"
            label="确认密码"
            placeholder="请再次输入密码"
            :rules="[
              { required: true, message: '请确认密码' },
              { validator: validatePassword, message: '两次密码不一致' }
            ]"
          />
          <van-field
            v-model="form.nickname"
            name="nickname"
            label="昵称"
            placeholder="请输入昵称(可选)"
          />
          <van-field
            v-model="form.phone"
            name="phone"
            label="手机号"
            placeholder="请输入手机号"
            :rules="[{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }]"
          />
        </van-cell-group>

        <div class="form-actions">
          <van-button round block type="primary" native-type="submit" :loading="loading">
            注册
          </van-button>
          <van-button round block plain @click="goToLogin">
            已有账号?去登录
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '@/utils/request'

const router = useRouter()

const loading = ref(false)
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  phone: ''
})

const validatePassword = () => {
  return form.value.password === form.value.confirmPassword
}

const handleRegister = async () => {
  loading.value = true
  try {
    await request.post('/api/auth/register', {
      username: form.value.username,
      password: form.value.password,
      nickname: form.value.nickname || form.value.username,
      phone: form.value.phone
    })
    
    showToast('注册成功')
    router.push('/login')
  } catch (error) {
    showToast(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register {
  min-height: 100vh;
  background: #f7f8fa;
}

.register-form {
  padding: 20px;
}

.form-actions {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
\n`\n`\n### views\client\UserSettings.vue\n`\n`vue\n<template>
  <div class="user-settings">
    <van-nav-bar title="账号设置" left-arrow @click-left="goBack" />

    <!-- 头像上传 -->
    <van-cell-group inset class="settings-group">
      <van-cell title="头像" center>
        <template #right-icon>
          <div class="avatar-upload">
            <img :src="form.avatar || 'https://via.placeholder.com/60'" alt="头像" class="avatar-preview" />
            <input
              type="file"
              accept="image/*"
              @change="handleAvatarChange"
              ref="avatarInput"
              style="display: none"
            />
            <van-button size="small" @click="triggerAvatarUpload">更换</van-button>
          </div>
        </template>
      </van-cell>
    </van-cell-group>

    <!-- 基本信息 -->
    <van-cell-group inset class="settings-group">
      <van-field
        v-model="form.nickname"
        label="昵称"
        placeholder="请输入昵称"
        clearable
      />
      <van-field
        v-model="form.phone"
        label="手机号"
        placeholder="请输入手机号"
        clearable
        type="tel"
      />
      <van-field
        v-model="form.email"
        label="邮箱"
        placeholder="请输入邮箱"
        clearable
        type="email"
      />
      <van-cell title="性别" is-link @click="showGenderPicker = true">
        {{ getGenderText(form.gender) }}
      </van-cell>
      <van-cell title="生日" is-link @click="showBirthdayPicker = true">
        {{ form.birthday || '请选择' }}
      </van-cell>
    </van-cell-group>

    <!-- 性别选择器 -->
    <van-action-sheet
      v-model:show="showGenderPicker"
      :actions="genderActions"
      @select="onGenderSelect"
    />

    <!-- 生日选择器 -->
    <van-popup v-model:show="showBirthdayPicker" position="bottom">
      <van-date-picker
        v-model="selectedDate"
        title="选择生日"
        @confirm="onBirthdayConfirm"
        @cancel="showBirthdayPicker = false"
      />
    </van-popup>

    <!-- 保存按钮 -->
    <div class="save-button">
      <van-button type="primary" block :loading="loading" @click="handleSave">
        保存修改
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()
const avatarInput = ref(null)

const loading = ref(false)
const showGenderPicker = ref(false)
const showBirthdayPicker = ref(false)

const form = ref({
  avatar: '',
  nickname: '',
  phone: '',
  email: '',
  gender: null,
  birthday: ''
})

const selectedDate = ref(['2020', '01', '01'])

const genderActions = [
  { name: '男', value: 1 },
  { name: '女', value: 0 }
]

const getGenderText = (gender) => {
  if (gender === 1) return '男'
  if (gender === 0) return '女'
  return '请选择'
}

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const res = await request.get(`/api/user/profile`)
    form.value = {
      avatar: res.avatar || '',
      nickname: res.nickname || '',
      phone: res.phone || '',
      email: res.email || '',
      gender: res.gender,
      birthday: res.birthday || ''
    }
    
    // 设置生日选择器初始值
    if (res.birthday) {
      const parts = res.birthday.split('-')
      selectedDate.value = parts
    }
    
    // 同步更新用户store中的信息
    userStore.setUserInfo(res)
  } catch (error) {
    console.error('加载用户信息失败:', error)
    showToast('加载用户信息失败')
  }
}

// 触发头像上传
const triggerAvatarUpload = () => {
  avatarInput.value?.click()
}

// 处理头像变更
const handleAvatarChange = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  
  // 检查文件大小(限制2MB)
  if (file.size > 2 * 1024 * 1024) {
    showToast('图片大小不能超过2MB')
    return
  }
  
  // 转换为base64
  const reader = new FileReader()
  reader.onload = (e) => {
    form.value.avatar = e.target?.result
  }
  reader.readAsDataURL(file)
}

// 性别选择
const onGenderSelect = (action) => {
  form.value.gender = action.value
  showGenderPicker.value = false
}

// 生日确认
const onBirthdayConfirm = ({ selectedValues }) => {
  form.value.birthday = selectedValues.join('-')
  showBirthdayPicker.value = false
}

// 保存修改
const handleSave = async () => {
  loading.value = true
  try {
    await request.put('/api/user/profile', form.value)
    
    // 更新本地用户信息
    userStore.setUserInfo({
      ...userStore.userInfo,
      ...form.value
    })
    
    showToast('保存成功')
    router.back()
  } catch (error) {
    showToast(error.message || '保存失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.user-settings {
  background: #f7f8fa;
  min-height: 100vh;
}

.settings-group {
  margin: 10px 0;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar-preview {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.save-button {
  padding: 20px;
}
</style>
\n`\n