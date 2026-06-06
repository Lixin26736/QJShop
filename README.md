# QJShop - 柒玖商店

一个前后端分离的全功能电商商城系统，包含用户前台（移动端+PC端）和管理后台，适合作为毕业设计项目或学习参考。

## 项目结构

```
QJShop/
├── QJ-Shop-Backend/     # 后端服务 (Spring Boot 3.2)
├── QJ-Shop-Foreground/  # 前端应用 (Vue 3 + Vite)
├── seed_images.sql      # 商品/Banner图片数据填充脚本
└── seed_data.sql        # 商品/订单/评价/规格/地址数据填充脚本
```

---

## 后端 (QJ-Shop-Backend)

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 3.2.5 | 应用框架 |
| Spring Security | - | 安全认证框架 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| Druid | 1.2.20 | 数据库连接池 |
| JWT (jjwt) | 0.12.5 | 身份认证令牌 |
| Knife4j | 4.5.0 | API 接口文档 |
| Hutool | 5.8.25 | Java 工具集 |
| Apache POI | 5.2.5 | Excel 导入导出 |
| Lombok | - | 简化代码工具 |
| DeepSeek API | - | AI 客服对话 |

### 项目结构

```
src/main/java/cn/mikuyun/qjshopbackend/
├── QjShopBackendApplication.java    # 启动类
├── common/                          # 公共类 (ApiResponse/PageResult)
├── config/                          # 配置 (JWT/Security/MyBatis-Plus/CORS/SPA)
├── controller/                      # 控制器层
│   ├── CommonAuthController.java    # 公共认证 (登录/注册)
│   ├── BannerController.java        # 前端Banner展示
│   ├── ProductController.java       # 前端商品搜索/详情
│   ├── ReviewController.java        # 前端评价提交/查看
│   ├── FileUploadController.java    # 图片文件上传
│   ├── admin/                       # 管理后台接口
│   │   ├── AuthController.java      # 管理员登录
│   │   ├── BannerAdminController.java    # Banner CRUD
│   │   ├── CategoryAdminController.java  # 分类管理
│   │   ├── CustomerServiceAdminController.java  # 客服管理
│   │   ├── DashboardController.java     # 仪表盘统计
│   │   ├── OrderInfoAdminController.java # 订单管理+发货
│   │   ├── ProductAdminController.java   # 商品管理
│   │   ├── ReviewAdminController.java    # 评价审核/回复
│   │   └── UserAdminController.java      # 用户管理
│   └── user/                        # 用户端接口
│       ├── AddressController.java        # 收货地址CRUD
│       ├── CustomerServiceClientController.java  # AI客服
│       ├── FavoriteController.java       # 收藏管理
│       ├── OrderController.java          # 下单/订单列表/支付
│       └── UserProfileController.java    # 个人信息
├── dto/                             # 数据传输对象
├── entity/                          # 实体类 (10个表全部映射)
├── exception/                       # 全局异常处理
├── mapper/                          # MyBatis-Plus Mapper (11个)
├── security/                        # JWT 认证过滤器
├── service/                         # 业务逻辑层
│   ├── DeepSeekService.java         # DeepSeek AI 服务
│   └── impl/                        # 实现类 (11个Service)
├── util/                            # 工具类
└── vo/                              # 视图对象
```

### 数据库 (12张表)

| 表名 | 说明 | 初始数据 |
|------|------|----------|
| user | 用户 | 8条 |
| product | 商品 | 40条 |
| category | 分类（二级） | 有base64图标 |
| order_info | 订单 | 12条 |
| order_item | 订单明细 | 18条 |
| review | 评价 | 24条 |
| product_spec | 商品规格 | 57条 |
| address | 收货地址 | 15条 |
| banner | 首页轮播 | 5条(有图) |
| favorite | 收藏 | - |
| cart | 购物车 | - |
| customer_service | 客服消息 | - |

### 数据库配置

- 数据库名: `qj_shop`
- 连接地址: `localhost:3306`
- 用户名/密码: `root`/`root`
- 使用 Druid 连接池，支持慢 SQL 监控和 Druid 监控面板

### API 文档

启动后端服务后访问：http://localhost:8080/doc.html

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin2 | admin123 |
| 普通用户 | zhangsan | (自行注册) |

---

## 前端 (QJ-Shop-Foreground)

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5 | 渐进式前端框架 |
| Vite | 8.0 | 构建工具 |
| Vue Router | 5.0 | 路由管理 |
| Pinia | 3.0 | 状态管理 |
| Element Plus | 2.13 | 管理后台 UI 组件库 |
| Vant | 4.9 | 移动端 UI 组件库 |
| Axios | 1.15 | HTTP 请求库 |

### 页面路由

**用户前台：**

| 路径 | 页面 | 权限 | 说明 |
|------|------|------|------|
| `/client/home` | 首页 | 公开 | Banner+分类+热门+新品+搜索 |
| `/client/category` | 分类/搜索 | 公开 | 侧边栏分类+响应式商品网格 |
| `/client/cart` | 购物车 | 需登录 | 全选/数量/提交订单 |
| `/client/product/:id` | 商品详情 | 公开 | PC京东风双栏+移动端轮播 |
| `/client/checkout` | 确认订单 | 需登录 | 选地址/确认商品/提交支付 |
| `/client/orders` | 我的订单 | 需登录 | 分状态Tab/取消/支付 |
| `/client/order/:id` | 订单详情 | 需登录 | 商品明细+物流状态 |
| `/client/address` | 收货地址 | 需登录 | CRUD+GPS定位+设默认 |
| `/client/favorites` | 我的收藏 | 需登录 | 商品列表+取消收藏 |
| `/client/profile` | 个人中心 | 需登录 | 订单统计+服务入口 |
| `/client/settings` | 账号设置 | 需登录 | 头像(相册/拍照)+资料编辑 |
| `/client/cs` | AI客服 | 公开 | DeepSeek对话+商品推荐卡片 |
| `/login` | 用户登录 | 公开 | - |
| `/register` | 用户注册 | 公开 | - |

**管理后台：**

| 路径 | 页面 | 权限 | 说明 |
|------|------|------|------|
| `/admin/login` | 管理员登录 | 公开 | - |
| `/admin/dashboard` | 仪表盘 | 管理员 | 统计卡片+最近订单+热销 |
| `/admin/user` | 用户管理 | 管理员 | 列表/搜索/CRUD/导出Excel |
| `/admin/category` | 分类管理 | 管理员 | 一级/二级展开CRUD |
| `/admin/product` | 商品管理 | 管理员 | 三级分类树+商品CRUD+导出 |
| `/admin/order` | 订单管理 | 管理员 | 列表/详情/发货/导出 |
| `/admin/banner` | Banner管理 | 管理员 | CRUD+图片上传 |
| `/admin/review` | 评价管理 | 管理员 | 审核/回复/隐藏 |
| `/admin/cs` | 客服消息 | 管理员 | 用户列表+回复 |
| `/admin/settings` | 系统设置 | 管理员 | 修改密码+关于 |

---

## 快速启动

### 环境要求

- **后端**: JDK 17+、Maven 3.6+、MySQL 8.0
- **前端**: Node.js 20.19+ 或 22.12+

### 1. 数据库准备

```sql
CREATE DATABASE qj_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

然后导入项目根目录的 `qj_shop.sql` 初始化数据。

### 2. 启动后端

```bash
cd QJ-Shop-Backend
# 修改 src/main/resources/application.properties 中的数据库连接信息
mvnw spring-boot:run
```

后端运行在 http://localhost:8080

### 3. 启动前端

```bash
cd QJ-Shop-Foreground
npm install
npm run dev
```

前端运行在 http://localhost:3000

---

## 生产部署打包

### 一键打包

```bash
# 1. 打包前端
cd QJ-Shop-Foreground
npm run build

# 2. 复制dist到后端静态资源目录
cp -r dist/* ../QJ-Shop-Backend/src/main/resources/static/

# 3. 打包后端jar (包含前端)
cd ../QJ-Shop-Backend
mvnw package -DskipTests

# 4. 运行jar
java -jar target/QJ-Shop-Backend-0.0.1-SNAPSHOT.jar
```

访问 http://localhost:8080 即可使用完整系统。

---

## 项目特点

- **前后端分离**: Vue 3 + Spring Boot，RESTful API
- **JWT 认证**: 无状态 Token，支持用户端+管理端双端登录
- **RBAC 权限**: 普通用户/管理员角色隔离，路由守卫
- **双端适配 UI**: 移动端 Vant + PC 端京东风格布局，响应式适配
- **AI 客服**: 集成 DeepSeek API，智能对话+关联商品推荐
- **GPS 定位**: 浏览器定位+IP 定位双重回退，自动填充地址
- **图片处理**: SVG 渐变占位图+Unsplash 网络图片+base64 分类图标
- **导出功能**: 用户/商品/订单支持 Excel 导出
- **API 文档**: Knife4j 自动生成在线接口文档
- **逻辑删除**: 所有数据操作支持逻辑删除
- **一体化部署**: 前端打包放入后端 jar，单文件部署
