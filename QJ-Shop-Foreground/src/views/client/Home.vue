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
    <div class="category-nav">
      <div v-for="item in categories" :key="item.id" class="cate-item" @click="goToCategory(item.id)">
        <div class="cate-icon">
          <img v-if="item.icon" :src="item.icon" :alt="item.name" @error="e => e.target.style.display='none'" />
          <span v-else class="cate-letter" :style="{ background: cateColors[item.id % cateColors.length] }">{{ item.name.charAt(0) }}</span>
        </div>
        <div class="cate-name">{{ item.name }}</div>
      </div>
    </div>

    <!-- 热门商品 -->
    <div class="section">
      <div class="section-title">
        <van-icon name="fire-o" color="#ff5722" />
        <span>热门商品</span>
      </div>
      <div class="product-grid" :style="{ gridTemplateColumns: `repeat(${productCols}, 1fr)` }">
        <div v-for="product in hotProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="getImageUrl(product.mainImage) || getPlaceholder(product.name, product.id, 300, 200)" :alt="product.name" loading="lazy" @error="e => e.target.src = getPlaceholder(product.name, product.id, 300, 200)" />
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
          <img :src="getImageUrl(product.mainImage) || getPlaceholder(product.name, product.id, 300, 200)" :alt="product.name" loading="lazy" @error="e => e.target.src = getPlaceholder(product.name, product.id, 300, 200)" />
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
import { getImageUrl, getPlaceholder } from '@/utils/image'
import { useResponsive } from '@/utils/responsive'

const router = useRouter()
const { isMobile, isTablet, isDesktop } = useResponsive()

const searchKeyword = ref('')
const banners = ref([])

const categories = ref([])
const hotProducts = ref([])
const newProducts = ref([])

const cateColors = ['#667eea', '#f56c6c', '#67c23a', '#e6a23c', '#409eff', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899', '#06b6d4']

const productCols = computed(() => {
  if (isMobile.value) return 2
  if (isTablet.value) return 3
  return 4
})

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ name: 'ClientCategory', query: { keyword: searchKeyword.value } })
    searchKeyword.value = ''
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
    banners.value = (bannerRes || []).map(b => ({ ...b, image: getImageUrl(b.image) || getPlaceholder(b.title, b.id, 800, 300) }))
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
.home { background: var(--bg); min-height: 100vh; }
.search-bar { padding: 10px 16px; background: var(--bg-card); box-shadow: var(--shadow-sm); }
.banner { margin: 0 auto; max-width: 1200px; }
.banner img { width: 100%; height: 180px; object-fit: cover; }
@media screen and (min-width: 769px) { .banner img { height: 280px; border-radius: 0 0 var(--radius-lg) var(--radius-lg); } }
@media screen and (min-width: 1025px) { .banner img { height: 360px; } }
.category-nav { margin: 12px auto; max-width: 1200px; background: var(--bg-card); border-radius: var(--radius); padding: 16px 10px; display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; text-align: center; }
@media screen and (max-width: 400px) { .category-nav { grid-template-columns: repeat(4, 1fr); } }
.cate-item { cursor: pointer; transition: transform 0.15s; }
.cate-item:active { transform: scale(0.95); }
.cate-icon { width: 44px; height: 44px; border-radius: 14px; display: flex; align-items: center; justify-content: center; margin: 0 auto 6px; overflow: hidden; }
.cate-icon img { width: 100%; height: 100%; object-fit: cover; }
.cate-letter { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: 700; }
.cate-name { font-size: 12px; color: var(--text); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.section { margin: 16px auto; max-width: 1200px; background: var(--bg-card); border-radius: var(--radius-lg); padding: 20px; box-shadow: var(--shadow-sm); }
.section-title { display: flex; align-items: center; gap: 8px; font-size: 18px; font-weight: 700; margin-bottom: 16px; color: var(--text); }
.section-title::before { content: ''; width: 4px; height: 20px; background: var(--primary); border-radius: 2px; }
.product-grid { display: grid; gap: 12px; }
.product-card {
  cursor: pointer;
  transition: all 0.25s ease;
  background: var(--bg-card);
  border-radius: var(--radius);
  overflow: hidden;
  border: 1px solid var(--border);
}
.product-card:hover { transform: translateY(-4px); box-shadow: var(--shadow-lg); border-color: var(--primary-light); }
.product-card img { width: 100%; height: 180px; object-fit: cover; }
.product-info { padding: 12px; }
.product-name {
  font-size: 14px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
  margin-bottom: 8px; color: var(--text);
}
.product-price { color: var(--danger); font-size: 18px; font-weight: 700; }
.product-price::before { content: '¥'; font-size: 14px; }
@media screen and (max-width: 768px) {
  .section { margin: 10px; padding: 12px; }
  .product-card img { height: 150px; }
  .banner img { height: 160px; }
}
</style>
