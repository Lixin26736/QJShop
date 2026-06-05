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
        path: 'orders',
        name: 'OrderList',
        component: () => import('@/views/client/OrderList.vue'),
        meta: { title: '我的订单', requiresAuth: true }
      },
      {
        path: 'order/:id',
        name: 'OrderDetail',
        component: () => import('@/views/client/OrderDetail.vue'),
        meta: { title: '订单详情', requiresAuth: true }
      },
      {
        path: 'address',
        name: 'AddressList',
        component: () => import('@/views/client/AddressList.vue'),
        meta: { title: '收货地址', requiresAuth: true }
      },
      {
        path: 'favorites',
        name: 'FavoriteList',
        component: () => import('@/views/client/FavoriteList.vue'),
        meta: { title: '我的收藏', requiresAuth: true }
      },
      {
        path: 'product/:id',
        name: 'ProductDetail',
        component: () => import('@/views/client/ProductDetail.vue'),
        meta: { title: '商品详情' }
      },
      {
        path: 'checkout',
        name: 'Checkout',
        component: () => import('@/views/client/Checkout.vue'),
        meta: { title: '确认订单', requiresAuth: true }
      },
      {
        path: 'settings',
        name: 'UserSettings',
        component: () => import('@/views/client/UserSettings.vue'),
        meta: { title: '账号设置', requiresAuth: true }
      },
      {
        path: 'cs',
        name: 'CustomerService',
        component: () => import('@/views/client/CustomerService.vue'),
        meta: { title: 'AI客服' }
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
        path: 'category',
        name: 'AdminCategory',
        component: () => import('@/views/admin/Category.vue'),
        meta: { title: '分类管理' }
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
      },
      {
        path: 'banner',
        name: 'AdminBanner',
        component: () => import('@/views/admin/Banner.vue'),
        meta: { title: 'Banner管理' }
      },
      {
        path: 'review',
        name: 'AdminReview',
        component: () => import('@/views/admin/Review.vue'),
        meta: { title: '评价管理' }
      },
      {
        path: 'cs',
        name: 'AdminCS',
        component: () => import('@/views/admin/CustomerService.vue'),
        meta: { title: '客服消息' }
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: () => import('@/views/admin/Settings.vue'),
        meta: { title: '系统设置' }
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
      if (to.meta.requiresAdmin) {
        next({ path: '/admin/login', query: { redirect: to.fullPath } })
      } else {
        next({ path: '/login', query: { redirect: to.fullPath } })
      }
      return
    }

    if (to.meta.requiresAdmin && !isAdmin) {
      next('/client/home')
      return
    }
  }

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
