<template>
  <div class="review-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>评价管理</span>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品ID">
          <el-input v-model="searchForm.productId" placeholder="商品ID" clearable @clear="handleSearch" style="width:140px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable @clear="handleSearch" style="width:120px">
            <el-option label="正常" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="rating" label="评分" width="180">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled :size="14" />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="reply" label="回复" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.reply">{{ row.reply }}</span>
            <span v-else style="color:#999">未回复</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="160" />
        <el-table-column label="操作" fixed="right" width="240">
          <template #default="{ row }">
            <el-button size="small" @click="handleReply(row)">
              {{ row.reply ? '修改回复' : '回复' }}
            </el-button>
            <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? '隐藏' : '显示' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="load"
        @current-change="load"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 回复弹窗 -->
    <el-dialog
      v-model="replyDialog"
      :title="currentReview?.reply ? '修改回复' : '回复评价'"
      width="500px"
      @closed="replyText = ''"
    >
      <el-form label-width="80px">
        <el-form-item label="评价内容">
          <div style="color:#666;line-height:1.6">{{ currentReview?.content }}</div>
        </el-form-item>
        <el-form-item label="回复内容">
          <el-input v-model="replyText" type="textarea" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReply">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reviewApi } from '@/api/review'

const list = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const replyDialog = ref(false)
const replyText = ref('')
const currentReview = ref(null)

const searchForm = reactive({
  productId: '',
  status: null
})

const load = async () => {
  loading.value = true
  try {
    const res = await reviewApi.adminPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      productId: searchForm.productId || undefined,
      status: searchForm.status
    })
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const handleSearch = () => { pageNum.value = 1; load() }
const handleReset = () => {
  searchForm.productId = ''
  searchForm.status = null
  pageNum.value = 1
  load()
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
  ElMessageBox.confirm('确定删除该评价?', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await reviewApi.delete(row.id); ElMessage.success('已删除'); load() })
    .catch(() => {})
}

load()
</script>

<style scoped>
.review-management { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
</style>
