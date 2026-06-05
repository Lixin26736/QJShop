<template>
  <div class="home">
    <!-- 移动端导航栏 -->
    <van-nav-bar v-if="isMobile" title="QJ商城" />

    <!-- 搜索栏 -->
    <div class="search-bar">
      <van-search v-model="searchKeyword" shape="round" placeholder="搜索商品" @search="handleSearch" />
    </div>

    <!-- Banner轮播 -->
    <van-swipe v-if="banners.length > 0" class="banner" :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="item in banners" :key="item.id">
        <img :src="item.image" :alt="item.title" loading="lazy" />
      </van-swipe-item>
    </van-swipe>

    <!-- 分类导航 -->
    <van-grid :column-num="categoryCols" class="category-nav">
      <van-grid-item v-for="item in categories" :key="item.id" :icon="item.icon" :text="item.name" @click="goToCategory(item.id)" />
    </van-grid>

    <!-- 热门商品 -->
    <div class="section">
      <div class="section-title">
        <van-icon name="fire-o" color="#ff5722" />
        <span>热门商品</span>
      </div>
      <div class="product-grid" :style="{ gridTemplateColumns: `repeat(${productCols}, 1fr)` }">
        <div v-for="product in hotProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="product.mainImage" :alt="product.name" loading="lazy" />
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">¥{{ product.price }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 新品推荐 -->
    <div class="section">
      <div class="section-title">
        <van-icon name="new-o" color="#1989fa" />
        <span>新品推荐</span>
      </div>
      <div class="product-grid" :style="{ gridTemplateColumns: `repeat(${productCols}, 1fr)` }">
        <div v-for="product in newProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="product.mainImage" :alt="product.name" loading="lazy" />
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">¥{{ product.price }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { productApi } from '@/api/product'
import { categoryApi } from '@/api/category'
import { bannerApi } from '@/api/banner'
import { uploadApi } from '@/api/upload'
import { useResponsive } from '@/utils/responsive'

const router = useRouter()
const { isMobile, isTablet, isDesktop } = useResponsive()

const searchKeyword = ref('')
const banners = ref([])

const categories = ref([])
const hotProducts = ref([])
const newProducts = ref([])

const categoryCols = computed(() => {
  if (isMobile.value) return 4
  if (isTablet.value) return 6
  return 8
})

const productCols = computed(() => {
  if (isMobile.value) return 2
  if (isTablet.value) return 3
  return 4
})

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/client/category', query: { keyword: searchKeyword.value } })
  }
}

const loadData = async () => {
  try {
    const [categoryRes, hotRes, newRes, bannerRes] = await Promise.all([
      categoryApi.getCategoryList(),
      productApi.getHotProducts(),
      productApi.getNewProducts(),
      bannerApi.list('home').catch(() => [])
    ])
    categories.value = categoryRes || []
    hotProducts.value = hotRes || []
    newProducts.value = newRes || []
    banners.value = (bannerRes || []).map(b => ({ ...b, image: uploadApi.getImageUrl(b.image) }))
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const goToCategory = (categoryId) => {
  router.push({ path: '/client/category', query: { id: categoryId } })
}

const goToProduct = (productId) => {
  router.push({ name: 'ProductDetail', params: { id: productId } })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home {
  background: #f7f8fa;
  min-height: 100vh;
}

.search-bar {
  padding: 8px 12px;
  background: #fff;
}

.banner img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

@media screen and (min-width: 769px) {
  .banner img {
    height: 300px;
  }
}

@media screen and (min-width: 1025px) {
  .banner img {
    height: 400px;
  }
}

.category-nav {
  margin: 10px 0;
}

.section {
  margin: 10px;
  background: #fff;
  border-radius: 8px;
  padding: 10px;
}

@media screen and (min-width: 769px) {
  .section {
    margin: 20px auto;
    padding: 20px;
    max-width: 1200px;
  }
}

.section-title {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

@media screen and (min-width: 769px) {
  .section-title {
    font-size: 18px;
    margin-bottom: 15px;
  }
}

.product-grid {
  display: grid;
  gap: 10px;
}

.product-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

@media screen and (min-width: 769px) {
  .product-card img {
    height: 180px;
  }
}

@media screen and (min-width: 1025px) {
  .product-card img {
    height: 200px;
  }
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 5px;
}

.product-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
}
</style>
