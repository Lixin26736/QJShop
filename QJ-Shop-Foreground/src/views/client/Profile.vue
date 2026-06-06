<template>
  <div class="profile-page">
    <!-- 用户信息卡片 -->
    <div class="user-card" v-if="isLoggedIn">
      <div class="card-bg"></div>
      <div class="card-content">
        <div class="avatar-wrap">
          <img v-if="userInfo.avatar" :src="userInfo.avatar.startsWith('http')||userInfo.avatar.startsWith('data:') ? userInfo.avatar : (import.meta.env.VITE_API_BASE_URL||'')+userInfo.avatar" class="avatar-img" />
          <van-icon v-else name="manager" size="32" color="#fff" />
        </div>
        <div class="user-detail">
          <div class="user-name">{{ userInfo.nickname || userInfo.username }}</div>
          <div class="user-phone">{{ userInfo.phone || '未绑定手机号' }}</div>
        </div>
        <van-icon name="setting-o" size="20" color="#fff" @click="goToSettings" class="setting-icon" />
      </div>
      <!-- 订单统计 -->
      <div class="order-stats">
        <div class="stat-item" @click="goToOrders(-1)"><span class="stat-num">--</span><span class="stat-label">全部</span></div>
        <div class="stat-item" @click="goToOrders(0)"><span class="stat-num">--</span><span class="stat-label">待付款</span></div>
        <div class="stat-item" @click="goToOrders(1)"><span class="stat-num">--</span><span class="stat-label">待发货</span></div>
        <div class="stat-item" @click="goToOrders(2)"><span class="stat-num">--</span><span class="stat-label">待收货</span></div>
        <div class="stat-item" @click="goToOrders(3)"><span class="stat-num">--</span><span class="stat-label">已完成</span></div>
      </div>
    </div>

    <div class="user-card login-card" v-else @click="goToLogin">
      <div class="card-bg login-bg"></div>
      <div class="card-content">
        <div class="avatar-wrap">
          <van-icon name="user-circle-o" size="32" color="#fff" />
        </div>
        <div class="user-detail">
          <div class="user-name">点击登录</div>
          <div class="user-phone">登录后享受更多权益</div>
        </div>
        <van-icon name="arrow" size="18" color="#fff" />
      </div>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-section">
      <div class="menu-title">我的服务</div>
      <van-grid :column-num="4" :border="false" :gutter="8">
        <van-grid-item icon="orders-o" text="我的订单" @click="goToOrders(-1)" />
        <van-grid-item icon="location-o" text="收货地址" @click="goToAddress" />
        <van-grid-item icon="star-o" text="我的收藏" @click="goToFavorite" />
        <van-grid-item icon="service-o" text="联系客服" @click="$router.push('/client/cs')" />
      </van-grid>
    </div>

    <div class="menu-section">
      <div class="menu-title">其他</div>
      <van-cell-group inset>
        <van-cell title="账号设置" icon="setting-o" is-link @click="goToSettings" />
        <van-cell v-if="isLoggedIn" title="退出登录" icon="close" @click="handleLogout" />
      </van-cell-group>
    </div>
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

const goToLogin = () => router.push({ name: 'Login' })
const goToOrders = (status) => {
  if (!isLoggedIn.value) { showToast('请先登录'); return }
  router.push({ name: 'OrderList', query: status >= 0 ? { status } : {} })
}
const goToAddress = () => {
  if (!isLoggedIn.value) { showToast('请先登录'); return }
  router.push({ name: 'AddressList' })
}
const goToFavorite = () => {
  if (!isLoggedIn.value) { showToast('请先登录'); return }
  router.push({ name: 'FavoriteList' })
}
const goToSettings = () => {
  if (!isLoggedIn.value) { showToast('请先登录'); return }
  router.push('/client/settings')
}
const handleLogout = () => {
  showConfirmDialog({ title: '提示', message: '确定要退出登录吗?' })
    .then(() => { userStore.logout(); showToast('已退出登录') })
    .catch(() => {})
}
</script>

<style scoped>
.profile-page { background: var(--bg); min-height: 100vh; padding-bottom: 20px; }
.user-card { position: relative; margin: 16px; border-radius: var(--radius-lg); overflow: hidden; cursor: pointer; }
.card-bg { position: absolute; inset: 0; background: linear-gradient(135deg, #667eea, #764ba2); }
.login-bg { background: linear-gradient(135deg, #94a3b8, #64748b); }
.card-content { position: relative; display: flex; align-items: center; gap: 14px; padding: 24px 20px; }
.avatar-wrap { width: 56px; height: 56px; border-radius: 50%; background: rgba(255,255,255,0.25); display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.avatar-img { width: 56px; height: 56px; border-radius: 50%; object-fit: cover; border: 2px solid rgba(255,255,255,0.4); }
.user-detail { flex: 1; min-width: 0; }
.user-name { color: #fff; font-size: 18px; font-weight: 700; }
.user-phone { color: rgba(255,255,255,0.8); font-size: 13px; margin-top: 4px; }
.setting-icon { flex-shrink: 0; }
.order-stats { position: relative; display: flex; background: rgba(255,255,255,0.1); backdrop-filter: blur(4px); padding: 12px 0; }
.stat-item { flex: 1; text-align: center; color: #fff; cursor: pointer; display: flex; flex-direction: column; gap: 4px; }
.stat-num { font-size: 16px; font-weight: 700; }
.stat-label { font-size: 11px; opacity: 0.8; }
.menu-section { margin: 0 16px 12px; }
.menu-title { font-size: 14px; font-weight: 600; color: var(--text-secondary); margin-bottom: 8px; padding-left: 4px; }
:deep(.van-grid-item__content) { background: var(--bg-card); border-radius: var(--radius); padding: 12px 0; }
</style>
