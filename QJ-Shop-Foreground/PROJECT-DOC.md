# QJ-Shop-Foreground 项目文档

## 一、项目概述

| 属性 | 值 |
|------|-----|
| 项目名称 | QJ-Shop-Foreground (QJ商城前台) |
| 版本 | 0.0.0 |
| 类型 | Vue 3 单页应用 (SPA) |
| 构建工具 | Vite 8.0.3 |
| Node 要求 | ^20.19.0 \|\| >=22.12.0 |
| 开发端口 | 3000 |
| 项目性质 | 电商商城前台系统, 包含客户端(C端)和管理后台(B端)两套界面 |

---

## 二、技术栈与依赖

### 2.1 生产依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| vue | ^3.5.31 | 核心框架 |
| vue-router | ^5.0.4 | 路由管理 |
| pinia | ^3.0.4 | 状态管理 |
| axios | ^1.15.0 | HTTP 请求 |
| element-plus | ^2.13.7 | PC 端 UI 组件库 (管理后台) |
| vant | ^4.9.24 | 移动端 UI 组件库 (客户端) |

### 2.2 开发依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| @vitejs/plugin-vue | ^6.0.5 | Vue SFC 编译支持 |
| vite | ^8.0.3 | 构建工具 |
| vite-plugin-vue-devtools | ^8.1.1 | Vue DevTools 调试插件 |
| terser | ^5.46.1 | 代码压缩 (生产构建) |

### 2.3 NPM Scripts

| 脚本 | 命令 | 说明 |
|------|------|------|
| dev | vite | 启动开发服务器 |
| build | vite build | 生产构建 |
| preview | vite preview | 预览生产构建 |

---

## 三、项目目录结构

```
QJ-Shop-Foreground/
├── public/
│   └── favicon.ico                    # 网站图标
├── src/
│   ├── main.js                        # 入口文件
│   ├── App.vue                        # 根组件
│   ├── api/                           # API 接口层
│   │   ├── user.js                    # 用户 API
│   │   ├── product.js                 # 商品 API
│   │   ├── category.js                # 分类 API
│   │   └── order.js                   # 订单 API
│   ├── assets/                        # 静态资源
│   │   ├── base.css                   # 基础样式 (CSS 变量)
│   │   ├── main.css                   # 主样式
│   │   ├── responsive.css             # 响应式样式
│   │   └── logo.svg                   # Logo
│   ├── components/                    # 公共组件
│   ├── router/
│   │   └── index.js                   # 路由配置
│   ├── store/                         # Pinia 状态管理
│   │   ├── index.js                   # Pinia 实例
│   │   ├── user.js                    # 用户状态
│   │   └── cart.js                    # 购物车状态
│   ├── utils/                         # 工具类
│   │   ├── request.js                 # Axios 封装
│   │   ├── envConfig.js               # 环境配置
│   │   └── responsive.js              # 响应式工具
│   └── views/                         # 页面视图
│       ├── admin/                     # 管理端页面 (Element Plus)
│       │   ├── Layout.vue             # 管理端布局
│       │   ├── Login.vue              # 管理员登录
│       │   ├── Dashboard.vue          # 仪表盘
│       │   ├── User.vue               # 用户管理
│       │   ├── Product.vue            # 商品管理
│       │   └── Order.vue              # 订单管理
│       └── client/                    # 客户端页面 (Vant)
│           ├── Layout.vue             # 客户端布局
│           ├── Home.vue               # 首页
│           ├── Category.vue           # 分类页
│           ├── Cart.vue               # 购物车
│           ├── Login.vue              # 用户登录
│           ├── Register.vue           # 用户注册
│           ├── Profile.vue            # 个人中心
│           └── UserSettings.vue       # 账号设置
├── .env                               # 环境变量
├── index.html                         # HTML 入口
├── vite.config.js                     # Vite 配置
├── package.json                       # 依赖配置
└── jsconfig.json                      # JS 配置
```

---

## 四、构建配置 (vite.config.js)

### 4.1 路径别名

- `@` -> `./src`

### 4.2 开发服务器

| 配置项 | 值 | 说明 |
|--------|-----|------|
| host | 0.0.0.0 | 允许外部访问 |
| port | 3000 | 开发服务端口 |
| proxy /api | http://localhost:8080 | 代理到后端, 去除 /api 前缀 |

### 4.3 构建优化

- 压缩器: Terser (生产环境移除 console 和 debugger)
- 代码分割 (manualChunks):
  - `element-plus` -> 独立 chunk
  - `vant` -> 独立 chunk
  - `vue/vue-router/pinia` -> `vue-vendor` chunk
  - 其他 `node_modules` -> `vendor` chunk
- chunkSizeWarningLimit: 1000KB

### 4.4 预优化依赖

vue, vue-router, pinia, axios, element-plus, vant

---

## 五、环境变量 (.env)

| 变量 | 值 | 说明 |
|------|-----|------|
| VITE_API_BASE_URL | http://localhost:8080 | API 基础 URL |

---

## 六、应用入口

### 6.1 main.js 初始化顺序

1. 导入 responsive.css (最先加载, 确保响应式变量可用)
2. 创建 Vue 应用实例
3. 全局注册 Element Plus Icons (所有图标组件)
4. 安装插件: router -> pinia -> Vant -> ElementPlus
5. 挂载到 #app

### 6.2 App.vue

- 模板: 仅 `<router-view />`
- 全局样式: CSS Reset (margin/padding/box-sizing), body 字体栈, #app 全屏最小高度

---

## 七、API 层 (src/api/)

### 7.1 user.js

| 函数 | 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|------|
| login(data) | POST | /api/admin/auth/login | {username, password} | 管理员/用户登录 |
| register(data) | POST | /api/admin/auth/register | 注册数据 | 用户注册 |
| getUserInfo() | GET | /api/user/profile | 无 | 获取用户信息 |
| updateUserInfo(data) | PUT | /api/user/profile | 用户信息对象 | 更新用户信息 |
| logout() | POST | /api/admin/auth/logout | 无 | 退出登录 |

### 7.2 product.js

| 函数 | 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|------|
| getProductList(params) | GET | /api/admin/products/page | 分页参数 | 分页获取商品列表 |
| getProductDetail(id) | GET | /api/admin/products/${id} | 商品 ID | 获取商品详情 |
| getHotProducts() | GET | /api/admin/products/hot | 无 | 获取热门商品 |
| getNewProducts() | GET | /api/admin/products/new | 无 | 获取新品推荐 |
| getProductsByCategory(categoryId, params) | GET | /api/admin/products/category/${categoryId} | 分类 ID + 分页 | 按分类获取商品 |

### 7.3 category.js

| 函数 | 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|------|
| getCategoryList() | GET | /api/admin/categories/page | 无 | 获取分类列表 |
| getCategoryTree() | GET | /api/admin/categories/tree | 无 | 获取分类树形结构 |
| getCategoryDetail(id) | GET | /api/admin/categories/${id} | 分类 ID | 获取分类详情 |

### 7.4 order.js

| 函数 | 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|------|
| createOrder(data) | POST | /api/admin/orders | 订单数据 | 创建订单 |
| getOrderList(params) | GET | /api/admin/orders/page | 分页参数 | 分页获取订单列表 |
| getOrderDetail(id) | GET | /api/admin/orders/${id} | 订单 ID | 获取订单详情 |
| cancelOrder(id) | PUT | /api/admin/orders/${id}/cancel | 订单 ID | 取消订单 |
| payOrder(id, data) | PUT | /api/admin/orders/${id}/pay | 订单 ID + 支付数据 | 支付订单 |

---

## 八、路由系统 (src/router/index.js)

### 8.1 路由模式

HTML5 History 模式 (createWebHistory)

### 8.2 路由表

| 路径 | 名称 | 组件 | Meta | 说明 |
|------|------|------|------|------|
| / | - | - | - | 重定向到 /client |
| /login | Login | client/Login.vue | {title: '登录'} | 客户端登录页 |
| /register | Register | client/Register.vue | {title: '注册'} | 客户端注册页 |
| /client | - | client/Layout.vue | - | 客户端布局 (重定向到 /client/home) |
| /client/home | ClientHome | client/Home.vue | {title: '首页'} | 首页 |
| /client/category | ClientCategory | client/Category.vue | {title: '分类'} | 分类页 |
| /client/cart | ClientCart | client/Cart.vue | {title: '购物车', requiresAuth: true} | 购物车 (需登录) |
| /client/profile | ClientProfile | client/Profile.vue | {title: '个人信息', requiresAuth: true} | 个人中心 (需登录) |
| /client/settings | UserSettings | client/UserSettings.vue | {title: '账号设置', requiresAuth: true} | 账号设置 (需登录) |
| /admin/login | AdminLogin | admin/Login.vue | {title: '管理员登录'} | 管理员登录页 |
| /admin | - | admin/Layout.vue | {requiresAuth: true, requiresAdmin: true} | 管理后台布局 (重定向到 /admin/dashboard) |
| /admin/dashboard | AdminDashboard | admin/Dashboard.vue | {title: '仪表盘'} | 仪表盘 |
| /admin/user | AdminUser | admin/User.vue | {title: '用户管理'} | 用户管理 |
| /admin/product | AdminProduct | admin/Product.vue | {title: '商品管理'} | 商品管理 |
| /admin/order | AdminOrder | admin/Order.vue | {title: '订单管理'} | 订单管理 |

### 8.3 路由守卫 (beforeEach)

1. 设置页面标题: `document.title = meta.title + ' - QJ商城'`
2. 获取认证状态: `userStore.isLoggedIn` 和 `userStore.userInfo.role === 1` (管理员判断)
3. 需要认证的页面 (requiresAuth: true):
   - 未登录 + 管理员页面 -> 跳转 /admin/login?redirect=原路径
   - 未登录 + 客户端页面 -> 跳转 /login?redirect=原路径
   - 已登录但非管理员 + 管理员页面 -> 跳转 /client/home
4. 已登录用户访问登录/注册页:
   - 访问 /login 或 /register -> 跳转 /client/home
   - 访问 /admin/login + 是管理员 -> 跳转 /admin/dashboard
   - 访问 /admin/login + 非管理员 -> 跳转 /client/home

---

## 九、状态管理 (src/store/)

### 9.1 user.js - 用户状态

**State:**

| 状态 | 类型 | 初始值 | 持久化 | 说明 |
|------|------|--------|--------|------|
| token | String | localStorage.getItem('token') \|\| '' | localStorage | JWT 令牌 |
| userInfo | Object | JSON.parse(localStorage.getItem('userInfo') \|\| '{}') | localStorage | 用户信息 |
| isLoggedIn | Boolean | !!token.value | 派生 | 登录状态 |

**Actions:**

| Action | 参数 | 逻辑 | 说明 |
|--------|------|------|------|
| setToken(newToken) | 新令牌字符串 | 更新 token 和 isLoggedIn, 同步 localStorage | 设置认证令牌 |
| setUserInfo(info) | 用户信息对象 | 更新 userInfo, 同步 localStorage (JSON 序列化) | 设置用户信息 |
| logout() | 无 | 调用 setToken('') 和 setUserInfo(null) | 退出登录 (清除所有状态) |

### 9.2 cart.js - 购物车状态

**State:**

| 状态 | 类型 | 初始值 | 持久化 | 说明 |
|------|------|--------|--------|------|
| cartItems | Array | JSON.parse(localStorage.getItem('cartItems') \|\| '[]') | localStorage | 购物车商品列表 |

**Getters:**

| Getter | 计算逻辑 | 说明 |
|--------|----------|------|
| cartCount | reduce(total + item.quantity, 0) | 购物车商品总数量 |
| cartTotal | reduce(total + item.price * item.quantity, 0) | 购物车总金额 |

**Actions:**

| Action | 参数 | 逻辑 | 说明 |
|--------|------|------|------|
| addToCart(product) | 商品对象 | 已存在则 quantity+1, 不存在则 push 并设 quantity=1 | 添加商品到购物车 |
| removeFromCart(productId) | 商品 ID | findIndex 并 splice 移除 | 从购物车移除商品 |
| updateQuantity(productId, quantity) | 商品 ID + 新数量 | 找到商品更新 quantity | 更新商品数量 |
| clearCart() | 无 | 清空 cartItems 数组 | 清空购物车 |
| saveCart() (内部) | 无 | localStorage.setItem('cartItems', JSON.stringify(cartItems)) | 持久化购物车 |

---

## 十、工具层 (src/utils/)

### 10.1 request.js - Axios 请求封装

**Axios 实例配置:**
- baseURL: /api
- timeout: 10000ms (10 秒)

**请求拦截器:**
- 从 useUserStore() 获取 token
- 如果 token 存在, 设置 `Authorization: Bearer ${token}` 请求头

**响应拦截器 - 成功:**
- 如果 responseType === 'blob' (文件下载), 直接返回 response.data
- 否则检查 res.code:
  - code === 200: 返回 res.data (解包业务数据)
  - 其他: showToast(res.message) 并 Promise.reject

**响应拦截器 - 错误:**

| HTTP 状态码 | 处理 |
|-------------|------|
| 401 | 调用 userStore.logout(), 提示"登录已过期,请重新登录" |
| 403 | 提示"没有权限访问" |
| 404 | 提示"请求资源不存在" |
| 500 | 提示"服务器错误" |
| 其他 | 提示 error.response.data.message |
| 无响应 | 提示"网络连接失败" |

### 10.2 envConfig.js - 环境配置

**环境类型:**

| 类型 | 常量 | 说明 |
|------|------|------|
| 本地开发 | DEVELOPMENT | localhost |
| 模拟器 | EMULATOR | VirtualBox Host-Only 网络 |
| 真机 | REAL_DEVICE | 局域网真实 IP |

当前环境: REAL_DEVICE

**环境配置:**

| 环境 | API_BASE_URL | BACKEND_URL |
|------|-------------|-------------|
| DEVELOPMENT | http://localhost:3000 | http://localhost:8080 |
| EMULATOR | http://192.168.56.1:3000 | http://192.168.56.1:8080 |
| REAL_DEVICE | http://192.168.3.191:3000 | http://192.168.3.191:8080 |

**导出函数:**

| 函数 | 返回值 | 说明 |
|------|--------|------|
| getApiBaseUrl() | 当前环境的 API_BASE_URL | 获取前端 API 地址 |
| getBackendUrl() | 当前环境的 BACKEND_URL | 获取后端地址 |
| getCurrentEnv() | 当前环境类型字符串 | 获取当前环境 |
| isDevelopment() | boolean | 是否开发环境 |
| isEmulator() | boolean | 是否模拟器环境 |
| isRealDevice() | boolean | 是否真机环境 |

### 10.3 responsive.js - 响应式工具

**useResponsive() 组合式函数:**

| 返回值 | 类型 | 说明 |
|--------|------|------|
| isMobile | ref(boolean) | 屏幕宽度 <= 768px |
| isTablet | ref(boolean) | 768px < 屏幕宽度 <= 1024px |
| isDesktop | ref(boolean) | 屏幕宽度 > 1024px |
| screenWidth | ref(number) | 当前屏幕宽度 |

- 使用防抖优化 (100ms) 监听 resize 事件
- onMounted 时初始化检测
- onUnmounted 时清理定时器和事件监听
- resize 事件使用 { passive: true } 优化性能

**断点常量:** Mobile <= 768px, Tablet 769-1024px, Desktop > 1024px

**辅助函数:**

| 函数 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| getGridCols(screenWidth) | 屏幕宽度 | 1/2/4 | 获取网格列数 |
| getImageSize(screenWidth) | 屏幕宽度 | {width, height} | 获取图片尺寸 |

getGridCols 映射: <=768 -> 1, <=1024 -> 2, >1024 -> 4
getImageSize 映射: <=768 -> 150x150, <=1024 -> 180x180, >1024 -> 200x200

---

## 十一、管理后台视图 (src/views/admin/)

### 11.1 Layout.vue - 管理后台布局

**组件结构:**
- 移动端: el-drawer 抽屉式侧边菜单
- PC/平板端: el-aside 固定侧边栏 (可折叠)

**侧边栏菜单项:**

| 路径 | 图标 | 文字 |
|------|------|------|
| /admin/dashboard | DataAnalysis | 仪表盘 |
| /admin/user | User | 用户管理 |
| /admin/product | Goods | 商品管理 |
| /admin/order | Document | 订单管理 |

**Header 区域:**
- 移动端: 菜单按钮 (打开抽屉)
- PC/平板端: 折叠按钮 + 面包屑导航
- 用户信息下拉: 头像 + 昵称, 下拉菜单 (个人中心/退出登录)

**关键数据/方法:**

| 名称 | 类型 | 说明 |
|------|------|------|
| isCollapse | ref(false) | 侧边栏折叠状态 |
| drawerVisible | ref(false) | 抽屉可见状态 |
| sidebarWidth | computed | 平板 180px, PC 200px |
| activeMenu | computed | 当前路由路径 |
| currentTitle | computed | 路由 meta.title |
| toggleCollapse() | method | 切换侧边栏折叠 |
| handleMenuSelect() | method | 关闭移动端抽屉 |
| goToProfile() | method | 跳转 /client/profile |
| handleLogout() | method | 确认后调用 userStore.logout() 并跳转首页 |

**样式:** 深色侧边栏 (#545c64), 白色 Header, 浅灰主内容区 (#f0f2f5)

### 11.2 Login.vue - 管理员登录

**表单字段:**
- 用户名 (必填, blur 触发验证)
- 密码 (必填, blur 触发验证, 支持显示/隐藏)

**登录逻辑 (handleLogin):**
1. 表单验证
2. POST /api/auth/login 发送 {username, password}
3. 检查 res.user.role !== 1 则拒绝 (仅管理员可登录)
4. 保存 token 和 userInfo 到 store
5. 跳转到 redirect 参数或 /admin/dashboard

**样式:** 渐变背景 (135deg, #667eea -> #764ba2), 白色卡片居中

### 11.3 Dashboard.vue - 仪表盘

**统计卡片 (4 个):**

| 指标 | 图标颜色 | 数据字段 |
|------|----------|----------|
| 用户总数 | #409eff (蓝) | stats.userCount |
| 商品总数 | #67c23a (绿) | stats.productCount |
| 订单总数 | #e6a23c (橙) | stats.orderCount |
| 销售总额 | #f56c6c (红) | stats.totalAmount |

**数据表格 (2 个):**
- 最近订单: 订单号、金额、状态
- 热销商品: 商品名称、销量、价格

**数据加载:** GET /api/admin/dashboard/stats, onMounted 时调用

**订单状态映射:**

| 状态码 | 文字 | Tag 类型 |
|--------|------|----------|
| 0 | 待付款 | info |
| 1 | 待发货 | warning |
| 2 | 待收货 | success |
| 3 | 已完成 | success |
| 4 | 已取消 | danger |

### 11.4 User.vue - 用户管理

**搜索表单:**
- 关键词 (用户名/昵称/手机号)
- 角色 (管理员=1/普通用户=0)
- 状态 (正常=1/禁用=0)

**表格列:** ID, 用户名, 昵称, 手机号, 邮箱, 角色 (Tag), 状态 (Tag), 创建时间, 操作 (编辑/删除)

**分页:** 支持 10/20/50/100 条/页

**API 调用:**

| 操作 | 方法 | 路径 |
|------|------|------|
| 加载用户 | GET | /api/admin/users/page |
| 导出 Excel | GET | /api/admin/users/export (responseType=blob) |
| 删除用户 | DELETE | /api/admin/users/${id} |

**方法:**
- loadUsers(): 加载用户列表
- handleSearch(): 重置页码后搜索
- handleReset(): 重置搜索条件
- handleExport(): 导出 Excel (创建 Blob 下载链接)
- handleAdd(): 待实现
- handleEdit(row): 待实现
- handleDelete(row): 确认后调用删除 API

### 11.5 Product.vue - 商品管理

**三级导航式商品管理 (一级分类 -> 二级分类 -> 商品):**

| currentLevel | 内容 |
|--------------|------|
| Level 0 | 一级分类列表 |
| Level 1 | 二级分类列表 (选中一级分类后) |
| Level 2 | 商品列表 (选中二级分类后) |

**一级分类表格:** ID, 分类名称, 图标, 排序, 状态 (启用/禁用), 操作 (查看子分类/编辑/删除)
**二级分类表格:** ID, 分类名称, 排序, 状态, 商品数量, 操作 (查看商品/编辑/删除)
**商品表格:** ID, 商品名称, 主图, 价格, 库存, 销量, 状态 (上架/下架), 操作 (编辑/删除)

**商品搜索:** 关键词 + 状态 (上架/下架)

**API 调用:**

| 操作 | 方法 | 路径 |
|------|------|------|
| 加载一级分类 | GET | /api/admin/categories/first |
| 加载二级分类 | GET | /api/admin/categories/second/${parentId} |
| 加载商品 | GET | /api/admin/products/page (含 categoryId) |
| 导出商品 Excel | GET | /api/admin/products/export (responseType=blob) |
| 删除分类 | DELETE | /api/admin/categories/${id} |
| 删除商品 | DELETE | /api/admin/products/${id} |

**面包屑导航:** 商品管理 > 一级分类名 > 二级分类名

**待实现:** 添加/编辑分类、添加/编辑商品

### 11.6 Order.vue - 订单管理

**搜索表单:** 订单号 + 订单状态 (待付款/待发货/待收货/已完成/已取消)

**表格列:** ID, 订单号, 订单金额, 支付方式 (微信=1/支付宝=其他), 订单状态 (Tag), 创建时间, 操作 (查看/发货)

**发货按钮:** 仅当 status === 1 (待发货) 时显示

**API 调用:**

| 操作 | 方法 | 路径 |
|------|------|------|
| 加载订单 | GET | /api/admin/orders/page |
| 导出 Excel | GET | /api/admin/orders/export (responseType=blob) |

**待实现:** 查看订单详情、发货操作

---

## 十二、客户端视图 (src/views/client/)

### 12.1 Layout.vue - 客户端布局

**PC 端:** 顶部固定导航栏 (Logo + 导航链接)
**移动端:** Vant van-tabbar 底部标签栏

**导航项:**

| 路径 | PC 文字 | 移动端图标 | 移动端文字 |
|------|---------|-----------|-----------|
| /client/home | 首页 | home-o | 首页 |
| /client/category | 分类 | apps-o | 分类 |
| /client/cart | 购物车 (带 badge) | shopping-cart-o | 购物车 |
| /client/profile | 我的 | user-o | 我的 |

**购物车角标:** 使用 cartStore.cartCount 显示商品数量

**样式:** 白色 Header + sticky 定位, Logo 蓝色 (#1989fa), 购物车角标红色 (#ff5722)

### 12.2 Home.vue - 首页

**页面结构:**
1. 移动端导航栏: van-nav-bar "QJ商城"
2. Banner 轮播: van-swipe 自动播放 (3 秒), 2 个占位图
3. 分类导航: van-grid, 列数响应式 (移动 4/平板 6/PC 8)
4. 热门商品: 火焰图标 + 商品网格 (移动 2 列/平板 3 列/PC 4 列)
5. 新品推荐: 新品图标 + 同上网格

**数据加载 (loadData):** 并行请求
- categoryApi.getCategoryList() -> categories
- productApi.getHotProducts() -> hotProducts
- productApi.getNewProducts() -> newProducts

**方法:**
- goToCategory(categoryId): 跳转 /client/category?id=categoryId
- goToProduct(productId): 跳转 {name: 'ProductDetail', params: {id}}

**商品卡片:** 图片 (懒加载) + 名称 (单行省略) + 价格 (红色 #ff5722)

### 12.3 Category.vue - 分类页

**布局:** 左侧一级分类侧边栏 (van-sidebar) + 右侧内容区 (二级分类标签 + 商品网格)

**数据流:**
1. 加载一级分类 -> 自动选中第一个
2. 选中一级分类 -> 加载其二级分类
3. 有二级分类 -> 选中第一个 -> 加载商品
4. 无二级分类 -> 直接加载一级分类下商品

**API 调用 (直接使用 request):**
- GET /api/admin/categories/first
- GET /api/admin/categories/second/${parentId}
- GET /api/admin/products/page (pageSize=100)

**路由联动:** 监听 route.query.id 变化, 切换到对应一级分类

### 12.4 Cart.vue - 购物车

**空状态:** van-empty + "去逛逛"按钮

**购物车内容:**
- van-checkbox-group 多选
- 每项: 复选框 + 商品图片 (80x80) + 名称 + 价格 + 数量步进器 + 删除图标
- van-submit-bar 底部提交栏: 全选复选框 + 总价 + 提交订单按钮

**计算属性:**
- cartItems: 从 cartStore 获取
- allChecked: 全选状态 (双向)
- totalPrice: 选中商品的总价

**方法:**
- toggleAll(): 全选/取消全选
- updateQuantity(productId, quantity): 调用 cartStore.updateQuantity
- removeFromCart(productId): 调用 cartStore.removeFromCart + 移除选中项
- onSubmit(): 跳转 {name: 'OrderCreate', query: {items: 选中 ID 逗号拼接}}
- goToHome(): 跳转首页

### 12.5 Login.vue - 客户端登录

**表单:** Vant Form
- 用户名 (必填)
- 密码 (必填)

**登录逻辑 (handleLogin):**
1. POST /api/auth/login 发送 {username, password}
2. 保存 token 和 userInfo 到 store
3. 跳转到 redirect 参数或 /client/home

**底部:** "没有账号?去注册" 按钮

### 12.6 Register.vue - 客户端注册

**表单字段:**
- 用户名 (必填)
- 密码 (必填)
- 确认密码 (必填 + 自定义验证: 两次密码一致)
- 昵称 (可选, 默认使用用户名)
- 手机号 (正则验证: /^1[3-9]\d{9}$/)

**注册逻辑 (handleRegister):**
1. POST /api/auth/register 发送 {username, password, nickname, phone}
2. 成功后跳转 /login

**底部:** "已有账号?去登录" 按钮

### 12.7 Profile.vue - 个人中心

**已登录状态:**
- 头像 + 昵称 + 手机号
- 菜单: 我的订单、收货地址、我的收藏、账号设置
- 退出登录按钮

**未登录状态:**
- 默认头像图标 + "点击登录"

**头像 URL 处理 (avatarUrl computed):**
- base64 / http(s) URL -> 直接使用
- 相对路径 -> 拼接 VITE_API_BASE_URL

**方法:**
- goToLogin(): 跳转登录页
- goToOrders(): 跳转 {name: 'OrderList'} (需登录)
- goToAddress(): 跳转 {name: 'AddressList'} (需登录)
- goToFavorite(): 跳转 {name: 'FavoriteList'} (需登录)
- goToSettings(): 跳转 /client/settings (需登录)
- handleLogout(): 确认后调用 userStore.logout()

**样式:** 渐变头部 (135deg, #667eea -> #764ba2)

### 12.8 UserSettings.vue - 账号设置

**表单字段:**
- 头像: 图片预览 + 文件上传 (限制 2MB, 转 base64)
- 昵称: 文本输入
- 手机号: tel 类型输入
- 邮箱: email 类型输入
- 性别: ActionSheet 选择器 (男=1/女=0)
- 生日: DatePicker 弹出选择器

**数据加载 (loadUserInfo):**
- GET /api/user/profile
- 填充表单 + 同步更新 userStore

**保存逻辑 (handleSave):**
- PUT /api/user/profile 发送表单数据
- 更新 userStore.userInfo
- 返回上一页

**方法:**
- triggerAvatarUpload(): 触发隐藏 file input
- handleAvatarChange(event): 文件大小检查 + FileReader 转 base64
- onGenderSelect(action): 设置性别
- onBirthdayConfirm({selectedValues}): 设置生日 (格式: YYYY-MM-DD)
- goBack(): router.back()

---

## 十三、CSS 资源 (src/assets/)

### 13.1 base.css

**CSS 变量体系:**
- 颜色调色板 (源自 vuejs/theme): white/black/indigo 系列
- 分隔线颜色: light/dark 两套
- 文本颜色: light/dark 两套
- 语义化变量: background/border/heading/text
- 支持暗色模式 (prefers-color-scheme: dark)

**全局样式:**
- box-sizing: border-box
- body: min-height 100vh, Inter 字体栈, 15px 基础字号, 抗锯齿渲染

### 13.2 main.css

- 导入 base.css
- #app: max-width 1280px, 居中, padding 2rem
- 链接样式: hsla(160, 100%, 37%, 1) 绿色
- 桌面端 (>=1024px): body flex 居中, #app 双列 grid

### 13.3 responsive.css

**CSS 变量:**
- 断点: --breakpoint-mobile: 768px, --breakpoint-tablet: 1024px, --breakpoint-desktop: 1200px
- 间距: xs(4px), sm(8px), md(16px), lg(24px), xl(32px)
- 字号: xs(12px), sm(14px), md(16px), lg(18px), xl(20px)

**工具类:**
- GPU 加速: .gpu-accelerate (translateZ(0) + will-change)
- 显示/隐藏: .hide-mobile, .show-mobile, .hide-tablet, .show-tablet, .hide-desktop, .show-desktop
- 响应式容器: .container (mobile:100%, tablet:750px, desktop:970px, 1200+:1170px)
- 响应式网格: .grid, .grid-cols-1/2/3/4 (移动端降级为 1 列, 平板 3/4 列降为 2 列)
- 响应式弹性: .flex-responsive (移动端 column 方向)
- 图片懒加载占位: img[loading="lazy"] 灰色背景
- 平滑滚动: scroll-behavior: smooth
- 触摸优化: 触摸设备上 button/a/input/select/textarea 最小 44x44px

---

## 十四、完整 API 调用汇总

| 来源 | 方法 | 路径 | 用途 |
|------|------|------|------|
| user.js | POST | /api/admin/auth/login | 登录 |
| user.js | POST | /api/admin/auth/register | 注册 |
| user.js | GET | /api/user/profile | 获取用户信息 |
| user.js | PUT | /api/user/profile | 更新用户信息 |
| user.js | POST | /api/admin/auth/logout | 退出登录 |
| product.js | GET | /api/admin/products/page | 分页商品列表 |
| product.js | GET | /api/admin/products/${id} | 商品详情 |
| product.js | GET | /api/admin/products/hot | 热门商品 |
| product.js | GET | /api/admin/products/new | 新品推荐 |
| product.js | GET | /api/admin/products/category/${categoryId} | 按分类商品 |
| category.js | GET | /api/admin/categories/page | 分类列表 |
| category.js | GET | /api/admin/categories/tree | 分类树 |
| category.js | GET | /api/admin/categories/${id} | 分类详情 |
| order.js | POST | /api/admin/orders | 创建订单 |
| order.js | GET | /api/admin/orders/page | 订单列表 |
| order.js | GET | /api/admin/orders/${id} | 订单详情 |
| order.js | PUT | /api/admin/orders/${id}/cancel | 取消订单 |
| order.js | PUT | /api/admin/orders/${id}/pay | 支付订单 |
| admin/Login.vue | POST | /api/auth/login | 管理员登录 |
| admin/Dashboard.vue | GET | /api/admin/dashboard/stats | 仪表盘统计 |
| admin/User.vue | GET | /api/admin/users/page | 用户分页列表 |
| admin/User.vue | GET | /api/admin/users/export | 导出用户 Excel |
| admin/User.vue | DELETE | /api/admin/users/${id} | 删除用户 |
| admin/Product.vue | GET | /api/admin/categories/first | 一级分类 |
| admin/Product.vue | GET | /api/admin/categories/second/${parentId} | 二级分类 |
| admin/Product.vue | GET | /api/admin/products/page | 商品分页 |
| admin/Product.vue | GET | /api/admin/products/export | 导出商品 Excel |
| admin/Product.vue | DELETE | /api/admin/categories/${id} | 删除分类 |
| admin/Product.vue | DELETE | /api/admin/products/${id} | 删除商品 |
| admin/Order.vue | GET | /api/admin/orders/page | 订单分页 |
| admin/Order.vue | GET | /api/admin/orders/export | 导出订单 Excel |
| client/Login.vue | POST | /api/auth/login | 客户端登录 |
| client/Register.vue | POST | /api/auth/register | 客户端注册 |
| client/Category.vue | GET | /api/admin/categories/first | 一级分类 |
| client/Category.vue | GET | /api/admin/categories/second/${parentId} | 二级分类 |
| client/Category.vue | GET | /api/admin/products/page | 分类商品 |
| client/UserSettings.vue | GET | /api/user/profile | 获取用户信息 |
| client/UserSettings.vue | PUT | /api/user/profile | 更新用户信息 |

---

## 十五、架构总结

### 双端设计
- **客户端 (C 端):** Vant 移动端组件 + 响应式布局, 底部 TabBar 导航
- **管理后台 (B 端):** Element Plus PC 组件, 侧边栏 + 顶部 Header 布局

### 响应式策略
- 断点: Mobile <=768px, Tablet 769-1024px, Desktop >1024px
- CSS 变量 + 工具类 + JS 组合式函数三重响应式支持
- 防抖 resize 监听 + passive 事件优化

### 认证机制
- JWT Bearer Token 认证
- Token 存储在 localStorage
- 路由守卫区分客户端/管理员权限
- 401 响应自动登出

### 数据持久化
- Token: localStorage('token')
- 用户信息: localStorage('userInfo')
- 购物车: localStorage('cartItems')

### 待实现功能
- 用户添加/编辑 (admin/User.vue)
- 分类添加/编辑 (admin/Product.vue)
- 商品添加/编辑 (admin/Product.vue)
- 订单查看详情/发货 (admin/Order.vue)
- Banner 数据动态加载 (client/Home.vue, 当前为占位图)
- ProductDetail/OrderCreate/OrderList/AddressList/FavoriteList 路由页面 (被引用但未在路由表中定义)
