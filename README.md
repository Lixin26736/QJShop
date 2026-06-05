# QJShop - 柒玖商店

一个前后端分离的电商商城系统，包含用户前台和管理后台，适合作为毕业设计项目。

## 项目结构

```
QJShop/
├── QJ-Shop-Backend/     # 后端服务 (Spring Boot)
└── QJ-Shop-Foreground/  # 前端应用 (Vue 3)
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

### 项目结构

```
src/main/java/cn/mikuyun/qjshopbackend/
├── QjShopBackendApplication.java   # 启动类
├── common/                          # 公共类 (分页结果等)
├── config/                          # 配置类 (JWT、Security、MyBatis-Plus)
├── controller/                      # 控制器层
│   ├── CommonAuthController.java    # 公共认证 (登录/注册)
│   ├── admin/                       # 管理后台接口
│   │   ├── AuthController.java
│   │   ├── CategoryAdminController.java
│   │   ├── DashboardController.java
│   │   ├── OrderInfoAdminController.java
│   │   ├── ProductAdminController.java
│   │   └── UserAdminController.java
│   └── user/                        # 用户端接口
│       └── UserProfileController.java
├── dto/                             # 数据传输对象
├── entity/                          # 实体类 (User, Product, Category, OrderInfo)
├── exception/                       # 全局异常处理
├── mapper/                          # MyBatis-Plus Mapper 接口
├── security/                        # JWT 认证过滤器
├── service/                         # 业务逻辑层
├── util/                            # 工具类 (JWT 工具等)
└── vo/                              # 视图对象
```

### 数据库配置

- 数据库名: `qj_shop`
- 连接地址: `localhost:3306`
- 使用 Druid 连接池，支持慢 SQL 监控和 Druid 监控面板

### API 文档

项目集成了 Knife4j (基于 OpenAPI 3)，启动后端服务后访问：

- http://localhost:8080/doc.html

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

### 项目结构

```
src/
├── App.vue              # 根组件
├── main.js              # 入口文件
├── api/                 # API 接口模块
│   ├── category.js      # 分类接口
│   ├── order.js         # 订单接口
│   ├── product.js       # 商品接口
│   └── user.js          # 用户接口
├── assets/              # 静态资源
├── router/              # 路由配置
│   └── index.js
├── store/               # Pinia 状态管理
│   ├── cart.js           # 购物车状态
│   └── user.js           # 用户状态
├── utils/               # 工具函数
│   ├── envConfig.js     # 环境配置
│   ├── request.js       # Axios 封装 (请求/响应拦截)
│   └── responsive.js    # 响应式适配
└── views/               # 页面视图
    ├── admin/            # 管理后台页面
    │   ├── Dashboard.vue # 仪表盘
    │   ├── Layout.vue    # 后台布局
    │   ├── Login.vue     # 管理员登录
    │   ├── Order.vue     # 订单管理
    │   ├── Product.vue   # 商品管理
    │   └── User.vue      # 用户管理
    └── client/           # 用户前台页面
        ├── Cart.vue       # 购物车
        ├── Category.vue   # 商品分类
        ├── Home.vue       # 首页
        ├── Layout.vue     # 前台布局
        ├── Login.vue      # 用户登录
        ├── Profile.vue    # 个人中心
        ├── Register.vue   # 用户注册
        └── UserSettings.vue # 账号设置
```

### 页面路由

| 路径 | 页面 | 权限 |
|------|------|------|
| `/client/home` | 首页 | 公开 |
| `/client/category` | 商品分类 | 公开 |
| `/client/cart` | 购物车 | 需登录 |
| `/client/profile` | 个人中心 | 需登录 |
| `/client/settings` | 账号设置 | 需登录 |
| `/login` | 用户登录 | 公开 |
| `/register` | 用户注册 | 公开 |
| `/admin/login` | 管理员登录 | 公开 |
| `/admin/dashboard` | 仪表盘 | 需管理员 |
| `/admin/user` | 用户管理 | 需管理员 |
| `/admin/product` | 商品管理 | 需管理员 |
| `/admin/order` | 订单管理 | 需管理员 |

---

## 快速启动

### 环境要求

- **后端**: JDK 17+、Maven 3.6+、MySQL 8.0
- **前端**: Node.js 20.19+ 或 22.12+

### 1. 数据库准备

创建 MySQL 数据库：

```sql
CREATE DATABASE qj_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 2. 启动后端

```bash
cd QJ-Shop-Backend
# 修改 src/main/resources/application.properties 中的数据库连接信息
mvn spring-boot:run
```

后端默认运行在 http://localhost:8080

### 3. 启动前端

```bash
cd QJ-Shop-Foreground
npm install
npm run dev
```

前端默认运行在 http://localhost:5173

---

## 项目特点

- **前后端分离**: 前端 Vue 3 + 后端 Spring Boot，通过 RESTful API 通信
- **JWT 认证**: 基于 JWT Token 的无状态身份认证，支持用户端和管理端双端登录
- **RBAC 权限**: 区分普通用户 (role=0) 和管理员 (role=1) 角色，路由守卫控制访问权限
- **双端 UI**: 用户前台使用 Vant (移动端风格)，管理后台使用 Element Plus (桌面端风格)
- **API 文档**: 集成 Knife4j，自动生成在线接口文档
- **逻辑删除**: 数据库操作支持逻辑删除，保障数据安全
