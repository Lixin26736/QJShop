<template>
  <div class="cart">
    <van-nav-bar title="购物车" />

    <div v-if="cartItems.length === 0" class="empty-cart">
      <van-empty description="购物车是空的" />
      <van-button type="primary" @click="goToHome">去逛逛</van-button>
    </div>

    <div v-else class="cart-content">
      <van-checkbox-group v-model="checkedItems">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <van-checkbox :name="item.id" />
          <div class="item-image">
            <img :src="item.mainImage" :alt="item.name" />
          </div>
          <div class="item-info">
            <div class="item-name">{{ item.name }}</div>
            <div class="item-price">¥{{ item.price }}</div>
            <van-stepper v-model="item.quantity" min="1" @change="updateQuantity(item.id, item.quantity)" />
          </div>
          <van-icon name="delete" class="delete-icon" @click="removeFromCart(item.id)" />
        </div>
      </van-checkbox-group>

      <van-submit-bar :price="totalPrice * 100" button-text="提交订单" @submit="onSubmit">
        <van-checkbox v-model="allChecked" @click="toggleAll">全选</van-checkbox>
      </van-submit-bar>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/store/cart'
import { showToast } from 'vant'

const router = useRouter()
const cartStore = useCartStore()

const cartItems = computed(() => cartStore.cartItems)
const checkedItems = ref([])

const allChecked = computed({
  get: () => checkedItems.value.length === cartItems.value.length && cartItems.value.length > 0,
  set: () => {}
})

const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => checkedItems.value.includes(item.id))
    .reduce((total, item) => total + item.price * item.quantity, 0)
})

const toggleAll = () => {
  if (allChecked.value) {
    checkedItems.value = []
  } else {
    checkedItems.value = cartItems.value.map(item => item.id)
  }
}

const updateQuantity = (productId, quantity) => {
  cartStore.updateQuantity(productId, quantity)
}

const removeFromCart = (productId) => {
  cartStore.removeFromCart(productId)
  checkedItems.value = checkedItems.value.filter(id => id !== productId)
  showToast('已移除')
}

const onSubmit = () => {
  if (checkedItems.value.length === 0) {
    showToast('请选择商品')
    return
  }
  router.push({ name: 'OrderCreate', query: { items: checkedItems.value.join(',') } })
}

const goToHome = () => {
  router.push('/client/home')
}
</script>

<style scoped>
.cart {
  background: #f7f8fa;
  min-height: 100vh;
}

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
}

.cart-content {
  padding-bottom: 100px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: #fff;
  margin-bottom: 10px;
}

.item-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.item-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.delete-icon {
  color: #999;
  font-size: 20px;
  cursor: pointer;
}
</style>
