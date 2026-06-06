<template>
  <div class="favorite-list">
    <van-nav-bar title="我的收藏" left-arrow @click-left="$router.back()" />

    <van-empty v-if="favorites.length === 0 && !loading" description="暂无收藏" />
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="loadFavorites">
        <div v-for="item in favorites" :key="item.id" class="fav-card" @click="goProduct(item.productId)">
          <img :src="getImage(item)" class="fav-image" @error="e => e.target.src = getPlaceholder(item.productName, item.productId, 80, 80)" />
          <div class="fav-info">
            <div class="fav-name">{{ item.productName }}</div>
            <div class="fav-price">¥{{ item.price }}</div>
          </div>
          <van-icon name="delete" class="fav-delete" @click.stop="handleRemove(item.productId)" />
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { favoriteApi } from '@/api/favorite'
import { getImageUrl, getPlaceholder } from '@/utils/image'
import { showToast } from 'vant'

const router = useRouter()
const favorites = ref([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
let pageNum = 1

const getImage = (item) => getImageUrl(item.productImage) || getPlaceholder(item.productName, item.productId, 80, 80)

const loadFavorites = async () => {
  if (refreshing.value) { pageNum = 1; finished.value = false }
  loading.value = true
  try {
    const res = await favoriteApi.list({ pageNum, pageSize: 10 })
    const records = res?.records || []
    if (refreshing.value) favorites.value = records
    else favorites.value.push(...records)
    if (records.length < 10) finished.value = true
    pageNum++
  } catch (e) { /* ignore */ }
  finally { loading.value = false; refreshing.value = false }
}

const onRefresh = () => { refreshing.value = true; pageNum = 1; loadFavorites() }
const goProduct = (id) => router.push(`/client/product/${id}`)
const handleRemove = async (productId) => {
  await favoriteApi.remove(productId)
  favorites.value = favorites.value.filter(f => f.productId !== productId)
  showToast('已取消收藏')
}
</script>

<style scoped>
.favorite-list { background: #f7f8fa; min-height: 100vh; padding: 10px; }
.fav-card { display: flex; align-items: center; gap: 10px; padding: 10px; background: #fff; border-radius: 8px; margin-bottom: 10px; cursor: pointer; }
.fav-image { width: 80px; height: 80px; border-radius: 4px; object-fit: cover; }
.fav-info { flex: 1; }
.fav-name { font-size: 14px; margin-bottom: 5px; }
.fav-price { color: #ff5722; font-weight: bold; }
.fav-delete { color: #999; font-size: 20px; }
</style>
