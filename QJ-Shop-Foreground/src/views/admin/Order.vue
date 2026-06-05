<template>
  <div class="order-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="success" @click="handleExport">导出Excel</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="订单号" clearable @clear="handleSearch" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select style="width:100px;" v-model="searchForm.status" placeholder="请选择" clearable @clear="handleSearch">
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="orderList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="payType" label="支付方式" width="100">
          <template #default="scope">
            {{ scope.row.payType === 1 ? '微信' : '支付宝' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleDeliver(scope.row)" v-if="scope.row.status === 1">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="650px">
      <div v-if="orderDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(orderDetail.status)">{{ getStatusText(orderDetail.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ orderDetail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="实付">¥{{ orderDetail.payAmount }}</el-descriptions-item>
          <el-descriptions-item label="支付方式">{{ orderDetail.payType === 1 ? '微信' : '支付宝' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ orderDetail.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top:15px">
          <h4>商品明细</h4>
          <el-table :data="orderItems" style="width:100%;margin-top:10px">
            <el-table-column prop="productName" label="商品" />
            <el-table-column prop="specInfo" label="规格" />
            <el-table-column prop="price" label="单价" width="100"><template #default="{ row }">¥{{ row.price }}</template></el-table-column>
            <el-table-column prop="quantity" label="数量" width="60" />
            <el-table-column prop="totalPrice" label="小计" width="100"><template #default="{ row }">¥{{ row.totalPrice }}</template></el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const orderList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  orderNo: '',
  status: null
})

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/orders/page', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        orderNo: searchForm.value.orderNo,
        status: searchForm.value.status
      }
    })
    orderList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

const handleReset = () => {
  searchForm.value = {
    orderNo: '',
    status: null
  }
  currentPage.value = 1
  loadOrders()
}

const handleExport = async () => {
  try {
    ElMessage.info('正在导出...')
    const res = await request.get('/api/admin/orders/export', {
      params: {
        orderNo: searchForm.value.orderNo,
        status: searchForm.value.status
      },
      responseType: 'blob'
    })

    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `订单数据_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const detailVisible = ref(false)
const orderDetail = ref(null)
const orderItems = ref([])

const handleView = async (row) => {
  try {
    const res = await request.get(`/admin/orders/${row.id}`)
    orderDetail.value = res.order
    orderItems.value = res.items || []
    detailVisible.value = true
  } catch (e) { ElMessage.error('加载失败') }
}

const handleDeliver = async (row) => {
  try {
    await request.put(`/admin/orders/${row.id}/ship`)
    ElMessage.success('发货成功')
    loadOrders()
  } catch (e) { ElMessage.error('操作失败') }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
