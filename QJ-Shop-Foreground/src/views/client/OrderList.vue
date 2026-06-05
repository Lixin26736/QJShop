<template>
  <div class="order-list">
    <van-nav-bar title="我的订单" left-arrow @click-left="$router.back()" />

    <van-tabs v-model:active="activeTab" @change="loadOrders">
      <van-tab title="全部" :name="-1" />
      <van-tab title="待付款" :name="0" />
      <van-tab title="待发货" :name="1" />
      <van-tab title="待收货" :name="2" />
      <van-tab title="已完成" :name="3" />
    </van-tabs>

    <div class="order-content">
      <van-empty v-if="orders.length === 0 && !loading" description="暂无订单" />
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="loadOrders">
          <div v-for="order in orders" :key="order.id" class="order-card" @click="goDetail(order.id)">
            <div class="order-header">
              <span class="order-no">订单号: {{ order.orderNo }}</span>
              <van-tag :type="statusType(order.status)">{{ statusText(order.status) }}</van-tag>
            </div>
            <div class="order-info">
              <div class="order-amount">¥{{ order.totalAmount }}</div>
              <div class="order-time">{{ formatDate(order.createTime) }}</div>
            </div>
            <div class="order-actions" @click.stop>
              <van-button size="small" plain @click="goDetail(order.id)">查看详情</van-button>
              <van-button size="small" type="danger" v-if="order.status === 0" @click="handleCancel(order.id)">取消</van-button>
              <van-button size="small" type="primary" v-if="order.status === 0" @click="handlePay(order.id)">去支付</van-button>
              <van-button size="small" type="primary" v-if="order.status === 2" @click="handleConfirm(order.id)">确认收货</van-button>
            </div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { orderApi } from '@/api/order'
import { showToast, showConfirmDialog } from 'vant'

const router = useRouter()
const orders = ref([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const activeTab = ref(-1)
let pageNum = 1

const loadOrders = async () => {
  if (refreshing.value) {
    pageNum = 1
    finished.value = false
  }
  loading.value = true
  try {
    const params = { pageNum, pageSize: 10 }
    if (activeTab.value !== -1) params.status = activeTab.value
    const res = await orderApi.getOrderList(params)
    const records = res?.records || []
    if (refreshing.value) {
      orders.value = records
    } else {
      orders.value.push(...records)
    }
    if (records.length < 10) finished.value = true
    pageNum++
  } catch (e) {
    console.error('加载订单失败:', e)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

const onRefresh = () => {
  refreshing.value = true
  pageNum = 1
  loadOrders()
}

const goDetail = (id) => {
  router.push(`/client/order/${id}`)
}

const handleCancel = (id) => {
  showConfirmDialog({ title: '提示', message: '确定取消该订单?' })
    .then(async () => {
      await orderApi.cancelOrder(id)
      showToast('已取消')
      onRefresh()
    })
}

const handlePay = async (id) => {
  await orderApi.payOrder(id)
  showToast('支付成功')
  onRefresh()
}

const handleConfirm = (id) => {
  showConfirmDialog({ title: '提示', message: '确认已收到商品?' })
    .then(async () => {
      await orderApi.payOrder(id) // 客户端暂用同一个方法
      showToast('已确认收货')
      onRefresh()
    })
}

const statusType = (s) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'success', 4: 'danger' }[s] || 'info')
const statusText = (s) => ({ 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消' }[s] || '未知')

const formatDate = (d) => {
  if (!d) return ''
  const dt = new Date(d)
  return `${dt.getFullYear()}-${String(dt.getMonth() + 1).padStart(2, '0')}-${String(dt.getDate()).padStart(2, '0')} ${String(dt.getHours()).padStart(2, '0')}:${String(dt.getMinutes()).padStart(2, '0')}`
}
</script>

<style scoped>
.order-list { background: #f7f8fa; min-height: 100vh; }
.order-content { padding: 10px; }
.order-card { background: #fff; border-radius: 8px; padding: 12px; margin-bottom: 10px; cursor: pointer; }
.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.order-no { font-size: 13px; color: #666; }
.order-info { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.order-amount { font-size: 18px; font-weight: bold; color: #ff5722; }
.order-time { font-size: 12px; color: #999; }
.order-actions { display: flex; gap: 8px; justify-content: flex-end; }
</style>
