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
          <div v-if="products.length === 0" class="empty-tip">
            <van-empty description="暂无商品" />
          </div>
          <van-grid v-else :column-num="2" :gutter="10">
            <van-grid-item v-for="product in products" :key="product.id">
              <div class="product-card" @click="goToProduct(product.id)">
                <img :src="product.mainImage" :alt="product.name" loading="lazy" />
                <div class="product-info">
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-price">¥{{ product.price }}</div>
                </div>
              </div>
            </van-grid-item>
          </van-grid>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

const activeFirstCategory = ref(0)
const activeSecondCategory = ref(0)
const firstCategories = ref([])
const secondCategories = ref([])
const products = ref([])

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
.category {
  background: #f7f8fa;
  min-height: 100vh;
}

.category-container {
  display: flex;
  height: calc(100vh - 96px);
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.second-category-tabs {
  background: #fff;
  border-bottom: 1px solid #eee;
}

.product-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background: #fff;
}

.empty-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.product-card {
  cursor: pointer;
}

.product-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info {
  padding: 5px;
}

.product-name {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
}
</style>
