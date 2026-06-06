<template>
  <div class="category">
    <van-nav-bar title="商品分类" />

    <div class="category-container">
      <!-- 一级分类侧边栏 -->
      <van-sidebar v-model="activeFirstCategory" @change="onFirstCategoryChange">
        <van-sidebar-item v-for="item in firstCategories" :key="item.id" :title="item.name" />
      </van-sidebar>

      <!-- 右侧内容区 -->
      <div class="content-area">
        <!-- 二级分类标签 -->
        <div v-if="secondCategories.length > 0" class="second-category-tabs">
          <van-tabs v-model:active="activeSecondCategory" @change="onSecondCategoryChange">
            <van-tab v-for="item in secondCategories" :key="item.id" :title="item.name" />
          </van-tabs>
        </div>

        <!-- 商品列表 -->
        <div class="product-list">
          <div v-if="loading" style="text-align:center;padding:30px"><van-loading /></div>
          <div v-else-if="products.length === 0" class="empty-tip">
            <van-empty description="暂无商品" />
          </div>
          <div v-else class="product-grid">
            <div v-for="product in products" :key="product.id" class="product-card" @click="goToProduct(product.id)">
              <div class="card-img-wrap">
                <img :src="getImageUrl(product.mainImage) || getPlaceholder(product.name, product.id, 200, 200)" :alt="product.name" loading="lazy" @error="e => e.target.src = getPlaceholder(product.name, product.id, 200, 200)" />
              </div>
              <div class="card-info">
                <div class="card-name">{{ product.name }}</div>
                <div class="card-price-row">
                  <span class="card-price">¥{{ product.price }}</span>
                  <span class="card-sales" v-if="product.sales">已售{{ product.sales }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'
import { getImageUrl, getPlaceholder } from '@/utils/image'

import { useResponsive } from '@/utils/responsive'

const router = useRouter()
const route = useRoute()
const { isMobile, isTablet } = useResponsive()

const activeFirstCategory = ref(0)
const activeSecondCategory = ref(0)
const firstCategories = ref([])
const secondCategories = ref([])
const products = ref([])
const loading = ref(false)

// 加载一级分类
const loadFirstCategories = async () => {
  try {
    const res = await request.get('/api/admin/categories/first')
    console.log('一级分类数据:', res)
    firstCategories.value = res?.records || res || []
    if (firstCategories.value.length > 0) {
      const queryId = route.query.id
      if (queryId) {
        // 查找对应的一级分类
        const index = firstCategories.value.findIndex(c => c.id === Number(queryId))
        if (index > -1) {
          activeFirstCategory.value = index
        }
      }
      loadSecondCategories(firstCategories.value[activeFirstCategory.value].id)
    }
  } catch (error) {
    console.error('加载一级分类失败:', error)
  }
}

// 加载二级分类
const loadSecondCategories = async (parentId) => {
  try {
    const res = await request.get(`/api/admin/categories/second/${parentId}`)
    console.log('二级分类数据:', res)
    secondCategories.value = res?.records || res || []
    if (secondCategories.value.length > 0) {
      activeSecondCategory.value = 0
      loadProducts(secondCategories.value[0].id)
    } else {
      // 如果没有二级分类,直接加载一级分类下的商品
      loadProducts(parentId)
    }
  } catch (error) {
    console.error('加载二级分类失败:', error)
  }
}

// 加载商品
const loadProducts = async (categoryId) => {
  try {
    const res = await request.get('/api/admin/products/page', {
      params: {
        pageNum: 1,
        pageSize: 100,
        categoryId: categoryId
      }
    })
    console.log('商品数据:', res)
    products.value = res?.records || res || []
  } catch (error) {
    console.error('加载商品失败:', error)
  }
}

// 一级分类切换
const onFirstCategoryChange = (index) => {
  if (firstCategories.value[index]) {
    loadSecondCategories(firstCategories.value[index].id)
  }
}

// 二级分类切换
const onSecondCategoryChange = (index) => {
  if (secondCategories.value[index]) {
    loadProducts(secondCategories.value[index].id)
  }
}

const goToProduct = (productId) => {
  router.push({ name: 'ProductDetail', params: { id: productId } })
}

watch(() => route.query.id, (newId) => {
  if (newId && firstCategories.value.length > 0) {
    const index = firstCategories.value.findIndex(c => c.id === Number(newId))
    if (index > -1) {
      activeFirstCategory.value = index
      loadSecondCategories(firstCategories.value[index].id)
    }
  }
})

onMounted(() => {
  loadFirstCategories()
})
</script>

<style scoped>
.category { background: var(--bg); min-height: 100vh; }
.category-container { display: flex; height: calc(100vh - 46px); }
.content-area { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.second-category-tabs { background: var(--bg-card); border-bottom: 1px solid var(--border); }
.product-list { flex: 1; overflow-y: auto; padding: 8px; background: var(--bg); }
.empty-tip { display: flex; align-items: center; justify-content: center; height: 100%; }

/* 响应式商品网格: 3~5列 */
.product-grid {
  display: grid;
  gap: 8px;
  grid-template-columns: repeat(3, 1fr);
}
@media screen and (min-width: 500px) {
  .product-grid { grid-template-columns: repeat(4, 1fr); gap: 10px; }
}
@media screen and (min-width: 900px) {
  .product-grid { grid-template-columns: repeat(5, 1fr); gap: 12px; }
}

.product-card {
  cursor: pointer;
  background: var(--bg-card);
  border-radius: var(--radius);
  overflow: hidden;
  transition: transform 0.15s;
}
.product-card:active { transform: scale(0.97); }
.card-img-wrap { width: 100%; aspect-ratio: 1; overflow: hidden; background: #f1f5f9; }
.card-img-wrap img { width: 100%; height: 100%; object-fit: cover; display: block; }
.card-info { padding: 8px; }
.card-name { font-size: 12px; line-height: 1.3; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; margin-bottom: 4px; }
.card-price-row { display: flex; align-items: baseline; justify-content: space-between; }
.card-price { color: var(--danger); font-size: 14px; font-weight: 700; }
.card-sales { font-size: 10px; color: var(--text-secondary); }
</style>
