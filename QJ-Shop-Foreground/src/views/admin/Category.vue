<template>
  <div class="category-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="handleAdd(null)">添加一级分类</el-button>
        </div>
      </template>
      <el-table :data="firstCategories" v-loading="loading" row-key="id" style="width:100%">
        <el-table-column type="expand">
          <template #default="{ row }">
            <div style="padding:10px 40px">
              <div class="sub-header">
                <span>二级分类</span>
                <el-button size="small" type="primary" @click="handleAdd(row.id)">添加</el-button>
              </div>
              <el-table :data="getChildren(row.id)" style="width:100%">
                <el-table-column prop="id" label="ID" width="60" />
                <el-table-column prop="name" label="名称" />
                <el-table-column prop="sortOrder" label="排序" width="80" />
                <el-table-column prop="status" label="状态" width="80">
                  <template #default="{ row: r }"><el-tag :type="r.status === 1 ? 'success' : 'danger'">{{ r.status === 1 ? '启用' : '禁用' }}</el-tag></template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="{ row: r }">
                    <el-button size="small" @click="handleEdit(r)">编辑</el-button>
                    <el-button size="small" type="danger" @click="handleDelete(r.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" @click="handleAdd(row.id)">添加子分类</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '添加分类'" width="450px" @closed="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status"><el-option label="启用" :value="1" /><el-option label="禁用" :value="0" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const firstCategories = ref([])
const childrenCache = ref({})
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, parentId: 0, name: '', sortOrder: 0, status: 1 })
const rules = { name: [{ required: true, message: '请输入名称' }] }

const resetForm = () => { Object.assign(form, { id: null, name: '', sortOrder: 0, status: 1 }); formRef.value?.resetFields() }

const loadFirst = async () => {
  loading.value = true
  try { firstCategories.value = await request.get('/api/admin/categories/first') || [] }
  catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const getChildren = (parentId) => {
  if (!childrenCache.value[parentId]) {
    request.get(`/admin/categories/second/${parentId}`).then(res => childrenCache.value[parentId] = res || [])
    return []
  }
  return childrenCache.value[parentId]
}

const handleAdd = (parentId) => {
  isEdit.value = false
  resetForm()
  form.parentId = parentId || 0
  dialogVisible.value = true
}
const handleEdit = (row) => {
  isEdit.value = true
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) await request.put(`/api/admin/categories/${form.id}`, form)
    else await request.post('/api/admin/categories', form)
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    dialogVisible.value = false
    childrenCache.value = {}
    loadFirst()
  } catch (e) { ElMessage.error('操作失败') }
  finally { submitLoading.value = false }
}
const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' })
    .then(async () => { await request.delete(`/api/admin/categories/${id}`); ElMessage.success('已删除'); childrenCache.value = {}; loadFirst() })
    .catch(() => {})
}

loadFirst()
</script>

<style scoped>
.category-management { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.sub-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
</style>
