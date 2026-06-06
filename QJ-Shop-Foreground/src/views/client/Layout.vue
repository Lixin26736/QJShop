<template>
  <div class="client-layout">
    <!-- PC端顶部导航 -->
    <header v-if="!isMobile" class="client-header">
      <div class="header-container">
        <router-link to="/client/home" class="logo">
          <span class="logo-icon">QJ</span>
          <span class="logo-text">QJ商城</span>
        </router-link>
        <nav class="nav-menu">
          <router-link to="/client/home" class="nav-item" :class="{ active: $route.path === '/client/home' }">
            <span class="nav-icon">🏠</span> 首页
          </router-link>
          <router-link to="/client/category" class="nav-item" :class="{ active: $route.path === '/client/category' }">
            <span class="nav-icon">📂</span> 分类
          </router-link>
          <router-link to="/client/cart" class="nav-item" :class="{ active: $route.path === '/client/cart' }">
            <span class="nav-icon">🛒</span> 购物车
            <span v-if="cartCount" class="badge">{{ cartCount }}</span>
          </router-link>
          <router-link to="/client/cs" class="nav-item">
            <span class="nav-icon">💬</span> 客服
          </router-link>
          <router-link to="/client/profile" class="nav-item" :class="{ active: $route.path === '/client/profile' }">
            <span class="nav-icon">👤</span> 我的
          </router-link>
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
  background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 10px;
}
.logo-icon {
  background: rgba(255,255,255,0.2);
  color: #fff;
  width: 40px; height: 40px;
  border-radius: var(--radius);
  display: flex; align-items: center; justify-content: center;
  font-weight: 800; font-size: 16px;
}
.logo-text { color: #fff; font-size: 20px; font-weight: 700; letter-spacing: 1px; }

.nav-menu { display: flex; gap: 8px; }
.nav-item {
  text-decoration: none;
  color: rgba(255,255,255,0.85);
  font-size: 14px;
  padding: 8px 16px;
  border-radius: var(--radius);
  transition: all 0.2s;
  position: relative;
  display: flex; align-items: center; gap: 4px;
}
.nav-icon { font-size: 16px; }
.nav-item:hover, .nav-item.active { color: #fff; background: rgba(255,255,255,0.15); }
.nav-item .badge {
  position: absolute;
  top: 2px; right: 4px;
  background: var(--danger);
  color: #fff;
  font-size: 11px;
  padding: 1px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

.client-main { flex: 1; }
.client-main.with-header { padding-top: 0; }

@media screen and (max-width: 768px) {
  .client-main { padding-bottom: 50px; }
}
</style>
