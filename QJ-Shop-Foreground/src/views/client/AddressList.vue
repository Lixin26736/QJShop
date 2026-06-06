<template>
  <div class="address-list">
    <van-nav-bar title="收货地址" left-arrow @click-left="$router.back()" />

    <div class="address-content">
      <van-address-list
        v-model="defaultId"
        :list="addressList"
        default-tag-text="默认"
        @add="showEdit(null)"
        @edit="(item) => showEdit(item.id)"
        @select="(item) => handleSelect(item)"
      />
      <div v-if="addresses.length === 0" class="empty-wrap">
        <van-empty description="暂无收货地址" />
        <van-button type="primary" round block @click="showEdit(null)">添加新地址</van-button>
      </div>
    </div>

    <!-- 地址编辑弹窗 -->
    <van-popup v-model:show="showPopup" position="bottom" round :style="{ height: '80%' }">
      <div class="address-form">
        <h3>{{ isEdit ? '编辑地址' : '添加地址' }}</h3>
        <van-form @submit="handleSave">
          <van-cell-group inset style="margin-bottom:10px">
            <van-cell title="快速获取位置" value="GPS定位" is-link @click="getLocation" />
          </van-cell-group>
          <van-cell-group inset>
            <van-field v-model="form.receiverName" label="收货人" placeholder="请输入收货人姓名" required />
            <van-field v-model="form.receiverPhone" label="手机号" placeholder="请输入手机号" required type="tel" />
            <van-field v-model="form.province" label="省份" placeholder="省份" required />
            <van-field v-model="form.city" label="城市" placeholder="城市" required />
            <van-field v-model="form.district" label="区/县" placeholder="区/县" required />
            <van-field v-model="form.detailAddress" label="详细地址" placeholder="请输入详细地址" required />
          </van-cell-group>
          <div class="form-actions">
            <van-button round block type="primary" native-type="submit" :loading="saving">保存</van-button>
          </div>
        </van-form>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { addressApi } from '@/api/address'
import { showToast } from 'vant'

const addresses = ref([])
const showPopup = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const defaultId = ref('')
const currentEditId = ref(null)

const form = ref({
  receiverName: '', receiverPhone: '',
  province: '', city: '', district: '', detailAddress: '',
  isDefault: 0
})

const addressList = computed(() => addresses.value.map(a => ({
  id: String(a.id),
  name: a.receiverName,
  tel: a.receiverPhone,
  address: `${a.province} ${a.city} ${a.district} ${a.detailAddress}`,
  isDefault: a.isDefault === 1
})))

const loadAddresses = async () => {
  try {
    addresses.value = await addressApi.list()
    const def = addresses.value.find(a => a.isDefault === 1)
    if (def) defaultId.value = String(def.id)
  } catch (e) { /* ignore */ }
}

const locating = ref(false)

const showEdit = (id) => {
  if (id) {
    const addr = addresses.value.find(a => a.id == id)
    if (addr) {
      form.value = { ...addr }
      isEdit.value = true
      currentEditId.value = addr.id
    }
  } else {
    form.value = { receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 }
    isEdit.value = false
    currentEditId.value = null
  }
  showPopup.value = true
}

const getLocation = () => {
  if (!navigator.geolocation) { showToast('浏览器不支持定位'); return }
  locating.value = true

  const onSuccess = async (pos) => {
    try {
      const { latitude, longitude } = pos.coords
      // 优先使用Nominatim，带上User-Agent头
      let addr = await fetchAddr(latitude, longitude)
      if (!addr) {
        // 回退: 尝试另一种方式获取城市信息
        addr = await fetchAddrFallback(latitude, longitude)
      }
      if (addr) {
        form.value.province = addr.province || addr.state || ''
        form.value.city = addr.city || addr.county || ''
        form.value.district = addr.district || addr.town || ''
        showToast('已获取位置')
      } else {
        showToast('位置解析失败,请手动输入')
      }
    } catch (e) {
      showToast('位置解析失败: ' + (e.message || '请手动输入'))
    } finally {
      locating.value = false
    }
  }

  const onError = (err) => {
    locating.value = false
    const msg = err.PERMISSION_DENIED ? '请允许定位权限' : err.TIMEOUT ? '定位超时' : '定位不可用'
    showToast(msg)
  }

  navigator.geolocation.getCurrentPosition(onSuccess, onError, { timeout: 10000, enableHighAccuracy: false })
}

async function fetchAddr(lat, lon) {
  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lon}&format=json&accept-language=zh`,
      { headers: { 'User-Agent': 'QJShop/1.0' } }
    )
    if (!res.ok) return null
    const data = await res.json()
    return data.address || null
  } catch (e) { return null }
}

async function fetchAddrFallback(lat, lon) {
  try {
    const res = await fetch(
      `https://geocode.maps.co/reverse?lat=${lat}&lon=${lon}&format=json`
    )
    if (!res.ok) return null
    const data = await res.json()
    const addr = data.address || {}
    return { province: addr.state || '', city: addr.city || addr.county || '', district: addr.district || '' }
  } catch (e) { return null }
}

const handleSave = async () => {
  saving.value = true
  try {
    if (isEdit.value) {
      await addressApi.update(currentEditId.value, form.value)
      showToast('已更新')
    } else {
      await addressApi.create(form.value)
      showToast('已添加')
    }
    showPopup.value = false
    loadAddresses()
  } catch (e) {
    showToast('保存失败')
  } finally {
    saving.value = false
  }
}

const handleSelect = async (item) => {
  try {
    await addressApi.setDefault(Number(item.id))
    showToast('已设为默认')
    loadAddresses()
  } catch (e) { /* ignore */ }
}

onMounted(() => { loadAddresses() })
</script>

<style scoped>
.address-list { background: var(--bg); min-height: 100vh; }
.address-content { padding-bottom: 20px; }
.empty-wrap { padding: 20px; text-align: center; }
.address-form { padding: 20px; }
.address-form h3 { text-align: center; margin-bottom: 20px; }
.form-actions { margin-top: 20px; padding: 0 16px; }
</style>
