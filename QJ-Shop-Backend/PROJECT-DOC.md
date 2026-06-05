# QJ-Shop-Backend 项目文档

## 一、项目概述

| 属性 | 值 |
|------|-----|
| 项目名称 | QJ-Shop-Backend (QJ商城后台) |
| GroupId | cn.mikuyun |
| ArtifactId | QJ-Shop-Backend |
| 版本 | 0.0.1-SNAPSHOT |
| Java 版本 | 17 |
| Spring Boot 版本 | 3.2.5 |
| 基础包路径 | cn.mikuyun.qjshopbackend |
| 服务端口 | 8080 |
| 主启动类 | QjShopBackendApplication.java (@MapperScan("cn.mikuyun.qjshopbackend.mapper")) |

---

## 二、技术栈与依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-parent | 3.2.5 | Spring Boot 父POM |
| spring-boot-starter-web | 继承 | Web MVC |
| spring-boot-starter-jdbc | 继承 | JDBC 支持 |
| spring-boot-starter-security | 继承 | Spring Security |
| spring-boot-starter-validation | 继承 | 参数校验 (Jakarta Validation) |
| mysql-connector-j | 8.0.33 | MySQL 驱动 |
| mybatis-plus-spring-boot3-starter | 3.5.5 | MyBatis-Plus (Spring Boot 3.x) |
| druid-spring-boot-3-starter | 1.2.20 | Druid 连接池 (Spring Boot 3.x) |
| jjwt-api | 0.12.5 | JWT API |
| jjwt-impl | 0.12.5 | JWT 实现 |
| jjwt-jackson | 0.12.5 | JWT Jackson 支持 |
| hutool-all | 5.8.25 | Hutool 工具库 |
| poi-ooxml | 5.2.5 | Apache POI Excel 导出 |
| commons-lang3 | 继承 | Apache Commons Lang3 |
| knife4j-openapi3-jakarta-spring-boot-starter | 4.5.0 | Knife4j API 文档 (OpenAPI 3) |
| lombok | 继承 | Lombok |
| spring-boot-devtools | 继承 | 开发热部署 |
| spring-boot-configuration-processor | 继承 | 配置元数据 |

---

## 三、项目目录结构

```
QJ-Shop-Backend/
├── src/
│   ├── main/
│   │   ├── java/cn/mikuyun/qjshopbackend/
│   │   │   ├── QjShopBackendApplication.java        # 启动类
│   │   │   ├── common/                              # 通用类
│   │   │   │   ├── ApiResponse.java                 # 统一响应封装
│   │   │   │   └── PageResult.java                  # 分页结果封装
│   │   │   ├── config/                              # 配置类
│   │   │   │   ├── CorsConfig.java                  # CORS 跨域配置
│   │   │   │   ├── JwtProperties.java               # JWT 属性配置
│   │   │   │   ├── MybatisPlusConfig.java           # MyBatis-Plus 分页插件
│   │   │   │   └── SecurityConfig.java              # Spring Security 配置
│   │   │   ├── controller/                          # 控制器层
│   │   │   │   ├── CommonAuthController.java        # 公共认证(登录/注册)
│   │   │   │   ├── admin/                           # 管理端控制器
│   │   │   │   │   ├── AuthController.java          # 管理员认证
│   │   │   │   │   ├── CategoryAdminController.java # 分类管理
│   │   │   │   │   ├── DashboardController.java     # 仪表盘
│   │   │   │   │   ├── OrderInfoAdminController.java# 订单管理
│   │   │   │   │   ├── ProductAdminController.java  # 商品管理
│   │   │   │   │   └── UserAdminController.java     # 用户管理
│   │   │   │   └── user/                            # 客户端控制器
│   │   │   │       └── UserProfileController.java   # 用户个人资料
│   │   │   ├── dto/                                 # 数据传输对象
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginRequest.java            # 登录请求
│   │   │   │   │   └── LoginResponse.java           # 登录响应
│   │   │   │   └── user/
│   │   │   │       └── UserProfileUpdateRequest.java# 用户资料更新请求
│   │   │   ├── entity/                              # 实体类(对应数据库表)
│   │   │   │   ├── User.java                        # 用户表
│   │   │   │   ├── Product.java                     # 商品表
│   │   │   │   ├── Category.java                    # 分类表
│   │   │   │   └── OrderInfo.java                   # 订单表
│   │   │   ├── exception/                           # 异常处理
│   │   │   │   └── GlobalExceptionHandler.java      # 全局异常处理器
│   │   │   ├── mapper/                              # MyBatis-Plus Mapper
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── ProductMapper.java
│   │   │   │   ├── CategoryMapper.java
│   │   │   │   └── OrderInfoMapper.java             # 含自定义SQL
│   │   │   ├── security/                            # 安全模块
│   │   │   │   └── JwtAuthenticationFilter.java     # JWT 认证过滤器
│   │   │   ├── service/                             # 服务接口
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── OrderInfoService.java
│   │   │   │   ├── DashboardService.java
│   │   │   │   └── impl/                            # 服务实现
│   │   │   │       ├── AuthServiceImpl.java
│   │   │   │       ├── UserServiceImpl.java
│   │   │   │       ├── ProductServiceImpl.java
│   │   │   │       ├── CategoryServiceImpl.java
│   │   │   │       ├── OrderInfoServiceImpl.java
│   │   │   │       └── DashboardServiceImpl.java
│   │   │   ├── util/                                # 工具类
│   │   │   │   ├── JwtTokenUtil.java                # JWT Token 工具
│   │   │   │   └── ExcelExportUtil.java             # Excel 导出工具
│   │   │   └── vo/                                  # 视图对象
│   │   │       └── DashboardStatsVO.java            # 仪表盘统计 VO
│   │   └── resources/
│   │       ├── application.properties                # 应用配置
│   │       ├── static/                              # 静态资源
│   │       └── templates/                           # 模板
│   └── test/
│       └── java/cn/mikuyun/qjshopbackend/
│           └── QjShopBackendApplicationTests.java   # 启动测试类
├── pom.xml                                          # Maven 依赖配置
├── mvnw / mvnw.cmd                                  # Maven Wrapper
└── logs/                                            # 运行日志
```

---

## 四、配置详情 (application.properties)

### 4.1 数据库配置

| 配置项 | 值 |
|--------|-----|
| spring.datasource.driver-class-name | com.mysql.cj.jdbc.Driver |
| spring.datasource.url | jdbc:mysql://localhost:3306/qj_shop?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true |
| spring.datasource.username | root |
| spring.datasource.password | root |

### 4.2 Druid 连接池

| 配置项 | 值 | 说明 |
|--------|-----|------|
| spring.datasource.type | com.alibaba.druid.pool.DruidDataSource | 数据源类型 |
| druid.initial-size | 5 | 初始连接数 |
| druid.min-idle | 5 | 最小空闲连接数 |
| druid.max-active | 20 | 最大活跃连接数 |
| druid.max-wait | 60000 | 获取连接最大等待时间(ms) |
| druid.time-between-eviction-runs-millis | 60000 | 检测间隔时间(ms) |
| druid.min-evictable-idle-time-millis | 300000 | 最小空闲时间(ms) |
| druid.validation-query | SELECT 1 | 验证 SQL |
| druid.test-while-idle | true | 空闲时检测 |
| druid.pool-prepared-statements | true | 缓存 PreparedStatement |
| druid.max-pool-prepared-statement-per-connection-size | 20 | 每连接最大 PS 数 |

### 4.3 Druid 监控

| 配置项 | 值 |
|--------|-----|
| stat-view-servlet.enabled | true |
| stat-view-servlet.url-pattern | /druid/* |
| stat-view-servlet.login-username | admin |
| stat-view-servlet.login-password | admin123 |
| filter.stat.log-slow-sql | true |
| filter.stat.slow-sql-millis | 2000 |

### 4.4 MyBatis-Plus

| 配置项 | 值 | 说明 |
|--------|-----|------|
| mapper-locations | classpath:mapper/*.xml | Mapper XML 位置 |
| type-aliases-package | cn.mikuyun.qjshopbackend.entity | 类型别名包 |
| map-underscore-to-camel-case | true | 驼峰命名转换 |
| cache-enabled | false | 禁用二级缓存 |
| log-impl | StdOutImpl | SQL 日志输出 |
| id-type | auto | 主键自增策略 |
| logic-delete-field | deleted | 逻辑删除字段 |
| logic-delete-value | 1 | 删除值 |
| logic-not-delete-value | 0 | 未删除值 |

### 4.5 Jackson JSON

| 配置项 | 值 |
|--------|-----|
| date-format | yyyy-MM-dd HH:mm:ss |
| time-zone | Asia/Shanghai |
| default-property-inclusion | non_null |

### 4.6 文件上传

| 配置项 | 值 |
|--------|-----|
| max-file-size | 10MB |
| max-request-size | 20MB |

### 4.7 JWT

| 配置项 | 值 | 说明 |
|--------|-----|------|
| jwt.secret | qj-shop-secret-key-2024-... | JWT 密钥 |
| jwt.expiration | 604800000 | 过期时间 7 天(ms) |
| jwt.header | Authorization | 请求头名称 |
| jwt.token-prefix | Bearer | Token 前缀 |

### 4.8 Knife4j

| 配置项 | 值 |
|--------|-----|
| knife4j.enable | true |
| knife4j.setting.language | zh_cn |

---

## 五、数据库实体与表结构

### 5.1 User 用户表

表名: `user`

| 字段 | Java 类型 | 注解 | 说明 |
|------|-----------|------|------|
| id | Long | @TableId | 主键, 自增 |
| username | String | - | 用户名 |
| password | String | - | 密码 (BCrypt 加密) |
| nickname | String | - | 昵称 |
| phone | String | - | 手机号 |
| email | String | - | 邮箱 |
| avatar | String | - | 头像 URL |
| gender | Integer | - | 性别 (0=女, 1=男) |
| birthday | LocalDate | - | 生日 |
| role | Integer | - | 角色 (0=超级管理员, 1=普通管理员) |
| status | Integer | - | 状态 (0=禁用, 1=正常) |
| lastLoginTime | LocalDateTime | - | 最后登录时间 |
| createTime | LocalDateTime | - | 创建时间 |
| updateTime | LocalDateTime | - | 更新时间 |
| deleted | Integer | @TableLogic | 逻辑删除 (0=未删除, 1=已删除) |

### 5.2 Category 分类表

表名: `category`

| 字段 | Java 类型 | 注解 | 说明 |
|------|-----------|------|------|
| id | Long | @TableId | 主键, 自增 |
| parentId | Long | - | 父分类 ID (0=一级分类) |
| name | String | - | 分类名称 |
| icon | String | - | 图标 |
| sortOrder | Integer | - | 排序值 |
| status | Integer | - | 状态 (0=禁用, 1=启用) |
| createTime | LocalDateTime | - | 创建时间 |
| updateTime | LocalDateTime | - | 更新时间 |
| deleted | Integer | @TableLogic | 逻辑删除 |

### 5.3 Product 商品表

表名: `product`

| 字段 | Java 类型 | 注解 | 说明 |
|------|-----------|------|------|
| id | Long | @TableId | 主键, 自增 |
| categoryId | Long | - | 所属分类 ID |
| name | String | - | 商品名称 |
| subtitle | String | - | 副标题 |
| mainImage | String | - | 主图 URL |
| detailImages | String | - | 详情图片 (多图) |
| price | BigDecimal | - | 售价 |
| originalPrice | BigDecimal | - | 原价 |
| stock | Integer | - | 库存 |
| sales | Integer | - | 销量 |
| description | String | - | 商品描述 |
| detailContent | String | - | 详情内容 (富文本) |
| status | Integer | - | 状态 (0=下架, 1=上架) |
| isHot | Integer | - | 是否热销 (0=否, 1=是) |
| isNew | Integer | - | 是否新品 (0=否, 1=是) |
| sortOrder | Integer | - | 排序值 |
| createTime | LocalDateTime | - | 创建时间 |
| updateTime | LocalDateTime | - | 更新时间 |
| deleted | Integer | @TableLogic | 逻辑删除 |

### 5.4 OrderInfo 订单表

表名: `order_info`

| 字段 | Java 类型 | 注解 | 说明 |
|------|-----------|------|------|
| id | Long | @TableId | 主键, 自增 |
| orderNo | String | - | 订单号 |
| userId | Long | - | 用户 ID |
| addressId | Long | - | 收货地址 ID |
| totalAmount | BigDecimal | - | 订单总金额 |
| payAmount | BigDecimal | - | 实付金额 |
| freight | BigDecimal | - | 运费 |
| payType | Integer | - | 支付方式 (1=微信, 2=支付宝) |
| payTime | LocalDateTime | - | 支付时间 |
| deliveryTime | LocalDateTime | - | 发货时间 |
| receiveTime | LocalDateTime | - | 收货时间 |
| status | Integer | - | 订单状态 (0=待付款, 1=待发货, 2=待收货, 3=已完成, 4=已取消) |
| remark | String | - | 备注 |
| createTime | LocalDateTime | - | 创建时间 |
| updateTime | LocalDateTime | - | 更新时间 |
| deleted | Integer | @TableLogic | 逻辑删除 |

---

## 六、DTO / VO 数据传输对象

### 6.1 LoginRequest (登录请求)

| 字段 | 类型 | 校验 | 说明 |
|------|------|------|------|
| username | String | @NotBlank(message="用户名不能为空") | 用户名 |
| password | String | @NotBlank(message="密码不能为空") | 密码 |

### 6.2 LoginResponse (登录响应)

| 字段 | 类型 | 说明 |
|------|------|------|
| token | String | JWT 令牌 |
| userId | Long | 用户 ID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| role | Integer | 角色 |

### 6.3 UserProfileUpdateRequest (用户资料更新请求)

| 字段 | 类型 | 校验 | 说明 |
|------|------|------|------|
| nickname | String | - | 昵称 |
| phone | String | @Pattern(regexp="^1\d{10}$", message="手机号格式不正确") | 手机号 |
| email | String | @Email(message="邮箱格式不正确") | 邮箱 |
| avatar | String | - | 头像 URL |
| gender | Integer | - | 性别 |
| birthday | LocalDate | - | 生日 |

### 6.4 DashboardStatsVO (仪表盘统计视图对象)

| 字段 | 类型 | 说明 |
|------|------|------|
| userCount | Long | 用户总数 |
| productCount | Long | 商品总数 |
| orderCount | Long | 订单总数 |
| todayOrderCount | Long | 今日订单数 |
| totalSales | BigDecimal | 总销售额 |
| todaySales | BigDecimal | 今日销售额 |

---

## 七、通用类

### 7.1 ApiResponse\<T\> (统一 API 响应)

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码 (200=成功, 400=参数错误, 403=无权限, 500=服务器异常) |
| message | String | 消息 |
| data | T | 数据 |

静态方法:
- `success(T data)` - 成功响应 (code=200, message="success")
- `success()` - 成功无数据响应
- `fail(String message)` - 失败响应 (code=500)
- `error(int codeNum, String message)` - 自定义错误响应

### 7.2 PageResult\<T\> (分页结果)

| 字段 | 类型 | 说明 |
|------|------|------|
| total | Long | 总记录数 |
| pageNum | Long | 当前页码 |
| pageSize | Long | 每页大小 |
| records | List\<T\> | 数据列表 |

---

## 八、全局异常处理器

| 异常类型 | 响应码 | 响应消息 |
|----------|--------|----------|
| RuntimeException | 500 | e.getMessage() |
| ConstraintViolationException | 400 | e.getMessage() |
| MethodArgumentTypeMismatchException | 400 | e.getMessage() |
| IllegalArgumentException | 400 | e.getMessage() |
| MethodArgumentNotValidException | 400 | 字段错误消息 / "参数校验失败" |
| AccessDeniedException | 403 | "无权限访问" |
| Exception | 500 | "服务器异常" |

---

## 九、安全与配置类

### 9.1 SecurityConfig

- CSRF: 禁用
- Session: STATELESS (无状态)
- 授权: 所有请求 permitAll (由 JWT Filter 自行处理认证)
- 过滤器: 在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationFilter
- 密码编码器: BCryptPasswordEncoder

### 9.2 CorsConfig

- 允许域名: * (所有域名)
- 允许方法: GET, POST, PUT, DELETE, OPTIONS, PATCH
- 允许请求头: * (所有)
- 允许凭证: true
- 预检缓存: 3600 秒

### 9.3 MybatisPlusConfig

- 分页插件: PaginationInnerInterceptor (DbType.MYSQL)

### 9.4 JwtProperties

| 属性 | 类型 | 对应配置 | 说明 |
|------|------|----------|------|
| secret | String | jwt.secret | JWT 密钥 |
| expiration | Long | jwt.expiration | 过期时间 (ms) |
| header | String | jwt.header | 请求头名 |
| tokenPrefix | String | jwt.token-prefix | Token 前缀 |

### 9.5 JwtAuthenticationFilter

继承 OncePerRequestFilter, 处理流程:
1. 从请求头 `Authorization` 获取 Token
2. 验证 Token 是否以 `Bearer ` 开头
3. 解析 Token 获取 Claims
4. 从 Claims 提取 `username` 和 `role`
5. 角色映射: role==0 -> ROLE_SUPER_ADMIN, 其他 -> ROLE_ADMIN
6. 构建 UsernamePasswordAuthenticationToken 设置到 SecurityContext
7. 解析失败则清除 SecurityContext

### 9.6 JwtTokenUtil

- `generateToken(Long userId, String username, Integer role)`: 生成 JWT Token, Claims 包含 userId, username, role
- `parseToken(String token)`: 解析 Token 返回 Claims
- `secretKey()`: 从配置密钥生成 HMAC-SHA Key

---

## 十、工具类

### 10.1 ExcelExportUtil

`exportExcel(HttpServletResponse response, String fileName, String sheetName, String[] headers, List<List<Object>> dataList)`

- 设置响应头 Content-Type = application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
- 文件名 URL 编码, 扩展名 .xlsx
- 使用 XSSFWorkbook 创建 Excel
- 表头样式: 灰色背景, 居中, 粗体
- 数据样式: 居中
- 自动调整列宽

---

## 十一、Mapper 层

| Mapper | 继承 | 自定义方法 |
|--------|------|------------|
| UserMapper | BaseMapper\<User\> | 无 |
| CategoryMapper | BaseMapper\<Category\> | 无 |
| ProductMapper | BaseMapper\<Product\> | 无 |
| OrderInfoMapper | BaseMapper\<OrderInfo\> | sumPayAmount(), countTodayOrders(start, end), sumTodayPayAmount(start, end) |

OrderInfoMapper 自定义 SQL:

| 方法 | SQL | 返回类型 | 说明 |
|------|-----|----------|------|
| sumPayAmount() | SELECT ifnull(sum(pay_amount), 0) FROM order_info | BigDecimal | 总支付金额 |
| countTodayOrders(start, end) | SELECT count(1) FROM order_info WHERE create_time >= #{start} AND create_time < #{end} | Long | 今日订单数 |
| sumTodayPayAmount(start, end) | SELECT ifnull(sum(pay_amount), 0) FROM order_info WHERE create_time >= #{start} AND create_time < #{end} | BigDecimal | 今日支付金额 |

---

## 十二、Service 层业务逻辑

### 12.1 AuthServiceImpl

- `login(LoginRequest)`: 根据 username 查询用户 -> 校验状态 -> BCrypt 密码校验 -> 生成 JWT Token -> 返回 LoginResponse

### 12.2 CategoryServiceImpl

- `page(pageNum, pageSize, keyword, status, parentId)`: 分页查询, keyword 模糊匹配 name, status/parentId 精确匹配, 按 ID 降序
- `getById(id)`: 根据 ID 查询
- `save(category)`: 插入 (置空 ID)
- `update(category)`: 根据 ID 更新
- `delete(id)`: 逻辑删除
- `getFirstCategories()`: 查询 parentId=0 且 status=1, 按 sortOrder 升序、ID 降序
- `getSecondCategories(parentId)`: 查询指定 parentId 且 status=1, 按 sortOrder 升序、ID 降序

### 12.3 ProductServiceImpl

- `page(pageNum, pageSize, keyword, status, categoryId)`: 分页查询, keyword 模糊匹配 name, status/categoryId 精确匹配, 按 ID 降序
- `listAll(keyword, status, categoryId)`: 全量查询 (用于导出)
- `getById(id)`: 根据 ID 查询
- `save(product)`: 插入 (置空 ID)
- `update(product)`: 根据 ID 更新
- `delete(id)`: 逻辑删除
- `getHotProducts()`: 查询 status=1 且 isHot=1, 按 sales 降序, LIMIT 10
- `getNewProducts()`: 查询 status=1 且 isNew=1, 按 createTime 降序, LIMIT 10

### 12.4 OrderInfoServiceImpl

- `page(pageNum, pageSize, orderNo, status, userId)`: 分页查询, orderNo 模糊匹配, status/userId 精确匹配, 按 ID 降序
- `listAll(orderNo, status, userId)`: 全量查询 (用于导出)
- `getById(id)`: 根据 ID 查询
- `save(orderInfo)`: 插入 (置空 ID)
- `update(orderInfo)`: 根据 ID 更新
- `delete(id)`: 逻辑删除

### 12.5 UserServiceImpl

- `page(pageNum, pageSize, keyword, role, status)`: 分页查询, keyword 模糊匹配 username/nickname/phone (OR), role/status 精确匹配, 按 ID 降序
- `listAll(keyword, role, status)`: 全量查询 (用于导出)
- `getById(id)`: 根据 ID 查询
- `save(user)`: 插入, 密码 BCrypt 加密, 密码为空则抛异常
- `update(user)`: 更新, 密码不为空则 BCrypt 加密, 密码为空则置 null (不更新密码)
- `delete(id)`: 逻辑删除
- `getByUsername(username)`: 根据 username 查询 (limit 1)
- `updateProfileByUsername(username, request)`: 根据 username 查找用户, 校验 gender (必须为 0 或 1), 更新 nickname/phone/email/avatar/gender/birthday

### 12.6 DashboardServiceImpl

- `stats()`: 统计仪表盘数据 (userCount, productCount, orderCount, todayOrderCount, totalSales, todaySales)

---

## 十三、API 接口文档

### 13.1 公共认证接口 `/api/auth`

| 方法 | 路径 | 请求体 | 响应 | 说明 |
|------|------|--------|------|------|
| POST | /api/auth/login | LoginRequest {username, password} | ApiResponse\<Map\> {token, user{id,username,nickname,phone,email,avatar,gender,birthday,role}} | 用户登录 |
| POST | /api/auth/register | Map {username, password, nickname?, phone?} | ApiResponse\<Void\> | 用户注册 (默认 role=0, status=1) |

### 13.2 管理员认证接口 `/api/admin/auth`

| 方法 | 路径 | 参数 | 响应 | 说明 |
|------|------|------|------|------|
| GET/POST | /api/admin/auth/login | username(String), password(String) | ApiResponse\<LoginResponse\> {token, userId, username, nickname, role} | 管理员登录 |

### 13.3 分类管理接口 `/api/admin/categories`

| 方法 | 路径 | 参数 | 响应 | 说明 |
|------|------|------|------|------|
| GET | /api/admin/categories/first | - | ApiResponse\<List\<Category\>\> | 获取一级分类 (parentId=0, status=1) |
| GET | /api/admin/categories/second/{parentId} | parentId(Path) | ApiResponse\<List\<Category\>\> | 获取二级分类 (status=1) |
| GET | /api/admin/categories/page | pageNum, pageSize, keyword?, status?, parentId? | ApiResponse\<PageResult\<Category\>\> | 分类分页查询 |
| GET | /api/admin/categories/{id} | id(Path) | ApiResponse\<Category\> | 获取分类详情 |
| POST | /api/admin/categories | Category JSON | ApiResponse\<Void\> | 创建分类 |
| PUT | /api/admin/categories/{id} | id(Path) + Category JSON | ApiResponse\<Void\> | 更新分类 |
| DELETE | /api/admin/categories/{id} | id(Path) | ApiResponse\<Void\> | 删除分类 (逻辑删除) |

### 13.4 商品管理接口 `/api/admin/products`

| 方法 | 路径 | 参数 | 响应 | 说明 |
|------|------|------|------|------|
| GET | /api/admin/products/page | pageNum, pageSize, keyword?, status?, categoryId? | ApiResponse\<PageResult\<Product\>\> | 商品分页查询 |
| GET | /api/admin/products/export | keyword?, status?, categoryId? | Excel 文件流 | 导出商品 Excel |
| GET | /api/admin/products/hot | - | ApiResponse\<List\<Product\>\> | 热销商品 (前 10) |
| GET | /api/admin/products/new | - | ApiResponse\<List\<Product\>\> | 新品商品 (前 10) |
| GET | /api/admin/products/{id} | id(Path) | ApiResponse\<Product\> | 获取商品详情 |
| POST | /api/admin/products | Product JSON | ApiResponse\<Void\> | 创建商品 |
| PUT | /api/admin/products/{id} | id(Path) + Product JSON | ApiResponse\<Void\> | 更新商品 |
| DELETE | /api/admin/products/{id} | id(Path) | ApiResponse\<Void\> | 删除商品 (逻辑删除) |

商品导出 Excel 列: ID, 商品名称, 价格, 原价, 库存, 销量, 状态, 创建时间

### 13.5 订单管理接口 `/api/admin/orders`

| 方法 | 路径 | 参数 | 响应 | 说明 |
|------|------|------|------|------|
| GET | /api/admin/orders/page | pageNum, pageSize, orderNo?, status?, userId? | ApiResponse\<PageResult\<OrderInfo\>\> | 订单分页查询 |
| GET | /api/admin/orders/export | orderNo?, status?, userId? | Excel 文件流 | 导出订单 Excel |
| GET | /api/admin/orders/{id} | id(Path) | ApiResponse\<OrderInfo\> | 获取订单详情 |
| POST | /api/admin/orders | OrderInfo JSON | ApiResponse\<Void\> | 创建订单 |
| PUT | /api/admin/orders/{id} | id(Path) + OrderInfo JSON | ApiResponse\<Void\> | 更新订单 |
| DELETE | /api/admin/orders/{id} | id(Path) | ApiResponse\<Void\> | 删除订单 (逻辑删除) |

订单导出 Excel 列: ID, 订单号, 用户 ID, 订单金额, 支付方式, 订单状态, 创建时间

### 13.6 用户管理接口 `/api/admin/users`

| 方法 | 路径 | 参数 | 响应 | 说明 |
|------|------|------|------|------|
| GET | /api/admin/users/page | pageNum, pageSize, keyword?, role?, status? | ApiResponse\<PageResult\<User\>\> | 用户分页查询 |
| GET | /api/admin/users/export | keyword?, role?, status? | Excel 文件流 | 导出用户 Excel |
| GET | /api/admin/users/{id} | id(Path) | ApiResponse\<User\> | 获取用户详情 |
| POST | /api/admin/users | User JSON | ApiResponse\<Void\> | 创建用户 (密码 BCrypt 加密) |
| PUT | /api/admin/users/{id} | id(Path) + User JSON | ApiResponse\<Void\> | 更新用户 |
| DELETE | /api/admin/users/{id} | id(Path) | ApiResponse\<Void\> | 删除用户 (逻辑删除) |

用户导出 Excel 列: ID, 用户名, 昵称, 手机号, 邮箱, 角色, 状态, 创建时间

### 13.7 仪表盘接口 `/api/admin/dashboard`

| 方法 | 路径 | 响应 | 说明 |
|------|------|------|------|
| GET | /api/admin/dashboard/stats | ApiResponse\<DashboardStatsVO\> | 仪表盘统计数据 |

### 13.8 用户个人资料接口 `/api/user/profile`

| 方法 | 路径 | 请求体 | 响应 | 说明 |
|------|------|--------|------|------|
| GET | /api/user/profile | - | ApiResponse\<User\> (password 置 null) | 获取当前用户资料 |
| PUT | /api/user/profile | UserProfileUpdateRequest JSON | ApiResponse\<Void\> | 更新当前用户资料 |

---

## 十四、API 接口汇总

| 序号 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 1 | POST | /api/auth/login | 用户登录 |
| 2 | POST | /api/auth/register | 用户注册 |
| 3 | GET/POST | /api/admin/auth/login | 管理员登录 |
| 4 | GET | /api/admin/categories/first | 获取一级分类 |
| 5 | GET | /api/admin/categories/second/{parentId} | 获取二级分类 |
| 6 | GET | /api/admin/categories/page | 分类分页查询 |
| 7 | GET | /api/admin/categories/{id} | 获取分类详情 |
| 8 | POST | /api/admin/categories | 创建分类 |
| 9 | PUT | /api/admin/categories/{id} | 更新分类 |
| 10 | DELETE | /api/admin/categories/{id} | 删除分类 |
| 11 | GET | /api/admin/products/page | 商品分页查询 |
| 12 | GET | /api/admin/products/export | 导出商品 Excel |
| 13 | GET | /api/admin/products/hot | 获取热销商品 |
| 14 | GET | /api/admin/products/new | 获取新品商品 |
| 15 | GET | /api/admin/products/{id} | 获取商品详情 |
| 16 | POST | /api/admin/products | 创建商品 |
| 17 | PUT | /api/admin/products/{id} | 更新商品 |
| 18 | DELETE | /api/admin/products/{id} | 删除商品 |
| 19 | GET | /api/admin/orders/page | 订单分页查询 |
| 20 | GET | /api/admin/orders/export | 导出订单 Excel |
| 21 | GET | /api/admin/orders/{id} | 获取订单详情 |
| 22 | POST | /api/admin/orders | 创建订单 |
| 23 | PUT | /api/admin/orders/{id} | 更新订单 |
| 24 | DELETE | /api/admin/orders/{id} | 删除订单 |
| 25 | GET | /api/admin/users/page | 用户分页查询 |
| 26 | GET | /api/admin/users/export | 导出用户 Excel |
| 27 | GET | /api/admin/users/{id} | 获取用户详情 |
| 28 | POST | /api/admin/users | 创建用户 |
| 29 | PUT | /api/admin/users/{id} | 更新用户 |
| 30 | DELETE | /api/admin/users/{id} | 删除用户 |
| 31 | GET | /api/admin/dashboard/stats | 仪表盘统计 |
| 32 | GET | /api/user/profile | 获取个人资料 |
| 33 | PUT | /api/user/profile | 更新个人资料 |

---

## 十五、业务规则与约定

### 15.1 角色体系

| role 值 | Spring Security 角色 | 说明 |
|---------|---------------------|------|
| 0 | ROLE_SUPER_ADMIN | 超级管理员 |
| 1 | ROLE_ADMIN | 普通管理员 |

注册用户默认 role=0, status=1

### 15.2 状态约定

| 实体 | 字段 | 值 | 含义 |
|------|------|----|------|
| User | status | 0 | 禁用 |
| User | status | 1 | 正常 |
| Category | status | 0 | 禁用 |
| Category | status | 1 | 启用 |
| Product | status | 0 | 下架 |
| Product | status | 1 | 上架 |
| Product | isHot | 1 | 热销 |
| Product | isNew | 1 | 新品 |
| OrderInfo | status | 0 | 待付款 |
| OrderInfo | status | 1 | 待发货 |
| OrderInfo | status | 2 | 待收货 |
| OrderInfo | status | 3 | 已完成 |
| OrderInfo | status | 4 | 已取消 |
| OrderInfo | payType | 1 | 微信支付 |
| OrderInfo | payType | 2 | 支付宝 |

### 15.3 逻辑删除

- 所有实体使用 `deleted` 字段逻辑删除 (0=未删除, 1=已删除)
- MyBatis-Plus 全局配置自动处理

### 15.4 密码安全

- BCryptPasswordEncoder 加密
- 创建用户时密码不能为空
- 更新用户时密码为空则不更新密码字段

### 15.5 认证流程

1. 客户端发送 `Authorization: Bearer <token>` 请求头
2. JwtAuthenticationFilter 解析 Token
3. 提取 username 和 role 设置到 SecurityContext
4. Controller 通过 `Authentication` 参数获取当前用户信息

### 15.6 分页参数

- pageNum: 页码, 默认 1
- pageSize: 每页大小, 默认 10
- 返回 PageResult {total, pageNum, pageSize, records}
