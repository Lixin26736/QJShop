<template>
  <div class="review-management">
    <el-card>
      <template #header><span>评价管理</span></template>
      <el-form :inline="true" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchStatus" clearable @change="load" style="width:120px">
            <el-option label="正常" :value="1" /><el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="productId" label="商品ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="rating" label="评分" width="80">
          <template #default="{ row }"><el-rate v-model="row.rating" disabled :size="14" /></template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="200" />
        <el-table-column prop="reply" label="回复" min-width="150">
          <template #default="{ row }">{{ row.reply || '未回复' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="160" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleReply(row)">{{ row.reply ? '修改回复' : '回复' }}</el-button>
            <el-button size="small" :type="row.status === 1 ? 'danger' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? '隐藏' : '显示' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, prev, pager, next" @change="load" style="margin-top:20px;justify-content:flex-end" />
    </el-card>

    <el-dialog v-model="replyDialog" title="回复评价" width="450px">
      <el-input v-model="replyText" type="textarea" :rows="4" placeholder="请输入回复内容" />
      <template #footer><el-button @click="replyDialog = false">取消</el-button><el-button type="primary" @click="submitReply">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reviewApi } from '@/api/review'

const list = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchStatus = ref(null)
const replyDialog = ref(false)
const replyText = ref('')
const currentReview = ref(null)

const load = async () => {
  loading.value = true
  try {
    const res = await reviewApi.adminPage({ pageNum: pageNum.value, pageSize: pageSize.value, status: searchStatus.value })
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const handleReply = (row) => { currentReview.value = row; replyText.value = row.reply || ''; replyDialog.value = true }
const submitReply = async () => {
  await reviewApi.reply(currentReview.value.id, replyText.value)
  ElMessage.success('回复成功')
  replyDialog.value = false
  load()
}
const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  await reviewApi.updateStatus(row.id, newStatus)
  ElMessage.success(newStatus === 1 ? '已显示' : '已隐藏')
  load()
}
const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' })
    .then(async () => { await reviewApi.delete(row.id); ElMessage.success('已删除'); load() })
    .catch(() => {})
}

load()
</script>

<style scoped>
.review-management { padding: 20px; }
.search-form { margin-bottom: 10px; }
</style>
