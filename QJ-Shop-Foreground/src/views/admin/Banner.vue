<template>
  <div class="banner-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Banner管理</span>
          <el-button type="primary" @click="handleAdd">添加Banner</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="image" label="图片" width="150">
          <template #default="{ row }">
            <el-image :src="getImageUrl(row.image) || getPlaceholder(row.title, row.id, 120, 60)" style="width:120px;height:60px" fit="cover"><template #error><img :src="getPlaceholder(row.title, row.id, 120, 60)" style="width:120px;height:60px;object-fit:cover" /></template></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="150" />
        <el-table-column prop="position" label="位置" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="160" />
        <el-table-column prop="endTime" label="结束时间" width="160" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, prev, pager, next" @change="load" style="margin-top:20px;justify-content:flex-end" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑Banner' : '添加Banner'" width="500px" @closed="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片" prop="image">
          <div class="upload-wrap">
            <el-image v-if="form.image" :src="form.image" class="upload-preview" fit="cover" />
            <el-button size="small" @click="imgInput?.click()">选择图片</el-button>
            <input ref="imgInput" type="file" accept="image/*" hidden @change="onFileChange" />
          </div>
        </el-form-item>
        <el-form-item label="链接类型"><el-select v-model="form.linkType"><el-option label="商品" :value="1" /><el-option label="分类" :value="2" /><el-option label="URL" :value="3" /></el-select></el-form-item>
        <el-form-item label="链接目标"><el-input v-model="form.linkTarget" placeholder="商品ID/分类ID/URL" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="form.position" placeholder="如 home" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status"><el-option label="启用" :value="1" /><el-option label="禁用" :value="0" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { bannerApi } from '@/api/banner'
import { uploadApi } from '@/api/upload'
import { getImageUrl, getPlaceholder } from '@/utils/image'

const list = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const imgInput = ref(null)
const form = reactive({ id: null, title: '', image: '', linkType: 1, linkTarget: '', position: 'home', sortOrder: 0, startTime: '', endTime: '', status: 1 })
const rules = { title: [{ required: true, message: '请输入标题' }], image: [{ required: true, message: '请选择图片' }] }

const resetForm = () => { Object.assign(form, { id: null, title: '', image: '', linkType: 1, linkTarget: '', position: 'home', sortOrder: 0, startTime: '', endTime: '', status: 1 }); formRef.value?.resetFields() }

const load = async () => {
  loading.value = true
  try {
    const res = await bannerApi.adminPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const onFileChange = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  try {
    const res = await uploadApi.uploadImage(file)
    form.image = res.url
  } catch (e) { ElMessage.error('上传失败') }
  e.target.value = ''
}

const handleAdd = () => { isEdit.value = false; resetForm(); dialogVisible.value = true }
const handleEdit = (row) => { isEdit.value = true; resetForm(); Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) await bannerApi.update(form.id, form)
    else await bannerApi.create(form)
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    dialogVisible.value = false
    load()
  } catch (e) { ElMessage.error('操作失败') }
  finally { submitLoading.value = false }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' })
    .then(async () => { await bannerApi.delete(row.id); ElMessage.success('已删除'); load() })
    .catch(() => {})
}

load()
</script>

<style scoped>
.banner-management { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.upload-wrap { display: flex; align-items: center; gap: 10px; }
.upload-preview { width: 100px; height: 50px; border-radius: 4px; border: 1px solid #dcdfe6; }
</style>
