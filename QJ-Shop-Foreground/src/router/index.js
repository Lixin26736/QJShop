import { createRouter, createWebHistory } from 'vue-router'
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
