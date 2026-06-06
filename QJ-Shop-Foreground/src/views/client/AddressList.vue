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

const showEdit = (id) => {
  if (id) {
    const addr = addresses.value.find(a => a.id === id)
    if (addr) {
      form.value = { ...addr }
      isEdit.value = true
      currentEditId.value = id
    }
  } else {
    form.value = { receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 }
    isEdit.value = false
    currentEditId.value = null
  }
  showPopup.value = true
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
