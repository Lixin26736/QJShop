<template>
  <div class="product-detail" :class="{ 'is-pc': !isMobile }">
    <van-nav-bar title="商品详情" left-arrow @click-left="$router.back()" />

    <!-- ==================== 移动端布局 ==================== -->
    <template v-if="isMobile">
      <van-swipe class="m-swipe" :autoplay="3000" indicator-color="white">
        <van-swipe-item v-for="(img, idx) in images" :key="idx">
          <img :src="getImageUrl(img) || getPlaceholder(pName, pId, 375, 375)" :alt="pName" @error="e => e.target.src = getPlaceholder(pName, pId, 375, 375)" />
        </van-swipe-item>
      </van-swipe>

      <div class="m-info">
        <div class="price-area">
          <span class="price">¥{{ product.price }}</span>
          <span class="orig" v-if="product.originalPrice > product.price">¥{{ product.originalPrice }}</span>
        </div>
        <div class="name">{{ product.name }}</div>
        <div class="sub" v-if="product.subtitle">{{ product.subtitle }}</div>
        <div class="meta">
          <span class="sales">已售{{ product.sales || 0 }}</span>
          <span class="stock">库存{{ product.stock || 0 }}件</span>
          <span class="rate" v-if="avgRating">★ {{ avgRating }} ({{ reviewCount }}评)</span>
        </div>
        <div class="promise">✓ 正品保障 ✓ 7天退换 ✓ 极速发货</div>
      </div>

      <div class="m-spec" v-if="specs.length" @click="showSpec = true">
        <span>已选：{{ selectedSpec ? selectedSpec.specName+':'+selectedSpec.specValue : '请选择规格' }}</span>
        <van-icon name="arrow" />
      </div>

      <div class="m-section"><div class="m-title">商品详情</div><div class="m-content" v-html="product.detailContent || product.description || '暂无详情'"></div></div>

      <div class="m-section" v-if="reviews.length">
        <div class="m-title">商品评价 ({{ reviewCount }})</div>
        <div v-for="r in reviews" :key="r.id" class="m-review">
          <div class="rv-h"><van-rate v-model="r.rating" readonly :size="12" /><span class="rv-time">{{ fmtDate(r.createTime) }}</span></div>
          <div class="rv-c">{{ r.content }}</div>
        </div>
      </div>

      <van-action-bar>
        <van-action-bar-icon icon="chat-o" text="客服" @click="$router.push('/client/cs')" />
        <van-action-bar-icon icon="star-o" text="收藏" @click="toggleFavorite" :color="isFavorited ? '#ff5722' : ''" />
        <van-action-bar-icon icon="cart-o" text="购物车" @click="$router.push('/client/cart')" :badge="cartCount" />
        <van-action-bar-button type="warning" text="加入购物车" @click="addToCart" />
        <van-action-bar-button type="danger" text="立即购买" @click="buyNow" />
      </van-action-bar>
    </template>

    <!-- ==================== PC端布局(京东风格) ==================== -->
    <template v-else>
      <div class="pc-wrap">
        <!-- 左: 图片区 -->
        <div class="pc-gallery">
          <div class="pc-main-img">
            <img :src="getImageUrl(activeImg) || getPlaceholder(pName, pId, 450, 450)" :alt="pName" @error="e => e.target.src = getPlaceholder(pName, pId, 450, 450)" />
          </div>
          <div class="pc-thumbs" v-if="images.length > 1">
            <div v-for="(img, idx) in images" :key="idx" class="pc-thumb" :class="{ active: activeImg === img }" @click="activeImg = img">
              <img :src="getImageUrl(img) || getPlaceholder(pName, pId, 60, 60)" @error="e => e.target.src = getPlaceholder(pName, pId, 60, 60)" />
            </div>
          </div>
        </div>

        <!-- 右: 商品信息 -->
        <div class="pc-info">
          <h1 class="pc-name">{{ product.name }}</h1>
          <div class="pc-sub" v-if="product.subtitle">{{ product.subtitle }}</div>

          <div class="pc-price-box">
            <div class="pc-price-row">
              <span class="pc-price-label">价 格</span>
              <span class="pc-price">¥{{ product.price }}</span>
              <span class="pc-orig" v-if="product.originalPrice > product.price">¥{{ product.originalPrice }}</span>
              <span class="pc-discount" v-if="product.originalPrice > product.price">{{ Math.round((1 - product.price / product.originalPrice) * 100) }}% off</span>
            </div>
            <div class="pc-price-meta">
              <span>销量 {{ product.sales || 0 }}</span>
              <span style="margin-left:30px">库存 {{ product.stock || 0 }} 件</span>
              <span style="margin-left:30px" v-if="avgRating">★ {{ avgRating }} ({{ reviewCount }}条评价)</span>
            </div>
          </div>

          <div class="pc-specs" v-if="specs.length">
            <div class="pc-spec-title">规格</div>
            <div class="pc-spec-list">
              <span v-for="s in specs" :key="s.id" class="pc-spec-tag" :class="{ active: selectedSpec?.id === s.id }" @click="selectedSpec = s">
                {{ s.specName }}:{{ s.specValue }} <template v-if="s.price && s.price !== product.price">(¥{{ s.price }})</template>
              </span>
            </div>
          </div>

          <div class="pc-qty-row">
            <span class="pc-qty-label">数量</span>
            <van-stepper v-model="quantity" min="1" :max="product.stock || 99" />
          </div>

          <div class="pc-actions">
            <el-button size="large" @click="addToCart" style="width:160px;height:48px;font-size:16px">加入购物车</el-button>
            <el-button size="large" type="danger" @click="buyNow" style="width:160px;height:48px;font-size:16px">立即购买</el-button>
            <el-button size="large" plain @click="toggleFavorite" style="height:48px">
              {{ isFavorited ? '❤ 已收藏' : '♡ 收藏' }}
            </el-button>
            <el-button size="large" plain @click="$router.push('/client/cs')" style="height:48px">💬 客服</el-button>
          </div>

          <div class="pc-promise">✓ 正品保障 ✓ 7天无理由退换 ✓ 极速发货 ✓ 全国包邮</div>
        </div>
      </div>

      <div class="pc-detail-section">
        <div class="pc-detail-tabs">
          <span :class="{ active: detailTab === 1 }" @click="detailTab = 1">商品详情</span>
          <span :class="{ active: detailTab === 2 }" @click="detailTab = 2">商品评价({{ reviewCount }})</span>
        </div>
        <div class="pc-detail-body" v-show="detailTab === 1" v-html="product.detailContent || product.description || '暂无详情'"></div>
        <div class="pc-detail-body" v-show="detailTab === 2">
          <div v-if="!reviews.length" style="text-align:center;padding:40px;color:#999">暂无评价</div>
          <div v-for="r in reviews" :key="r.id" class="pc-review">
            <div class="pcr-h"><van-rate v-model="r.rating" readonly :size="14" /><span class="pcr-time">{{ fmtDate(r.createTime) }}</span></div>
            <div class="pcr-c">{{ r.content }}</div>
            <div class="pcr-r" v-if="r.reply">客服回复：{{ r.reply }}</div>
          </div>
        </div>
      </div>
    </template>

    <!-- 规格弹窗(移动端) -->
    <van-popup v-if="isMobile" v-model:show="showSpec" position="bottom" round>
      <div class="spec-pop">
        <div class="sp-h">
          <img :src="getImageUrl(product.mainImage) || getPlaceholder(pName, pId, 80, 80)" @error="e => e.target.src = getPlaceholder(pName, pId, 80, 80)" class="sp-img" />
          <div><div class="sp-price">¥{{ selectedSpec?.price || product.price }}</div><div class="sp-stock">库存 {{ selectedSpec?.stock || product.stock }} 件</div></div>
        </div>
        <div class="sp-list">
          <div v-for="s in specs" :key="s.id" class="sp-tag" :class="{ active: selectedSpec?.id === s.id }" @click="selectedSpec = s">
            {{ s.specName }}:{{ s.specValue }} <template v-if="s.price">(¥{{ s.price }})</template>
          </div>
        </div>
        <div class="sp-footer"><van-button type="primary" block @click="showSpec = false">确定</van-button></div>
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
import { getImageUrl, getPlaceholder } from '@/utils/image'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { useResponsive } from '@/utils/responsive'
import { showToast } from 'vant'

const route = useRoute(); const router = useRouter()
const cartStore = useCartStore(); const userStore = useUserStore()
const { isMobile } = useResponsive()

const product = ref({}); const specs = ref([])
const avgRating = ref(0); const reviewCount = ref(0); const reviews = ref([])
const selectedSpec = ref(null); const isFavorited = ref(false)
const showSpec = ref(false); const quantity = ref(1)
const activeImg = ref(''); const detailTab = ref(1)
const cartCount = computed(() => cartStore.cartCount)
const pName = computed(() => product.value.name || '商品')
const pId = computed(() => product.value.id || 0)

const images = computed(() => {
  const imgs = []
  if (product.value.mainImage) imgs.push(product.value.mainImage)
  if (product.value.detailImages) {
    try {
      let arr = product.value.detailImages
      if (typeof arr === 'string') {
        try { arr = JSON.parse(arr) } catch { arr = arr.split(',').filter(Boolean) }
      }
      if (Array.isArray(arr)) arr.forEach(img => imgs.push(img))
    } catch { /* ignore */ }
  }
  if (!imgs.length) imgs.push(getPlaceholder(pName.value, pId.value, 450, 450))
  return imgs
})

const loadData = async () => {
  const id = route.params.id
  try {
    const res = await productApi.getProductDetail(id)
    product.value = res.product || {}
    specs.value = res.specs || []
    avgRating.value = res.avgRating || 0
    reviewCount.value = res.reviewCount || 0
    activeImg.value = product.value.mainImage || images.value[0]
  } catch (e) { console.error(e) }
  try { const rr = await reviewApi.listByProduct(id, { pageNum: 1, pageSize: 5 }); reviews.value = rr?.records || [] } catch (e) { /* ignore */ }
  if (userStore.isLoggedIn) {
    try { const f = await favoriteApi.check(id); isFavorited.value = !!f } catch (e) { /* ignore */ }
  }
}

const toggleFavorite = async () => {
  if (!userStore.isLoggedIn) { showToast('请先登录'); return }
  const id = route.params.id
  try {
    if (isFavorited.value) { await favoriteApi.remove(id); isFavorited.value = false; showToast('已取消收藏') }
    else { await favoriteApi.add(id); isFavorited.value = true; showToast('已收藏') }
  } catch (e) { showToast('操作失败') }
}

const addToCart = () => {
  for (let i = 0; i < quantity.value; i++) {
    cartStore.addToCart({
      id: product.value.id, name: product.value.name, mainImage: product.value.mainImage,
      price: selectedSpec.value?.price || product.value.price,
      specId: selectedSpec.value?.id,
      specInfo: selectedSpec.value ? `${selectedSpec.value.specName}:${selectedSpec.value.specValue}` : ''
    })
  }
  showToast(`已加入购物车 x${quantity.value}`)
}

const buyNow = () => { addToCart(); router.push('/client/checkout') }
const fmtDate = (d) => {
  if (!d) return ''; const dt = new Date(d)
  return `${dt.getFullYear()}-${String(dt.getMonth()+1).padStart(2,'0')}-${String(dt.getDate()).padStart(2,'0')}`
}
onMounted(() => loadData())
</script>

<style scoped>
.product-detail { background: var(--bg); }
.product-detail.is-pc { max-width: 1200px; margin: 0 auto; padding: 20px 0; }
/* ====== 移动端 ====== */
.m-swipe img { width: 100%; height: 375px; object-fit: cover; }
.m-info { background: #fff; padding: 14px; }
.price-area { display: flex; align-items: baseline; gap: 8px; margin-bottom: 8px; }
.price-area .price { color: #f10215; font-size: 26px; font-weight: 800; }
.price-area .orig { color: #999; font-size: 13px; text-decoration: line-through; }
.m-info .name { font-size: 16px; font-weight: 600; line-height: 1.4; margin-bottom: 6px; }
.m-info .sub { color: #f10215; font-size: 12px; margin-bottom: 8px; }
.meta { display: flex; gap: 16px; font-size: 12px; color: #999; margin-bottom: 8px; }
.promise { font-size: 11px; color: #e4393c; background: #fff5f5; padding: 6px 10px; border-radius: 4px; }
.m-spec { display: flex; justify-content: space-between; align-items: center; padding: 12px 14px; background: #fff; margin-top: 8px; font-size: 13px; cursor: pointer; }
.m-section { background: #fff; margin-top: 8px; padding: 14px; }
.m-title { font-size: 15px; font-weight: 700; margin-bottom: 10px; padding-left: 8px; border-left: 3px solid var(--primary); }
.m-content { font-size: 13px; line-height: 1.6; }
.m-review { padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.rv-h { display: flex; justify-content: space-between; align-items: center; }
.rv-time { font-size: 11px; color: #999; }
.rv-c { font-size: 13px; margin-top: 6px; }
.product-detail.is-pc { padding-bottom: 30px; }

/* ====== PC端 ====== */
.pc-wrap { display: flex; gap: 30px; background: #fff; border-radius: var(--radius-lg); padding: 24px; margin-bottom: 20px; }
.pc-gallery { width: 450px; flex-shrink: 0; }
.pc-main-img { width: 450px; height: 450px; border: 1px solid var(--border); border-radius: 4px; overflow: hidden; display: flex; align-items: center; justify-content: center; background: #fafafa; }
.pc-main-img img { max-width: 100%; max-height: 100%; object-fit: contain; }
.pc-thumbs { display: flex; gap: 8px; margin-top: 12px; }
.pc-thumb { width: 60px; height: 60px; border: 2px solid transparent; border-radius: 4px; overflow: hidden; cursor: pointer; }
.pc-thumb.active { border-color: var(--primary); }
.pc-thumb img { width: 100%; height: 100%; object-fit: cover; }
.pc-info { flex: 1; min-width: 0; }
.pc-name { font-size: 20px; font-weight: 700; line-height: 1.4; margin-bottom: 6px; }
.pc-sub { color: #f10215; font-size: 14px; margin-bottom: 16px; }
.pc-price-box { background: #fff5f5; padding: 16px; border-radius: 8px; margin-bottom: 20px; }
.pc-price-row { display: flex; align-items: baseline; gap: 10px; margin-bottom: 8px; }
.pc-price-label { color: #999; font-size: 13px; }
.pc-price { color: #f10215; font-size: 28px; font-weight: 800; }
.pc-orig { color: #999; font-size: 14px; text-decoration: line-through; }
.pc-discount { background: #f10215; color: #fff; padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.pc-price-meta { color: #999; font-size: 12px; }
.pc-specs { margin-bottom: 20px; }
.pc-spec-title { font-size: 13px; color: #999; margin-bottom: 8px; }
.pc-spec-list { display: flex; flex-wrap: wrap; gap: 8px; }
.pc-spec-tag { padding: 6px 14px; border: 1px solid #ddd; border-radius: 4px; cursor: pointer; font-size: 13px; transition: all 0.15s; }
.pc-spec-tag:hover { border-color: var(--primary); }
.pc-spec-tag.active { border-color: var(--primary); color: var(--primary); background: #eff6ff; }
.pc-qty-row { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.pc-qty-label { font-size: 13px; color: #999; }
.pc-actions { display: flex; gap: 10px; align-items: center; }
.pc-promise { margin-top: 16px; font-size: 12px; color: #e4393c; }
.pc-detail-section { background: #fff; border-radius: var(--radius-lg); padding: 20px; }
.pc-detail-tabs { display: flex; gap: 30px; border-bottom: 2px solid #eee; margin-bottom: 20px; }
.pc-detail-tabs span { padding: 10px 0; cursor: pointer; font-size: 15px; color: #666; border-bottom: 2px solid transparent; margin-bottom: -2px; }
.pc-detail-tabs span.active { color: var(--primary); border-bottom-color: var(--primary); font-weight: 600; }
.pc-detail-body { font-size: 14px; line-height: 1.8; min-height: 200px; }
.pc-detail-body :deep(img) { max-width: 100%; }
.pc-review { padding: 14px 0; border-bottom: 1px solid #f0f0f0; }
.pcr-h { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.pcr-time { font-size: 12px; color: #999; }
.pcr-c { font-size: 14px; line-height: 1.5; }
.pcr-r { margin-top: 8px; padding: 8px 12px; background: #f7f8fa; border-radius: 4px; font-size: 13px; color: #666; }

/* ====== 规格弹窗(移动端) ====== */
.spec-pop { padding: 20px; }
.sp-h { display: flex; gap: 12px; align-items: center; margin-bottom: 16px; }
.sp-img { width: 80px; height: 80px; border-radius: 6px; object-fit: cover; }
.sp-price { color: #f10215; font-size: 18px; font-weight: 700; }
.sp-stock { color: #999; font-size: 12px; margin-top: 4px; }
.sp-list { display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 20px; }
.sp-tag { padding: 8px 18px; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; font-size: 14px; }
.sp-tag.active { border-color: var(--primary); color: var(--primary); background: #eff6ff; }
.sp-footer { padding-top: 10px; }
</style>
