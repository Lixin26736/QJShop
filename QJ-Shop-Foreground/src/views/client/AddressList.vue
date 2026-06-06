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
  if (!navigator.geolocation) {
    ipFallback()
    return
  }
  locating.value = true

  const onSuccess = async (pos) => {
    const { latitude, longitude } = pos.coords
    const addr = await fetchAddr(latitude, longitude) || await fetchAddrFallback(latitude, longitude)
    fillAddress(addr)
    locating.value = false
  }

  const onError = (err) => {
    locating.value = false
    if (err.code === 1) {
      // PERMISSION_DENIED - 用户拒绝了，尝试IP定位
      ipFallback()
    } else if (err.code === 3) {
      showToast('定位超时,使用IP定位')
      ipFallback()
    } else {
      ipFallback()
    }
  }

  navigator.geolocation.getCurrentPosition(onSuccess, onError, { timeout: 8000, enableHighAccuracy: false })
}

// IP定位回退方案
async function ipFallback() {
  locating.value = true
  try {
    const res = await fetch('https://ipapi.co/json/')
    if (!res.ok) throw new Error('failed')
    const data = await res.json()
    fillAddress({
      province: data.region || '',
      city: data.city || '',
      district: ''
    })
    showToast('已通过IP获取位置')
  } catch (e) {
    try {
      // 第二回退
      const res2 = await fetch('https://api.ip.sb/geoip/')
      const d2 = await res2.json()
      fillAddress({ province: d2.region || '', city: d2.city || '', district: '' })
      showToast('已通过IP获取位置')
    } catch (e2) {
      showToast('无法获取位置,请手动输入')
    }
  } finally {
    locating.value = false
  }
}

function fillAddress(addr) {
  if (!addr) { showToast('位置解析失败,请手动输入'); return }
  form.value.province = addr.province || addr.state || ''
  form.value.city = addr.city || addr.county || ''
  form.value.district = addr.district || addr.town || ''
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
