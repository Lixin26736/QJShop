<template>
  <div class="checkout">
    <van-nav-bar title="确认订单" left-arrow @click-left="$router.back()" />

    <!-- 收货地址 -->
    <div class="section">
      <div class="section-title">收货地址</div>
      <div v-if="!selectedAddress" class="no-address" @click="showAddressPicker = true">
        <van-icon name="add-o" /> 请选择收货地址
      </div>
      <div v-else class="address-card" @click="showAddressPicker = true">
        <div class="address-info">
          <div class="receiver">{{ selectedAddress.receiverName }} {{ selectedAddress.receiverPhone }}</div>
          <div class="address-detail">{{ selectedAddress.province }} {{ selectedAddress.city }} {{ selectedAddress.district }} {{ selectedAddress.detailAddress }}</div>
        </div>
        <van-icon name="arrow" />
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="section">
      <div class="section-title">商品信息</div>
      <div v-for="item in checkoutItems" :key="item.id" class="checkout-item">
        <img :src="getImage(item)" class="item-image" @error="e => e.target.src = getPlaceholder(item.name, item.id, 80, 80)" />
        <div class="item-info">
          <div class="item-name">{{ item.name }}</div>
          <div class="item-spec" v-if="item.specInfo">{{ item.specInfo }}</div>
          <div class="item-price-row">
            <span class="item-price">¥{{ item.price }}</span>
            <span class="item-quantity">x{{ item.quantity }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 订单备注 -->
    <div class="section">
      <van-field v-model="remark" label="订单备注" placeholder="选填" />
    </div>

    <!-- 提交 -->
    <van-submit-bar :price="totalPrice * 100" button-text="提交订单" @submit="handleSubmit" :loading="submitting">
      <span>合计: </span>
    </van-submit-bar>

    <!-- 地址选择弹窗 -->
    <van-popup v-model:show="showAddressPicker" position="bottom" round :style="{ height: '60%' }">
      <div class="address-picker">
        <h3>选择地址</h3>
        <div v-if="addresses.length === 0" class="no-address-tip">
          <van-empty description="暂无地址" />
          <van-button type="primary" size="small" @click="goAddAddress">添加地址</van-button>
        </div>
        <van-radio-group v-model="selectedAddressId">
          <div v-for="addr in addresses" :key="addr.id" class="address-option" @click="selectedAddressId = addr.id">
            <van-radio :name="addr.id">
              <div>{{ addr.receiverName }} {{ addr.receiverPhone }}</div>
              <div class="addr-text">{{ addr.province }} {{ addr.city }} {{ addr.district }} {{ addr.detailAddress }}</div>
            </van-radio>
          </div>
        </van-radio-group>
        <div class="picker-actions">
          <van-button type="primary" block @click="confirmAddress">确定</van-button>
        </div>
      </div>
    </van-popup>

    <!-- 支付成功弹窗 -->
    <van-dialog v-model:show="showPayDialog" title="支付" show-cancel-button :confirmButtonText="'去支付'" @confirm="confirmPay">
      <div class="pay-dialog">
        <p>订单已创建，订单号: {{ createdOrderNo }}</p>
        <p>金额: ¥{{ createdAmount }}</p>
        <van-radio-group v-model="payType" direction="horizontal">
          <van-radio :name="1">微信支付</van-radio>
          <van-radio :name="2">支付宝</van-radio>
        </van-radio-group>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '@/store/cart'
import { addressApi } from '@/api/address'
import { orderApi } from '@/api/order'
import { getImageUrl, getPlaceholder } from '@/utils/image'
import { showToast } from 'vant'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

const addresses = ref([])
const selectedAddressId = ref(null)
const showAddressPicker = ref(false)
const remark = ref('')
const submitting = ref(false)
const showPayDialog = ref(false)
const createdOrderNo = ref('')
const createdAmount = ref('')
const payType = ref(1)

const checkoutItems = computed(() => {
  const itemIds = route.query.items
  if (itemIds) {
    const ids = itemIds.split(',').map(Number)
    return cartStore.cartItems.filter(item => ids.includes(item.id))
  }
  return cartStore.cartItems
})

const selectedAddress = computed(() => {
  if (selectedAddressId.value) return addresses.value.find(a => a.id === selectedAddressId.value)
  const def = addresses.value.find(a => a.isDefault === 1)
  return def || (addresses.value.length > 0 ? addresses.value[0] : null)
})

const totalPrice = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const getImage = (item) => getImageUrl(item.mainImage) || getPlaceholder(item.name, item.id, 80, 80)

const loadAddresses = async () => {
  try {
    addresses.value = await addressApi.list()
    const def = addresses.value.find(a => a.isDefault === 1)
    if (def) selectedAddressId.value = def.id
  } catch (e) { /* ignore */ }
}

const confirmAddress = () => {
  showAddressPicker.value = false
}

const goAddAddress = () => {
  showAddressPicker.value = false
  router.push('/client/address')
}

const handleSubmit = async () => {
  if (!selectedAddress.value) {
    showToast('请选择收货地址')
    return
  }
  if (checkoutItems.value.length === 0) {
    showToast('订单商品不能为空')
    return
  }
  submitting.value = true
  try {
    const items = checkoutItems.value.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }))
    const res = await orderApi.createOrder({
      addressId: selectedAddress.value.id,
      items,
      remark: remark.value
    })
    createdOrderNo.value = res.orderNo
    createdAmount.value = res.totalAmount
    showPayDialog.value = true

    // 清空已购买的商品
    checkoutItems.value.forEach(item => cartStore.removeFromCart(item.id))
  } catch (e) {
    showToast(e.message || '下单失败')
  } finally {
    submitting.value = false
  }
}

const confirmPay = async () => {
  // 模拟支付
  showToast('支付成功!')
  router.push('/client/orders')
}

onMounted(() => {
  loadAddresses()
  // 如果没有选中商品，从购物车全选
  if (checkoutItems.value.length === 0) {
    router.push('/client/cart')
  }
})
</script>

<style scoped>
.checkout { background: #f7f8fa; min-height: 100vh; padding-bottom: 50px; }
.section { background: #fff; margin: 10px 0; padding: 12px; }
.section-title { font-size: 16px; font-weight: bold; margin-bottom: 10px; }
.no-address { text-align: center; padding: 20px; color: #999; cursor: pointer; }
.address-card { display: flex; justify-content: space-between; align-items: center; cursor: pointer; }
.address-info { flex: 1; }
.receiver { font-size: 15px; font-weight: bold; }
.address-detail { font-size: 13px; color: #666; margin-top: 4px; }
.checkout-item { display: flex; gap: 10px; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.item-image { width: 80px; height: 80px; border-radius: 4px; object-fit: cover; }
.item-info { flex: 1; }
.item-name { font-size: 14px; }
.item-spec { font-size: 12px; color: #999; }
.item-price-row { display: flex; justify-content: space-between; margin-top: 8px; }
.item-price { color: #ff5722; font-weight: bold; }
.item-quantity { color: #999; }
.address-picker { padding: 20px; }
.address-picker h3 { text-align: center; margin-bottom: 15px; }
.no-address-tip { text-align: center; padding: 20px; }
.address-option { padding: 10px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.addr-text { font-size: 12px; color: #666; margin-top: 4px; }
.picker-actions { margin-top: 20px; }
.pay-dialog { padding: 20px; text-align: center; }
.pay-dialog p { margin-bottom: 10px; }
</style>
