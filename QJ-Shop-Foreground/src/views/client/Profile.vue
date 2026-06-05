<template>
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
