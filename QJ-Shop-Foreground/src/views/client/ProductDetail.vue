<template>
  <div class="product-detail">
    <van-nav-bar title="商品详情" left-arrow @click-left="$router.back()" />

    <!-- 商品图片轮播 -->
    <van-swipe class="product-swipe" :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="(img, idx) in images" :key="idx">
        <img :src="getImageUrl(img)" :alt="product.name" />
      </van-swipe-item>
    </van-swipe>

    <!-- 商品基本信息 -->
    <div class="product-info">
      <div class="product-name">{{ product.name }}</div>
      <div class="product-subtitle" v-if="product.subtitle">{{ product.subtitle }}</div>
      <div class="product-price-row">
        <span class="price">¥{{ product.price }}</span>
        <span class="original-price" v-if="product.originalPrice > product.price">¥{{ product.originalPrice }}</span>
        <span class="sales">已售 {{ product.sales || 0 }}</span>
      </div>
      <div class="rating-row" v-if="avgRating > 0">
        <van-rate v-model="avgRating" readonly :size="14" />
        <span class="review-count">({{ reviewCount }}条评价)</span>
      </div>
    </div>

    <!-- 规格选择 -->
    <van-cell-group inset class="spec-group" v-if="specs.length > 0">
      <van-cell title="规格" is-link @click="showSpec = true">
        {{ selectedSpec ? selectedSpec.specName + ': ' + selectedSpec.specValue : '请选择规格' }}
      </van-cell>
    </van-cell-group>

    <!-- 商品描述 -->
    <div class="section">
      <div class="section-title">商品描述</div>
      <div class="description" v-html="product.description || '暂无描述'"></div>
    </div>

    <!-- 商品详情 -->
    <div class="section" v-if="product.detailContent">
      <div class="section-title">商品详情</div>
      <div class="detail-content" v-html="product.detailContent"></div>
    </div>

    <!-- 商品评价 -->
    <div class="section">
      <div class="section-title">
        <span>商品评价 ({{ reviewCount }})</span>
        <van-button size="small" plain type="primary" @click="goToReviews">查看全部</van-button>
      </div>
      <div v-if="reviews.length === 0" class="no-reviews">暂无评价</div>
      <div v-else class="review-list">
        <div v-for="review in reviews.slice(0, 3)" :key="review.id" class="review-item">
          <div class="review-header">
            <van-rate v-model="review.rating" readonly :size="12" />
            <span class="review-time">{{ formatDate(review.createTime) }}</span>
          </div>
          <div class="review-content">{{ review.content }}</div>
          <div class="review-reply" v-if="review.reply">客服回复: {{ review.reply }}</div>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <van-goods-action>
      <van-goods-action-icon icon="chat-o" text="客服" @click="goToCS" />
      <van-goods-action-icon icon="star-o" text="收藏" @click="toggleFavorite" :color="isFavorited ? '#ff5722' : ''" />
      <van-goods-action-icon icon="cart-o" text="购物车" @click="$router.push('/client/cart')" :badge="cartCount" />
      <van-goods-action-button type="warning" text="加入购物车" @click="addToCart" />
      <van-goods-action-button type="danger" text="立即购买" @click="buyNow" />
    </van-goods-action>

    <!-- 规格选择弹窗 -->
    <van-popup v-model:show="showSpec" position="bottom" round>
      <div class="spec-popup">
        <div class="spec-header">
          <img :src="getImageUrl(product.mainImage)" class="spec-image" />
          <div>
            <div class="spec-price">¥{{ selectedSpec ? selectedSpec.price || product.price : product.price }}</div>
            <div class="spec-stock">库存: {{ selectedSpec ? selectedSpec.stock : product.stock }}</div>
          </div>
        </div>
        <div class="spec-list">
          <div v-for="spec in specs" :key="spec.id"
               class="spec-item" :class="{ active: selectedSpec?.id === spec.id }"
               @click="selectedSpec = spec">
            {{ spec.specName }}: {{ spec.specValue }}
            <span v-if="spec.price">(¥{{ spec.price }})</span>
          </div>
        </div>
        <div class="spec-footer">
          <van-button type="primary" block @click="showSpec = false">确定</van-button>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi } from '@/api/product'
import { reviewApi } from '@/api/review'
import { favoriteApi } from '@/api/favorite'
import { uploadApi } from '@/api/upload'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { showToast } from 'vant'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const product = ref({})
const specs = ref([])
const avgRating = ref(0)
const reviewCount = ref(0)
const reviews = ref([])
const selectedSpec = ref(null)
const isFavorited = ref(false)
const showSpec = ref(false)

const cartCount = computed(() => cartStore.cartCount)

const images = computed(() => {
  const imgs = []
  if (product.value.mainImage) imgs.push(product.value.mainImage)
  if (product.value.detailImages) {
    product.value.detailImages.split(',').filter(Boolean).forEach(img => imgs.push(img))
  }
  return imgs.length > 0 ? imgs : ['https://via.placeholder.com/375x375']
})

const getImageUrl = (path) => uploadApi.getImageUrl(path)

const loadData = async () => {
  const id = route.params.id
  try {
    const res = await productApi.getProductDetail(id)
    product.value = res.product || {}
    specs.value = res.specs || []
    avgRating.value = res.avgRating || 0
    reviewCount.value = res.reviewCount || 0
  } catch (error) {
    console.error('加载商品详情失败:', error)
  }

  // 加载评价
  try {
    const reviewRes = await reviewApi.listByProduct(id, { pageNum: 1, pageSize: 3 })
    reviews.value = reviewRes?.records || []
  } catch (e) { /* ignore */ }

  // 检查是否已收藏
  if (userStore.isLoggedIn) {
    try {
      const favRes = await favoriteApi.check(id)
      isFavorited.value = !!favRes
    } catch (e) { /* ignore */ }
  }
}

const toggleFavorite = async () => {
  if (!userStore.isLoggedIn) {
    showToast('请先登录')
    return
  }
  const id = route.params.id
  try {
    if (isFavorited.value) {
      await favoriteApi.remove(id)
      isFavorited.value = false
      showToast('已取消收藏')
    } else {
      await favoriteApi.add(id)
      isFavorited.value = true
      showToast('已收藏')
    }
  } catch (e) {
    showToast('操作失败')
  }
}

const addToCart = () => {
  cartStore.addToCart({
    id: product.value.id,
    name: product.value.name,
    mainImage: product.value.mainImage,
    price: selectedSpec.value?.price || product.value.price,
    specId: selectedSpec.value?.id,
    specInfo: selectedSpec.value ? `${selectedSpec.value.specName}:${selectedSpec.value.specValue}` : ''
  })
  showToast('已加入购物车')
}

const buyNow = () => {
  addToCart()
  router.push('/client/checkout')
}

const goToReviews = () => {
  // 简单处理，实际可以跳转评价列表页
  showToast('评价功能开发中')
}

const goToCS = () => {
  router.push('/client/cs')
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.product-detail { background: #f7f8fa; min-height: 100vh; padding-bottom: 50px; }
.product-swipe img { width: 100%; height: 375px; object-fit: cover; }
.product-info { background: #fff; padding: 15px; }
.product-name { font-size: 18px; font-weight: bold; }
.product-subtitle { color: #999; font-size: 13px; margin-top: 5px; }
.product-price-row { display: flex; align-items: baseline; gap: 10px; margin-top: 10px; }
.price { color: #ff5722; font-size: 24px; font-weight: bold; }
.original-price { color: #999; font-size: 14px; text-decoration: line-through; }
.sales { color: #999; font-size: 12px; margin-left: auto; }
.rating-row { display: flex; align-items: center; gap: 5px; margin-top: 8px; }
.review-count { color: #999; font-size: 12px; }
.spec-group { margin: 10px 0; }
.section { background: #fff; margin: 10px 0; padding: 15px; }
.section-title { display: flex; justify-content: space-between; align-items: center; font-size: 16px; font-weight: bold; margin-bottom: 10px; }
.description, .detail-content { font-size: 14px; color: #333; line-height: 1.6; }
.no-reviews { text-align: center; color: #999; padding: 20px; }
.review-item { padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.review-header { display: flex; justify-content: space-between; align-items: center; }
.review-time { font-size: 12px; color: #999; }
.review-content { margin-top: 5px; font-size: 14px; }
.review-reply { margin-top: 5px; padding: 8px; background: #f7f8fa; border-radius: 4px; font-size: 13px; color: #666; }
.spec-popup { padding: 20px; }
.spec-header { display: flex; gap: 10px; align-items: center; }
.spec-image { width: 80px; height: 80px; border-radius: 4px; object-fit: cover; }
.spec-price { color: #ff5722; font-size: 18px; font-weight: bold; }
.spec-stock { color: #999; font-size: 12px; }
.spec-list { margin-top: 15px; display: flex; flex-wrap: wrap; gap: 10px; }
.spec-item { padding: 8px 16px; border: 1px solid #ddd; border-radius: 4px; cursor: pointer; font-size: 14px; }
.spec-item.active { border-color: #1989fa; color: #1989fa; background: #ecf5ff; }
.spec-footer { margin-top: 20px; }
</style>
