<template>
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
