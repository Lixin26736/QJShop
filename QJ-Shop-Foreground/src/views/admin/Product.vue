<template>
  <div class="product-management">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" style="margin-bottom: 20px">
      <el-breadcrumb-item>
        <el-link @click="goToLevel(0)" :type="currentLevel === 0 ? 'primary' : 'default'">商品管理</el-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentLevel >= 1 && selectedFirstCategory">
        <el-link @click="goToLevel(1)" :type="currentLevel === 1 ? 'primary' : 'default'">{{ selectedFirstCategory.name }}</el-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentLevel >= 2 && selectedSecondCategory">
        <span>{{ selectedSecondCategory.name }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 一级分类列表 -->
    <el-card v-if="currentLevel === 0">
      <template #header>
        <div class="card-header">
          <span>一级分类</span>
          <el-button type="primary" size="small" @click="handleAddFirstCategory">添加一级分类</el-button>
        </div>
      </template>
      <el-table :data="firstCategories" style="width: 100%" v-loading="loading" @row-click="selectFirstCategory">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.icon && (scope.row.icon.startsWith('data:') || scope.row.icon.startsWith('http'))" :src="scope.row.icon" style="width: 40px; height: 40px" fit="cover" />
            <el-icon v-else-if="scope.row.icon"><component :is="scope.row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click.stop="selectFirstCategory(scope.row)">查看子分类</el-button>
            <el-button size="small" @click.stop="handleEditFirstCategory(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDeleteFirstCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 二级分类列表 -->
    <el-card v-else-if="currentLevel === 1">
      <template #header>
        <div class="card-header">
          <span>{{ selectedFirstCategory?.name }} - 二级分类</span>
          <el-button type="primary" size="small" @click="handleAddSecondCategory">添加二级分类</el-button>
        </div>
      </template>
      <el-table :data="secondCategories" style="width: 100%" v-loading="loading" @row-click="selectSecondCategory">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.icon && (scope.row.icon.startsWith('data:') || scope.row.icon.startsWith('http'))" :src="scope.row.icon" style="width: 40px; height: 40px" fit="cover" />
            <el-icon v-else-if="scope.row.icon"><component :is="scope.row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="商品数量" width="100">
          <template #default="scope">
            <el-tag>{{ scope.row.productCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click.stop="selectSecondCategory(scope.row)">查看商品</el-button>
            <el-button size="small" @click.stop="handleEditSecondCategory(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDeleteSecondCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 商品列表 -->
    <el-card v-else-if="currentLevel === 2">
      <template #header>
        <div class="card-header">
          <span>{{ selectedSecondCategory?.name }} - 商品列表</span>
          <div class="header-actions">
            <el-button type="success" size="small" @click="handleExportProduct">导出Excel</el-button>
            <el-button type="primary" size="small" @click="handleAddProduct">添加商品</el-button>
          </div>
        </div>
      </template>

      <!-- 商品搜索表单 -->
      <el-form :inline="true" :model="productSearchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="productSearchForm.keyword" placeholder="商品名称" clearable @clear="handleProductSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="productSearchForm.status" placeholder="请选择" clearable @clear="handleProductSearch">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleProductSearch">查询</el-button>
          <el-button @click="handleProductReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="productList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" width="200" />
        <el-table-column prop="mainImage" label="主图" width="100">
          <template #default="scope">
            <el-image :src="scope.row.mainImage" style="width: 60px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEditProduct(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteProduct(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadProducts"
        @current-change="loadProducts"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 添加/编辑一级分类弹窗 -->
    <el-dialog
      v-model="categoryDialogVisible"
      :title="categoryDialogType === 'add' ? '添加一级分类' : '编辑一级分类'"
      width="450px"
      @closed="resetCategoryForm"
    >
      <el-form ref="categoryFormRef" :model="categoryFormData" :rules="categoryFormRules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryFormData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <div class="upload-wrapper">
            <el-image v-if="categoryFormData.icon" :src="categoryFormData.icon" class="upload-preview" fit="cover" />
            <div v-else class="upload-placeholder">暂无图标</div>
            <el-button size="small" type="primary" @click="categoryIconInputRef?.click()">选择图片</el-button>
            <el-button v-if="categoryFormData.icon" size="small" @click="categoryFormData.icon = ''">清除</el-button>
            <input ref="categoryIconInputRef" type="file" accept="image/*" style="display:none" @change="handleFileUpload($event, (val) => categoryFormData.icon = val)" />
          </div>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="categoryFormData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="categoryFormData.status" placeholder="请选择">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCategorySubmit" :loading="categorySubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 添加/编辑二级分类弹窗 -->
    <el-dialog
      v-model="secondCategoryDialogVisible"
      :title="secondCategoryDialogType === 'add' ? '添加二级分类' : '编辑二级分类'"
      width="450px"
      @closed="resetSecondCategoryForm"
    >
      <el-form ref="secondCategoryFormRef" :model="secondCategoryFormData" :rules="categoryFormRules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="secondCategoryFormData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <div class="upload-wrapper">
            <el-image v-if="secondCategoryFormData.icon" :src="secondCategoryFormData.icon" class="upload-preview" fit="cover" />
            <div v-else class="upload-placeholder">暂无图标</div>
            <el-button size="small" type="primary" @click="secondCategoryIconInputRef?.click()">选择图片</el-button>
            <el-button v-if="secondCategoryFormData.icon" size="small" @click="secondCategoryFormData.icon = ''">清除</el-button>
            <input ref="secondCategoryIconInputRef" type="file" accept="image/*" style="display:none" @change="handleFileUpload($event, (val) => secondCategoryFormData.icon = val)" />
          </div>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="secondCategoryFormData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="secondCategoryFormData.status" placeholder="请选择">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="secondCategoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSecondCategorySubmit" :loading="secondCategorySubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 添加/编辑商品弹窗 -->
    <el-dialog
      v-model="productDialogVisible"
      :title="productDialogType === 'add' ? '添加商品' : '编辑商品'"
      width="650px"
      @closed="resetProductForm"
    >
      <el-form ref="productFormRef" :model="productFormData" :rules="productFormRules" label-width="80px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productFormData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="productFormData.subtitle" placeholder="请输入副标题" />
        </el-form-item>
        <el-form-item label="主图" prop="mainImage">
          <div class="upload-wrapper">
            <el-image v-if="productFormData.mainImage" :src="productFormData.mainImage" class="upload-preview" fit="cover" />
            <div v-else class="upload-placeholder">暂无主图</div>
            <el-button size="small" type="primary" @click="mainImageInputRef?.click()">选择图片</el-button>
            <el-button v-if="productFormData.mainImage" size="small" @click="productFormData.mainImage = ''">清除</el-button>
            <input ref="mainImageInputRef" type="file" accept="image/*" style="display:none" @change="handleFileUpload($event, (val) => productFormData.mainImage = val)" />
          </div>
        </el-form-item>
        <el-form-item label="详情图片" prop="detailImages">
          <div class="detail-images-upload">
            <div class="detail-images-list">
              <div v-for="(img, index) in detailImageList" :key="index" class="detail-image-item">
                <el-image :src="img" class="upload-preview" fit="cover" />
                <el-button size="small" type="danger" circle @click="removeDetailImage(index)">
                  <el-icon><Close /></el-icon>
                </el-button>
              </div>
              <div v-if="detailImageList.length === 0" class="upload-placeholder">暂无详情图片</div>
            </div>
            <el-button size="small" type="primary" @click="detailImagesInputRef?.click()">添加图片</el-button>
            <input ref="detailImagesInputRef" type="file" accept="image/*" style="display:none" @change="handleDetailImageUpload" />
          </div>
        </el-form-item>
        <el-form-item label="售价" prop="price">
          <el-input-number v-model="productFormData.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="productFormData.originalPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="productFormData.stock" :min="0" />
        </el-form-item>
        <el-form-item label="销量" prop="sales">
          <el-input-number v-model="productFormData.sales" :min="0" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="productFormData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="productFormData.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="详情内容" prop="detailContent">
          <el-input v-model="productFormData.detailContent" type="textarea" :rows="4" placeholder="请输入详情内容(支持富文本)" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="productFormData.status" placeholder="请选择">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="热销" prop="isHot">
          <el-select v-model="productFormData.isHot" placeholder="请选择">
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="新品" prop="isNew">
          <el-select v-model="productFormData.isNew" placeholder="请选择">
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProductSubmit" :loading="productSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Close } from '@element-plus/icons-vue'
import request from '@/utils/request'

const currentLevel = ref(0) // 0: 一级分类, 1: 二级分类, 2: 商品列表
const loading = ref(false)

// 一级分类
const firstCategories = ref([])
const selectedFirstCategory = ref(null)

// 二级分类
const secondCategories = ref([])
const selectedSecondCategory = ref(null)

// 商品
const productList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const productSearchForm = ref({
  keyword: '',
  status: null
})

// ========== 一级分类弹窗 ==========
const categoryDialogVisible = ref(false)
const categoryDialogType = ref('add')
const categorySubmitLoading = ref(false)
const categoryFormRef = ref(null)
const categoryIconInputRef = ref(null)

// ========== 文件上传工具 ==========
const mainImageInputRef = ref(null)
const detailImagesInputRef = ref(null)

// 详情图片列表（用于多图管理）
const detailImageList = computed({
  get: () => productFormData.detailImages ? productFormData.detailImages.split(',').filter(s => s) : [],
  set: (val) => { productFormData.detailImages = val.join(',') }
})

const handleFileUpload = (event, callback) => {
  const file = event.target.files[0]
  if (!file) return
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过10MB')
    event.target.value = ''
    return
  }
  const reader = new FileReader()
  reader.onload = (e) => {
    callback(e.target.result)
  }
  reader.readAsDataURL(file)
  event.target.value = ''
}

const handleDetailImageUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过10MB')
    event.target.value = ''
    return
  }
  const reader = new FileReader()
  reader.onload = (e) => {
    const list = detailImageList.value
    list.push(e.target.result)
    detailImageList.value = list
  }
  reader.readAsDataURL(file)
  event.target.value = ''
}

const removeDetailImage = (index) => {
  const list = [...detailImageList.value]
  list.splice(index, 1)
  detailImageList.value = list
}

const categoryFormData = reactive({
  id: null,
  name: '',
  icon: '',
  sortOrder: 0,
  status: 1
})

const categoryFormRules = reactive({
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

const resetCategoryForm = () => {
  categoryFormData.id = null
  categoryFormData.name = ''
  categoryFormData.icon = ''
  categoryFormData.sortOrder = 0
  categoryFormData.status = 1
  categoryFormRef.value?.resetFields()
}

// ========== 二级分类弹窗 ==========
const secondCategoryDialogVisible = ref(false)
const secondCategoryDialogType = ref('add')
const secondCategorySubmitLoading = ref(false)
const secondCategoryFormRef = ref(null)
const secondCategoryIconInputRef = ref(null)

const secondCategoryFormData = reactive({
  id: null,
  parentId: null,
  name: '',
  icon: '',
  sortOrder: 0,
  status: 1
})

const resetSecondCategoryForm = () => {
  secondCategoryFormData.id = null
  secondCategoryFormData.parentId = null
  secondCategoryFormData.name = ''
  secondCategoryFormData.icon = ''
  secondCategoryFormData.sortOrder = 0
  secondCategoryFormData.status = 1
  secondCategoryFormRef.value?.resetFields()
}

// ========== 商品弹窗 ==========
const productDialogVisible = ref(false)
const productDialogType = ref('add')
const productSubmitLoading = ref(false)
const productFormRef = ref(null)

const productFormData = reactive({
  id: null,
  categoryId: null,
  name: '',
  subtitle: '',
  mainImage: '',
  detailImages: '',
  price: 0,
  originalPrice: 0,
  stock: 0,
  sales: 0,
  sortOrder: 0,
  description: '',
  detailContent: '',
  status: 1,
  isHot: 0,
  isNew: 0
})

const productFormRules = reactive({
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

const resetProductForm = () => {
  productFormData.id = null
  productFormData.categoryId = null
  productFormData.name = ''
  productFormData.subtitle = ''
  productFormData.mainImage = ''
  productFormData.detailImages = ''
  productFormData.price = 0
  productFormData.originalPrice = 0
  productFormData.stock = 0
  productFormData.sales = 0
  productFormData.sortOrder = 0
  productFormData.description = ''
  productFormData.detailContent = ''
  productFormData.status = 1
  productFormData.isHot = 0
  productFormData.isNew = 0
  productFormRef.value?.resetFields()
}

// ========== 数据加载 ==========
const loadFirstCategories = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/categories/first')
    firstCategories.value = res?.records || res || []
  } catch (error) {
    ElMessage.error('加载一级分类失败')
  } finally {
    loading.value = false
  }
}

const loadSecondCategories = async (parentId) => {
  loading.value = true
  try {
    const res = await request.get(`/api/admin/categories/second/${parentId}`)
    secondCategories.value = res?.records || res || []
  } catch (error) {
    ElMessage.error('加载二级分类失败')
  } finally {
    loading.value = false
  }
}

const loadProducts = async () => {
  if (!selectedSecondCategory.value) return
  loading.value = true
  try {
    const res = await request.get('/api/admin/products/page', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        categoryId: selectedSecondCategory.value.id,
        keyword: productSearchForm.value.keyword,
        status: productSearchForm.value.status
      }
    })
    productList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

// 商品搜索
const handleProductSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const handleProductReset = () => {
  productSearchForm.value = {
    keyword: '',
    status: null
  }
  currentPage.value = 1
  loadProducts()
}

// 导出商品Excel
const handleExportProduct = async () => {
  if (!selectedSecondCategory.value) return
  try {
    ElMessage.info('正在导出...')
    const res = await request.get('/api/admin/products/export', {
      params: {
        categoryId: selectedSecondCategory.value.id,
        keyword: productSearchForm.value.keyword,
        status: productSearchForm.value.status
      },
      responseType: 'blob'
    })

    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `商品数据_${selectedSecondCategory.value.name}_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 选择一级分类
const selectFirstCategory = (row) => {
  selectedFirstCategory.value = row
  currentLevel.value = 1
  loadSecondCategories(row.id)
}

// 选择二级分类
const selectSecondCategory = (row) => {
  selectedSecondCategory.value = row
  currentLevel.value = 2
  currentPage.value = 1
  loadProducts()
}

// 导航层级切换
const goToLevel = (level) => {
  currentLevel.value = level
  if (level === 0) {
    selectedFirstCategory.value = null
    selectedSecondCategory.value = null
    loadFirstCategories()
  } else if (level === 1) {
    selectedSecondCategory.value = null
    if (selectedFirstCategory.value) {
      loadSecondCategories(selectedFirstCategory.value.id)
    }
  }
}

// ========== 一级分类操作 ==========
const handleAddFirstCategory = () => {
  categoryDialogType.value = 'add'
  resetCategoryForm()
  categoryDialogVisible.value = true
}

const handleEditFirstCategory = (row) => {
  categoryDialogType.value = 'edit'
  resetCategoryForm()
  categoryFormData.id = row.id
  categoryFormData.name = row.name || ''
  categoryFormData.icon = row.icon || ''
  categoryFormData.sortOrder = row.sortOrder ?? 0
  categoryFormData.status = row.status
  categoryDialogVisible.value = true
}

const handleCategorySubmit = async () => {
  const valid = await categoryFormRef.value.validate().catch(() => false)
  if (!valid) return

  categorySubmitLoading.value = true
  try {
    const data = { ...categoryFormData, parentId: 0 }
    if (categoryDialogType.value === 'add') {
      delete data.id
      await request.post('/api/admin/categories', data)
      ElMessage.success('添加成功')
    } else {
      await request.put(`/api/admin/categories/${data.id}`, data)
      ElMessage.success('编辑成功')
    }
    categoryDialogVisible.value = false
    await loadFirstCategories()
  } catch (error) {
    ElMessage.error(categoryDialogType.value === 'add' ? '添加失败' : '编辑失败')
  } finally {
    categorySubmitLoading.value = false
  }
}

const handleDeleteFirstCategory = (row) => {
  ElMessageBox.confirm('确定要删除该一级分类吗?删除后其下的二级分类和商品也将被删除!', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/categories/${row.id}`)
        ElMessage.success('删除成功')
        await loadFirstCategories()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// ========== 二级分类操作 ==========
const handleAddSecondCategory = () => {
  secondCategoryDialogType.value = 'add'
  resetSecondCategoryForm()
  secondCategoryFormData.parentId = selectedFirstCategory.value?.id
  secondCategoryDialogVisible.value = true
}

const handleEditSecondCategory = (row) => {
  secondCategoryDialogType.value = 'edit'
  resetSecondCategoryForm()
  secondCategoryFormData.id = row.id
  secondCategoryFormData.parentId = row.parentId
  secondCategoryFormData.name = row.name || ''
  secondCategoryFormData.icon = row.icon || ''
  secondCategoryFormData.sortOrder = row.sortOrder ?? 0
  secondCategoryFormData.status = row.status
  secondCategoryDialogVisible.value = true
}

const handleSecondCategorySubmit = async () => {
  const valid = await secondCategoryFormRef.value.validate().catch(() => false)
  if (!valid) return

  secondCategorySubmitLoading.value = true
  try {
    const data = { ...secondCategoryFormData }
    if (secondCategoryDialogType.value === 'add') {
      delete data.id
      await request.post('/api/admin/categories', data)
      ElMessage.success('添加成功')
    } else {
      await request.put(`/api/admin/categories/${data.id}`, data)
      ElMessage.success('编辑成功')
    }
    secondCategoryDialogVisible.value = false
    if (selectedFirstCategory.value) {
      await loadSecondCategories(selectedFirstCategory.value.id)
    }
  } catch (error) {
    ElMessage.error(secondCategoryDialogType.value === 'add' ? '添加失败' : '编辑失败')
  } finally {
    secondCategorySubmitLoading.value = false
  }
}

const handleDeleteSecondCategory = (row) => {
  ElMessageBox.confirm('确定要删除该二级分类吗?删除后其下的商品也将被删除!', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/categories/${row.id}`)
        ElMessage.success('删除成功')
        await loadSecondCategories(selectedFirstCategory.value.id)
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// ========== 商品操作 ==========
const handleAddProduct = () => {
  productDialogType.value = 'add'
  resetProductForm()
  productFormData.categoryId = selectedSecondCategory.value?.id
  productDialogVisible.value = true
}

const handleEditProduct = (row) => {
  productDialogType.value = 'edit'
  resetProductForm()
  productFormData.id = row.id
  productFormData.categoryId = row.categoryId
  productFormData.name = row.name || ''
  productFormData.subtitle = row.subtitle || ''
  productFormData.mainImage = row.mainImage || ''
  productFormData.detailImages = row.detailImages || ''
  productFormData.price = row.price ?? 0
  productFormData.originalPrice = row.originalPrice ?? 0
  productFormData.stock = row.stock ?? 0
  productFormData.sales = row.sales ?? 0
  productFormData.sortOrder = row.sortOrder ?? 0
  productFormData.description = row.description || ''
  productFormData.detailContent = row.detailContent || ''
  productFormData.status = row.status
  productFormData.isHot = row.isHot ?? 0
  productFormData.isNew = row.isNew ?? 0
  productDialogVisible.value = true
}

const handleProductSubmit = async () => {
  const valid = await productFormRef.value.validate().catch(() => false)
  if (!valid) return

  productSubmitLoading.value = true
  try {
    const data = { ...productFormData }
    if (productDialogType.value === 'add') {
      delete data.id
      await request.post('/api/admin/products', data)
      ElMessage.success('添加成功')
    } else {
      await request.put(`/api/admin/products/${data.id}`, data)
      ElMessage.success('编辑成功')
    }
    productDialogVisible.value = false
    loadProducts()
  } catch (error) {
    ElMessage.error(productDialogType.value === 'add' ? '添加失败' : '编辑失败')
  } finally {
    productSubmitLoading.value = false
  }
}

const handleDeleteProduct = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/api/admin/products/${row.id}`)
        ElMessage.success('删除成功')
        loadProducts()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  loadFirstCategories()
})
</script>

<style scoped>
.product-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}

.el-table {
  cursor: pointer;
}

.el-table__row:hover {
  background-color: #f5f7fa;
}

.upload-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.upload-preview {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.upload-placeholder {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  border: 1px dashed #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 12px;
}

.detail-images-upload {
  width: 100%;
}

.detail-images-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}

.detail-image-item {
  position: relative;
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}
</style>
