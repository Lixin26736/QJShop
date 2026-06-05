<template>
  <div class="admin-settings">
    <el-card>
      <template #header><span>系统设置</span></template>
      <el-tabs>
        <el-tab-pane label="修改密码">
          <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px" style="max-width:400px">
            <el-form-item label="原密码" prop="oldPassword"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
            <el-form-item label="新密码" prop="newPassword"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword"><el-input v-model="pwdForm.confirmPassword" type="password" show-password /></el-form-item>
            <el-form-item><el-button type="primary" @click="changePassword" :loading="pwdLoading">修改密码</el-button></el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="关于系统">
          <div class="about">
            <p><strong>QJ商城管理后台 v1.0</strong></p>
            <p>前端: Vue 3 + Element Plus + Vant</p>
            <p>后端: Spring Boot 3.2 + MyBatis-Plus</p>
            <p>数据库: MySQL 8.0</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const userStore = useUserStore()
const pwdLoading = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码' }],
  newPassword: [{ required: true, min: 6, message: '密码至少6位' }],
  confirmPassword: [{ required: true, validator: (rule, value, cb) => value === pwdForm.newPassword ? cb() : cb(new Error('两次密码不一致')) }]
}
const pwdFormRef = ref(null)

const changePassword = async () => {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return
  pwdLoading.value = true
  try {
    await request.put('/api/user/profile', { password: pwdForm.newPassword })
    ElMessage.success('密码修改成功, 请重新登录')
    userStore.logout()
    window.location.href = '/admin/login'
  } catch (e) {
    ElMessage.error('修改失败')
  } finally {
    pwdLoading.value = false
  }
}
</script>

<style scoped>
.admin-settings { padding: 20px; }
.about p { margin-bottom: 10px; }
</style>
