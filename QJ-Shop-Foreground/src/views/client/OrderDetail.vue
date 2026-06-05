<template>
  <div class="order-detail">
    <van-nav-bar title="订单详情" left-arrow @click-left="$router.back()" />

    <div v-if="order" class="detail-content">
      <!-- 状态 -->
      <div class="status-bar">
        <div class="status-text">{{ statusText(order.status) }}</div>
      </div>

      <!-- 商品列表 -->
      <div class="section">
        <div class="section-title">商品信息</div>
        <div v-for="item in items" :key="item.id" class="order-item">
          <img :src="getImage(item.productImage)" class="item-image" />
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-spec" v-if="item.specInfo">{{ item.specInfo }}</div>
            <div class="item-price-row">
              <span class="item-price">¥{{ item.price }} x{{ item.quantity }}</span>
              <span class="item-total">¥{{ item.totalPrice }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="section">
        <div class="section-title">订单信息</div>
        <van-cell-group inset>
          <van-cell title="订单编号" :value="order.orderNo" />
          <van-cell title="下单时间" :value="formatDate(order.createTime)" />
          <van-cell title="支付方式" :value="order.payType === 1 ? '微信' : '支付宝'" />
          <van-cell title="订单金额">
            ¥{{ order.totalAmount }}
          </van-cell>
          <van-cell title="运费">¥{{ order.freight || 0 }}</van-cell>
          <van-cell title="实付金额">
            <span class="pay-amount">¥{{ order.payAmount }}</span>
          </van-cell>
          <van-cell title="备注" :value="order.remark || '无'" />
        </van-cell-group>
      </div>

      <!-- 操作按钮 -->
      <div class="actions">
        <van-button v-if="order.status === 0" type="danger" block @click="handleCancel">取消订单</van-button>
        <van-button v-if="order.status === 0" type="primary" block @click="handlePay">去支付</van-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '@/api/order'
import { uploadApi } from '@/api/upload'
import { showToast, showConfirmDialog } from 'vant'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const items = ref([])

const loadDetail = async () => {
  try {
    const res = await orderApi.getOrderDetail(route.params.id)
    order.value = res.order
    items.value = res.items || []
  } catch (e) { showToast('加载失败') }
}

const handleCancel = () => {
  showConfirmDialog({ title: '提示', message: '确定取消该订单?' })
    .then(async () => {
      await orderApi.cancelOrder(order.value.id)
      showToast('已取消')
      loadDetail()
    })
}

const handlePay = async () => {
  await orderApi.payOrder(order.value.id)
  showToast('支付成功')
  loadDetail()
}

const getImage = (img) => uploadApi.getImageUrl(img || 'https://via.placeholder.com/80')

const statusText = (s) => ({ 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消' }[s] || '未知')

const formatDate = (d) => {
  if (!d) return ''
  const dt = new Date(d)
  return `${dt.getFullYear()}-${String(dt.getMonth() + 1).padStart(2, '0')}-${String(dt.getDate()).padStart(2, '0')} ${String(dt.getHours()).padStart(2, '0')}:${String(dt.getMinutes()).padStart(2, '0')}`
}

onMounted(() => loadDetail())
</script>

<style scoped>
.order-detail { background: #f7f8fa; min-height: 100vh; }
.status-bar { background: linear-gradient(135deg, #667eea, #764ba2); padding: 20px; color: #fff; }
.status-text { font-size: 18px; font-weight: bold; }
.section { background: #fff; margin: 10px 0; padding: 12px; }
.section-title { font-size: 16px; font-weight: bold; margin-bottom: 10px; }
.order-item { display: flex; gap: 10px; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.item-image { width: 80px; height: 80px; border-radius: 4px; object-fit: cover; }
.item-info { flex: 1; }
.item-name { font-size: 14px; }
.item-spec { font-size: 12px; color: #999; }
.item-price-row { display: flex; justify-content: space-between; margin-top: 8px; }
.item-price { color: #666; }
.item-total { color: #ff5722; font-weight: bold; }
.pay-amount { color: #ff5722; font-weight: bold; font-size: 16px; }
.actions { padding: 20px; display: flex; flex-direction: column; gap: 10px; }
</style>
