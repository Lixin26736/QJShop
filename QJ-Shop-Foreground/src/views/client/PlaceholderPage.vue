<template>
  <div class="placeholder-page">
    <van-nav-bar :title="pageTitle" left-arrow @click-left="goBack" />
    <div class="content">
      <div class="title">{{ pageTitle }}</div>
      <div class="desc">{{ description }}</div>
      <van-button type="primary" round block @click="goBack">返回</van-button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const pageTitle = computed(() => route.meta.title || '页面')
const description = computed(() => {
  const id = route.params.id
  if (id) {
    return `当前页面已接收商品 ID：${id}`
  }
  return '该功能页面已预留路由，后续可以在这里补充完整业务。'
})

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.placeholder-page {
  min-height: 100vh;
  background: #f7f8fa;
}

.content {
  padding: 24px 16px;
}

.title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
}

.desc {
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}
</style>
