<template>
  <div class="user-settings">
    <van-nav-bar title="账号设置" left-arrow @click-left="goBack" />

    <!-- 头像上传 -->
    <van-cell-group inset class="settings-group">
      <van-cell title="头像" center>
        <template #right-icon>
          <div class="avatar-upload">
            <img :src="form.avatar || usePlaceholder" alt="头像" class="avatar-preview" />
            <input type="file" accept="image/*" @change="handleAvatarChange" ref="avatarInput" style="display:none" />
            <input type="file" accept="image/*" capture="camera" @change="handleAvatarChange" ref="cameraInput" style="display:none" />
            <van-button size="small" plain @click="triggerAvatarUpload">相册</van-button>
            <van-button size="small" plain type="primary" @click="triggerCamera">拍照</van-button>
          </div>
        </template>
      </van-cell>
    </van-cell-group>

    <!-- 基本信息 -->
    <van-cell-group inset class="settings-group">
      <van-field
        v-model="form.nickname"
        label="昵称"
        placeholder="请输入昵称"
        clearable
      />
      <van-field
        v-model="form.phone"
        label="手机号"
        placeholder="请输入手机号"
        clearable
        type="tel"
      />
      <van-field
        v-model="form.email"
        label="邮箱"
        placeholder="请输入邮箱"
        clearable
        type="email"
      />
      <van-cell title="性别" is-link @click="showGenderPicker = true">
        {{ getGenderText(form.gender) }}
      </van-cell>
      <van-cell title="生日" is-link @click="showBirthdayPicker = true">
        {{ form.birthday || '请选择' }}
      </van-cell>
    </van-cell-group>

    <!-- 性别选择器 -->
    <van-action-sheet
      v-model:show="showGenderPicker"
      :actions="genderActions"
      @select="onGenderSelect"
    />

    <!-- 生日选择器 -->
    <van-popup v-model:show="showBirthdayPicker" position="bottom">
      <van-date-picker
        v-model="selectedDate"
        title="选择生日"
        @confirm="onBirthdayConfirm"
        @cancel="showBirthdayPicker = false"
      />
    </van-popup>

    <!-- 保存按钮 -->
    <div class="save-button">
      <van-button type="primary" block :loading="loading" @click="handleSave">
        保存修改
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()
const avatarInput = ref(null)
const cameraInput = ref(null)
const usePlaceholder = 'https://via.placeholder.com/60'

const loading = ref(false)
const showGenderPicker = ref(false)
const showBirthdayPicker = ref(false)

const form = ref({
  avatar: '',
  nickname: '',
  phone: '',
  email: '',
  gender: null,
  birthday: ''
})

const selectedDate = ref(['2020', '01', '01'])

const genderActions = [
  { name: '男', value: 1 },
  { name: '女', value: 0 }
]

const getGenderText = (gender) => {
  if (gender === 1) return '男'
  if (gender === 0) return '女'
  return '请选择'
}

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const res = await request.get(`/api/user/profile`)
    form.value = {
      avatar: res.avatar || '',
      nickname: res.nickname || '',
      phone: res.phone || '',
      email: res.email || '',
      gender: res.gender,
      birthday: res.birthday || ''
    }
    
    // 设置生日选择器初始值
    if (res.birthday) {
      const parts = res.birthday.split('-')
      selectedDate.value = parts
    }
    
    // 同步更新用户store中的信息
    userStore.setUserInfo(res)
  } catch (error) {
    console.error('加载用户信息失败:', error)
    showToast('加载用户信息失败')
  }
}

// 触发头像上传
const triggerAvatarUpload = () => { avatarInput.value?.click() }
const triggerCamera = () => { cameraInput.value?.click() }

// 处理头像变更
const handleAvatarChange = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  
  // 检查文件大小(限制2MB)
  if (file.size > 2 * 1024 * 1024) {
    showToast('图片大小不能超过2MB')
    return
  }
  
  // 转换为base64
  const reader = new FileReader()
  reader.onload = (e) => {
    form.value.avatar = e.target?.result
  }
  reader.readAsDataURL(file)
}

// 性别选择
const onGenderSelect = (action) => {
  form.value.gender = action.value
  showGenderPicker.value = false
}

// 生日确认
const onBirthdayConfirm = ({ selectedValues }) => {
  form.value.birthday = selectedValues.join('-')
  showBirthdayPicker.value = false
}

// 保存修改
const handleSave = async () => {
  loading.value = true
  try {
    await request.put('/api/user/profile', form.value)
    
    // 更新本地用户信息
    userStore.setUserInfo({
      ...userStore.userInfo,
      ...form.value
    })
    
    showToast('保存成功')
    router.back()
  } catch (error) {
    showToast(error.message || '保存失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.user-settings {
  background: #f7f8fa;
  min-height: 100vh;
}

.settings-group {
  margin: 10px 0;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar-preview {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.save-button {
  padding: 20px;
}
</style>
