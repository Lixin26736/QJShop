<template>
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
        background-color="#1e293b"
        text-color="#cbd5e1"
        active-text-color="#60a5fa"
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
        <el-menu-item index="/admin/category">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/banner">
          <el-icon><Picture /></el-icon>
          <span>Banner管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/review">
          <el-icon><ChatDotRound /></el-icon>
          <span>评价管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/cs">
          <el-icon><Service /></el-icon>
          <span>客服消息</span>
        </el-menu-item>
        <el-menu-item index="/admin/settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
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
        background-color="#1e293b"
        text-color="#cbd5e1"
        active-text-color="#60a5fa"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/category">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/product">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/banner">
          <el-icon><Picture /></el-icon>
          <span>Banner管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/review">
          <el-icon><ChatDotRound /></el-icon>
          <span>评价管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/cs">
          <el-icon><Service /></el-icon>
          <span>客服消息</span>
        </el-menu-item>
        <el-menu-item index="/admin/settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
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
import { DataAnalysis, User, Goods, Document, Menu, ArrowDown, Expand, Fold, Picture, ChatDotRound, Service, Setting } from '@element-plus/icons-vue'
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
  background-color: #1e293b;
  color: #fff;
  transition: width 0.3s;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1d4ed8, #2563eb);
}

.logo h2 { font-size: 16px; margin: 0; color: #fff; font-weight: 700; }

.el-header {
  background-color: var(--bg-card);
  box-shadow: var(--shadow-sm);
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
