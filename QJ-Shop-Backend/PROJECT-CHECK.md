# QJ-Shop-Backend 项目检查文档\n`\n## 1. 项目基本信息\n`\n| 项目 | 值 |\n|------|------|\n| groupId | cn.mikuyun |\n| artifactId | QJ-Shop-Backend |\n| version | 0.0.1-SNAPSHOT |\n| name | QJ-Shop-Backend |\n| description | QJ商城后台管理系统 |\n| Java版本 | 17 |\n| Spring Boot版本 | 3.2.5 |\n| 基础包名 | cn.mikuyun.qjshopbackend |\n| 启动端口 | 8080 |\n`\n---\n`\n## 2. pom.xml\n`\n`xml\n<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>

    <groupId>cn.mikuyun</groupId>
    <artifactId>QJ-Shop-Backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>QJ-Shop-Backend</name>
    <description>QJ商城后台管理系统</description>

    <properties>
        <java.version>17</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <druid.version>1.2.20</druid.version>
        <jjwt.version>0.12.5</jjwt.version>
        <hutool.version>5.8.25</hutool.version>
        <mysql.version>8.0.33</mysql.version>
        <knife4j.version>4.5.0</knife4j.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot JDBC -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <!-- Spring Boot Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- Spring Boot Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- MySQL 驱动 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>${mysql.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- MyBatis-Plus（Spring Boot 3.x） -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!-- Druid 连接池（Spring Boot 3.x 专用） -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-3-starter</artifactId>
            <version>${druid.version}</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Hutool -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>

        <!-- Apache POI for Excel -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.5</version>
        </dependency>

        <!-- Commons Lang3 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>

        <!-- Knife4j OpenAPI 3 -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot DevTools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot Configuration Processor -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>\n`\n`\n---\n`\n## 3. application.properties\n`\n`properties\n# ============================================
# \u5E94\u7528\u57FA\u7840\u914D\u7F6E
# ============================================
spring.application.name=QJ-Shop-Backend
server.port=8080

# ============================================
# \u6570\u636E\u5E93\u914D\u7F6E
# ============================================
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/qj_shop?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root

# ============================================
# Druid \u8FDE\u63A5\u6C60\u914D\u7F6E
# ============================================
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.druid.initial-size=5
spring.datasource.druid.min-idle=5
spring.datasource.druid.max-active=20
spring.datasource.druid.max-wait=60000
spring.datasource.druid.time-between-eviction-runs-millis=60000
spring.datasource.druid.min-evictable-idle-time-millis=300000
spring.datasource.druid.validation-query=SELECT 1
spring.datasource.druid.test-while-idle=true
spring.datasource.druid.test-on-borrow=false
spring.datasource.druid.test-on-return=false
spring.datasource.druid.pool-prepared-statements=true
spring.datasource.druid.max-pool-prepared-statement-per-connection-size=20

# Druid \u76D1\u63A7\u914D\u7F6E
spring.datasource.druid.stat-view-servlet.enabled=true
spring.datasource.druid.stat-view-servlet.url-pattern=/druid/*
spring.datasource.druid.stat-view-servlet.login-username=admin
spring.datasource.druid.stat-view-servlet.login-password=admin123
spring.datasource.druid.filter.stat.enabled=true
spring.datasource.druid.filter.stat.log-slow-sql=true
spring.datasource.druid.filter.stat.slow-sql-millis=2000
spring.datasource.druid.filter.wall.enabled=true

# ============================================
# MyBatis-Plus \u914D\u7F6E
# ============================================
mybatis-plus.mapper-locations=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=cn.mikuyun.qjshopbackend.entity
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.cache-enabled=false
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

# MyBatis-Plus \u5168\u5C40\u914D\u7F6E
mybatis-plus.global-config.db-config.id-type=auto
mybatis-plus.global-config.db-config.logic-delete-field=deleted
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
mybatis-plus.global-config.db-config.table-underline=true

# ============================================
# Jackson JSON \u914D\u7F6E
# ============================================
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=Asia/Shanghai
spring.jackson.default-property-inclusion=non_null

# ============================================
# \u6587\u4EF6\u4E0A\u4F20\u914D\u7F6E
# ============================================
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=20MB

# ============================================
# \u65E5\u5FD7\u914D\u7F6E
# ============================================
logging.level.cn.mikuyun.qjshopbackend=debug
logging.level.org.springframework.web=info
logging.level.com.baomidou.mybatisplus=debug
logging.file.name=logs/qj-shop.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n

# ============================================
# JWT \u914D\u7F6E
# ============================================
jwt.secret=qj-shop-secret-key-2024-jwt-token-very-long-secret-key-for-security-must-be-256-bits
jwt.expiration=604800000
jwt.header=Authorization
jwt.token-prefix=Bearer

# ============================================
# Knife4j / OpenAPI 文档配置
# ============================================
knife4j.enable=true
knife4j.setting.language=zh_cn
\n`\n`\n---\n`\n## 4. 所有Java源文件\n`\n### QjShopBackendApplication.java\n`\n`java\npackage cn.mikuyun.qjshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.mikuyun.qjshopbackend.mapper")
public class QjShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QjShopBackendApplication.class, args);
        System.out.println("成功启动,访问接口:http://localhost:8080");
    }

}
\n`\n`\n### common\ApiResponse.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "success", null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(500, message, null);
    }

    public static <T> ApiResponse<T> error(int codeNum, String message){
        return new ApiResponse<>(codeNum, message, null);

    }
}
\n`\n`\n### common\PageResult.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private Long total;
    private Long pageNum;
    private Long pageSize;
    private List<T> records;
}
\n`\n`\n### config\CorsConfig.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS跨域配置
 * 允许前端应用从不同域名访问后端API
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的域名
        // 开发环境允许所有域名访问
        config.addAllowedOriginPattern("*");
        
        // 允许的HTTP方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // 允许的请求头
        config.addAllowedHeader("*");
        
        // 是否允许携带凭证(cookies等)
        config.setAllowCredentials(true);
        
        // 预检请求的缓存时间(秒)
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
\n`\n`\n### config\JwtProperties.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 密钥
     */
    private String secret;

    /**
     * 过期时间（毫秒）
     */
    private Long expiration;

    /**
     * Token 请求头
     */
    private String header;

    /**
     * Token 前缀
     */
    private String tokenPrefix;
}\n`\n`\n### config\MybatisPlusConfig.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
\n`\n`\n### config\SecurityConfig.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.config;

import cn.mikuyun.qjshopbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
\n`\n`\n### controller\CommonAuthController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.dto.auth.LoginRequest;
import cn.mikuyun.qjshopbackend.dto.auth.LoginResponse;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.service.AuthService;
import cn.mikuyun.qjshopbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CommonAuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        
        // 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", loginResponse.getToken());
        
        // 获取完整的用户信息
        User fullUser = userService.getById(loginResponse.getUserId());
        
        // 构建用户信息（包含所有字段）
        Map<String, Object> user = new HashMap<>();
        user.put("id", fullUser.getId());
        user.put("username", fullUser.getUsername());
        user.put("nickname", fullUser.getNickname());
        user.put("phone", fullUser.getPhone());
        user.put("email", fullUser.getEmail());
        user.put("avatar", fullUser.getAvatar());
        user.put("gender", fullUser.getGender());
        user.put("birthday", fullUser.getBirthday());
        user.put("role", fullUser.getRole());
        result.put("user", user);
        
        return ApiResponse.success(result);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String nickname = request.get("nickname");
        String phone = request.get("phone");
        
        // 检查用户名是否已存在
        User existUser = userService.getByUsername(username);
        if (existUser != null) {
            return ApiResponse.error(400, "用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname != null ? nickname : username);
        user.setPhone(phone);
        user.setRole(0); // 普通用户
        user.setStatus(1); // 正常状态
        
        userService.save(user);
        
        return ApiResponse.success();
    }
}
\n`\n`\n### controller\admin\AuthController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.dto.auth.LoginRequest;
import cn.mikuyun.qjshopbackend.dto.auth.LoginResponse;
import cn.mikuyun.qjshopbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponse<LoginResponse> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return ApiResponse.success(authService.login(request));
    }
}
\n`\n`\n### controller\admin\CategoryAdminController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Category;
import cn.mikuyun.qjshopbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    /**
     * 获取所有一级分类
     */
    @GetMapping("/first")
    public ApiResponse<List<Category>> getFirstCategories() {
        return ApiResponse.success(categoryService.getFirstCategories());
    }

    /**
     * 获取指定一级分类下的二级分类
     */
    @GetMapping("/second/{parentId}")
    public ApiResponse<List<Category>> getSecondCategories(@PathVariable Long parentId) {
        return ApiResponse.success(categoryService.getSecondCategories(parentId));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Category>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long parentId
    ) {
        return ApiResponse.success(categoryService.page(pageNum, pageSize, keyword, status, parentId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getById(@PathVariable Long id) {
        return ApiResponse.success(categoryService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody Category category) {
        categoryService.save(category);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success();
    }
}
\n`\n`\n### controller\admin\DashboardController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.service.DashboardService;
import cn.mikuyun.qjshopbackend.vo.DashboardStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStatsVO> stats() {
        return ApiResponse.success(dashboardService.stats());
    }
}
\n`\n`\n### controller\admin\OrderInfoAdminController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.service.OrderInfoService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrderInfoAdminController {

    private final OrderInfoService orderInfoService;

    @GetMapping("/page")
    public ApiResponse<PageResult<OrderInfo>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId
    ) {
        return ApiResponse.success(orderInfoService.page(pageNum, pageSize, orderNo, status, userId));
    }

    /**
     * 导出订单数据到Excel
     */
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId,
            HttpServletResponse response
    ) throws IOException {
        List<OrderInfo> orderList = orderInfoService.listAll(orderNo, status, userId);

        String[] headers = {"ID", "订单号", "用户ID", "订单金额", "支付方式", "订单状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (OrderInfo order : orderList) {
            List<Object> row = new ArrayList<>();
            row.add(order.getId());
            row.add(order.getOrderNo());
            row.add(order.getUserId());
            row.add(order.getTotalAmount() != null ? "¥" + order.getTotalAmount() : "¥0");
            row.add(order.getPayType() != null && order.getPayType() == 1 ? "微信" : "支付宝");
            row.add(getStatusText(order.getStatus()));
            row.add(order.getCreateTime() != null ? order.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        ExcelExportUtil.exportExcel(response, "订单数据", "订单列表", headers, dataList);
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待付款";
            case 1: return "待发货";
            case 2: return "待收货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderInfo> getById(@PathVariable Long id) {
        return ApiResponse.success(orderInfoService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody OrderInfo orderInfo) {
        orderInfoService.save(orderInfo);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody OrderInfo orderInfo) {
        orderInfo.setId(id);
        orderInfoService.update(orderInfo);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        orderInfoService.delete(id);
        return ApiResponse.success();
    }
}
\n`\n`\n### controller\admin\ProductAdminController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.service.ProductService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @GetMapping("/page")
    public ApiResponse<PageResult<Product>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId
    ) {
        return ApiResponse.success(productService.page(pageNum, pageSize, keyword, status, categoryId));
    }

    /**
     * 导出商品数据到Excel
     */
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            HttpServletResponse response
    ) throws IOException {
        List<Product> productList = productService.listAll(keyword, status, categoryId);

        String[] headers = {"ID", "商品名称", "价格", "原价", "库存", "销量", "状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Product product : productList) {
            List<Object> row = new ArrayList<>();
            row.add(product.getId());
            row.add(product.getName());
            row.add(product.getPrice() != null ? "¥" + product.getPrice() : "¥0");
            row.add(product.getOriginalPrice() != null ? "¥" + product.getOriginalPrice() : "¥0");
            row.add(product.getStock() != null ? product.getStock() : 0);
            row.add(product.getSales() != null ? product.getSales() : 0);
            row.add(product.getStatus() != null && product.getStatus() == 1 ? "上架" : "下架");
            row.add(product.getCreateTime() != null ? product.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        ExcelExportUtil.exportExcel(response, "商品数据", "商品列表", headers, dataList);
    }

    @GetMapping("/hot")
    public ApiResponse<List<Product>> getHotProducts() {
        return ApiResponse.success(productService.getHotProducts());
    }

    @GetMapping("/new")
    public ApiResponse<List<Product>> getNewProducts() {
        return ApiResponse.success(productService.getNewProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody Product product) {
        productService.save(product);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.update(product);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success();
    }
}
\n`\n`\n### controller\admin\UserAdminController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.service.UserService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/page")
    public ApiResponse<PageResult<User>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status
    ) {
        return ApiResponse.success(userService.page(pageNum, pageSize, keyword, role, status));
    }

    /**
     * 导出用户数据到Excel
     */
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response
    ) throws IOException {
        // 获取所有符合条件的用户数据
        List<User> userList = userService.listAll(keyword, role, status);

        // 准备Excel数据
        String[] headers = {"ID", "用户名", "昵称", "手机号", "邮箱", "角色", "状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (User user : userList) {
            List<Object> row = new ArrayList<>();
            row.add(user.getId());
            row.add(user.getUsername());
            row.add(user.getNickname());
            row.add(user.getPhone());
            row.add(user.getEmail());
            row.add(user.getRole() == 1 ? "管理员" : "普通用户");
            row.add(user.getStatus() == 1 ? "正常" : "禁用");
            row.add(user.getCreateTime() != null ? user.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        // 导出Excel
        ExcelExportUtil.exportExcel(response, "用户数据", "用户列表", headers, dataList);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        return ApiResponse.success(userService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody User user) {
        userService.save(user);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success();
    }
}
\n`\n`\n### controller\user\UserProfileController.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<User> getProfile(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("未登录");
        }
        User user = userService.getByUsername(authentication.getName());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(null);
        return ApiResponse.success(user);
    }

    @PutMapping
    public ApiResponse<Void> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("未登录");
        }
        userService.updateProfileByUsername(authentication.getName(), request);
        return ApiResponse.success();
    }
}
\n`\n`\n### dto\auth\LoginRequest.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
\n`\n`\n### dto\auth\LoginResponse.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Long userId;
    private String username;
    private String nickname;
    private Integer role;
}
\n`\n`\n### dto\user\UserProfileUpdateRequest.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequest {

    private String nickname;

    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String avatar;

    private Integer gender;

    private LocalDate birthday;
}
\n`\n`\n### entity\Category.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category {

    @TableId
    private Long id;
    private Long parentId;
    private String name;
    private String icon;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
\n`\n`\n### entity\OrderInfo.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_info")
public class OrderInfo {

    @TableId
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private BigDecimal freight;
    private Integer payType;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
\n`\n`\n### entity\Product.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {

    @TableId
    private Long id;
    private Long categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String detailImages;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sales;
    private String description;
    private String detailContent;
    private Integer status;
    private Integer isHot;
    private Integer isNew;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
\n`\n`\n### entity\User.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer gender;
    private LocalDate birthday;
    private Integer role;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
\n`\n`\n### exception\GlobalExceptionHandler.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.exception;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e) {
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    public ApiResponse<Void> handleBadRequest(Exception e) {
        return new ApiResponse<>(400, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        return new ApiResponse<>(400, message, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> handleAccessDenied(AccessDeniedException e) {
        return new ApiResponse<>(403, "无权限访问", null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return new ApiResponse<>(500, "服务器异常", null);
    }
}
\n`\n`\n### mapper\CategoryMapper.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CategoryMapper extends BaseMapper<Category> {
}
\n`\n`\n### mapper\OrderInfoMapper.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("select ifnull(sum(pay_amount), 0) from order_info")
    BigDecimal sumPayAmount();

    @Select("select count(1) from order_info where create_time >= #{start} and create_time < #{end}")
    Long countTodayOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("select ifnull(sum(pay_amount), 0) from order_info where create_time >= #{start} and create_time < #{end}")
    BigDecimal sumTodayPayAmount(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
\n`\n`\n### mapper\ProductMapper.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ProductMapper extends BaseMapper<Product> {
}
\n`\n`\n### mapper\UserMapper.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.mikuyun.qjshopbackend.entity.User;

public interface UserMapper extends BaseMapper<User> {
}
\n`\n`\n### security\JwtAuthenticationFilter.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.security;

import cn.mikuyun.qjshopbackend.config.JwtProperties;
import cn.mikuyun.qjshopbackend.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(jwtProperties.getHeader());
        String tokenPrefix = jwtProperties.getTokenPrefix() + " ";
        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(tokenPrefix.length());
        try {
            Claims claims = jwtTokenUtil.parseToken(token);
            String username = claims.get("username", String.class);
            Integer role = claims.get("role", Integer.class);
            String roleName = role != null && role == 0 ? "ROLE_SUPER_ADMIN" : "ROLE_ADMIN";
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    List.of(new SimpleGrantedAuthority(roleName))
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception ignored) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
\n`\n`\n### service\AuthService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.dto.auth.LoginRequest;
import cn.mikuyun.qjshopbackend.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
\n`\n`\n### service\CategoryService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Category;

import java.util.List;

public interface CategoryService {

    PageResult<Category> page(int pageNum, int pageSize, String keyword, Integer status, Long parentId);

    Category getById(Long id);

    void save(Category category);

    void update(Category category);

    void delete(Long id);

    /**
     * 获取所有一级分类(parent_id = 0)
     */
    List<Category> getFirstCategories();

    /**
     * 获取指定一级分类下的二级分类
     */
    List<Category> getSecondCategories(Long parentId);
}
\n`\n`\n### service\DashboardService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.vo.DashboardStatsVO;

public interface DashboardService {

    DashboardStatsVO stats();
}
\n`\n`\n### service\OrderInfoService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;

import java.util.List;

public interface OrderInfoService {

    PageResult<OrderInfo> page(int pageNum, int pageSize, String orderNo, Integer status, Long userId);

    List<OrderInfo> listAll(String orderNo, Integer status, Long userId);

    OrderInfo getById(Long id);

    void save(OrderInfo orderInfo);

    void update(OrderInfo orderInfo);

    void delete(Long id);
}
\n`\n`\n### service\ProductService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;

import java.util.List;

public interface ProductService {

    PageResult<Product> page(int pageNum, int pageSize, String keyword, Integer status, Long categoryId);

    List<Product> listAll(String keyword, Integer status, Long categoryId);

    Product getById(Long id);

    void save(Product product);

    void update(Product product);

    void delete(Long id);

    List<Product> getHotProducts();

    List<Product> getNewProducts();
}
\n`\n`\n### service\UserService.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;

import java.util.List;

public interface UserService {

    PageResult<User> page(int pageNum, int pageSize, String keyword, Integer role, Integer status);

    List<User> listAll(String keyword, Integer role, Integer status);

    User getById(Long id);

    void save(User user);

    void update(User user);

    void delete(Long id);

    User getByUsername(String username);

    void updateProfileByUsername(String username, UserProfileUpdateRequest request);
}
\n`\n`\n### service\impl\AuthServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.dto.auth.LoginRequest;
import cn.mikuyun.qjshopbackend.dto.auth.LoginResponse;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.mapper.UserMapper;
import cn.mikuyun.qjshopbackend.service.AuthService;
import cn.mikuyun.qjshopbackend.util.JwtTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername()).last("limit 1");
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getNickname(), user.getRole());
    }
}
\n`\n`\n### service\impl\CategoryServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Category;
import cn.mikuyun.qjshopbackend.mapper.CategoryMapper;
import cn.mikuyun.qjshopbackend.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public PageResult<Category> page(int pageNum, int pageSize, String keyword, Integer status, Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(keyword), Category::getName, keyword)
                .eq(status != null, Category::getStatus, status)
                .eq(parentId != null, Category::getParentId, parentId)
                .orderByDesc(Category::getId);
        Page<Category> page = categoryMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public void save(Category category) {
        category.setId(null);
        categoryMapper.insert(category);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> getFirstCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, 0)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSortOrder)
                .orderByDesc(Category::getId);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<Category> getSecondCategories(Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSortOrder)
                .orderByDesc(Category::getId);
        return categoryMapper.selectList(wrapper);
    }
}
\n`\n`\n### service\impl\DashboardServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.mapper.OrderInfoMapper;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.mapper.UserMapper;
import cn.mikuyun.qjshopbackend.service.DashboardService;
import cn.mikuyun.qjshopbackend.vo.DashboardStatsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final OrderInfoMapper orderInfoMapper;

    @Override
    public DashboardStatsVO stats() {
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>());
        Long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>());
        Long orderCount = orderInfoMapper.selectCount(new LambdaQueryWrapper<OrderInfo>());

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        Long todayOrderCount = orderInfoMapper.countTodayOrders(start, end);
        BigDecimal totalSales = orderInfoMapper.sumPayAmount();
        BigDecimal todaySales = orderInfoMapper.sumTodayPayAmount(start, end);

        return new DashboardStatsVO(
                userCount == null ? 0L : userCount,
                productCount == null ? 0L : productCount,
                orderCount == null ? 0L : orderCount,
                todayOrderCount == null ? 0L : todayOrderCount,
                totalSales == null ? BigDecimal.ZERO : totalSales,
                todaySales == null ? BigDecimal.ZERO : todaySales
        );
    }
}
\n`\n`\n### service\impl\OrderInfoServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.mapper.OrderInfoMapper;
import cn.mikuyun.qjshopbackend.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl implements OrderInfoService {

    private final OrderInfoMapper orderInfoMapper;

    @Override
    public PageResult<OrderInfo> page(int pageNum, int pageSize, String orderNo, Integer status, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(orderNo), OrderInfo::getOrderNo, orderNo)
                .eq(status != null, OrderInfo::getStatus, status)
                .eq(userId != null, OrderInfo::getUserId, userId)
                .orderByDesc(OrderInfo::getId);
        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public List<OrderInfo> listAll(String orderNo, Integer status, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(orderNo), OrderInfo::getOrderNo, orderNo)
                .eq(status != null, OrderInfo::getStatus, status)
                .eq(userId != null, OrderInfo::getUserId, userId)
                .orderByDesc(OrderInfo::getId);
        return orderInfoMapper.selectList(wrapper);
    }

    @Override
    public OrderInfo getById(Long id) {
        return orderInfoMapper.selectById(id);
    }

    @Override
    public void save(OrderInfo orderInfo) {
        orderInfo.setId(null);
        orderInfoMapper.insert(orderInfo);
    }

    @Override
    public void update(OrderInfo orderInfo) {
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public void delete(Long id) {
        orderInfoMapper.deleteById(id);
    }
}
\n`\n`\n### service\impl\ProductServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public PageResult<Product> page(int pageNum, int pageSize, String keyword, Integer status, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(keyword), Product::getName, keyword)
                .eq(status != null, Product::getStatus, status)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .orderByDesc(Product::getId);
        Page<Product> page = productMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public List<Product> listAll(String keyword, Integer status, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(keyword), Product::getName, keyword)
                .eq(status != null, Product::getStatus, status)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .orderByDesc(Product::getId);
        return productMapper.selectList(wrapper);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public void save(Product product) {
        product.setId(null);
        productMapper.insert(product);
    }

    @Override
    public void update(Product product) {
        productMapper.updateById(product);
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public List<Product> getHotProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .eq(Product::getIsHot, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 10");
        return productMapper.selectList(wrapper);
    }

    @Override
    public List<Product> getNewProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .eq(Product::getIsNew, 1)
                .orderByDesc(Product::getCreateTime)
                .last("LIMIT 10");
        return productMapper.selectList(wrapper);
    }
}
\n`\n`\n### service\impl\UserServiceImpl.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.mapper.UserMapper;
import cn.mikuyun.qjshopbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<User> page(int pageNum, int pageSize, String keyword, Integer role, Integer status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(keyword),
                        w -> w.like(User::getUsername, keyword)
                                .or().like(User::getNickname, keyword)
                                .or().like(User::getPhone, keyword))
                .eq(role != null, User::getRole, role)
                .eq(status != null, User::getStatus, status)
                .orderByDesc(User::getId);
        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public List<User> listAll(String keyword, Integer role, Integer status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(keyword),
                        w -> w.like(User::getUsername, keyword)
                                .or().like(User::getNickname, keyword)
                                .or().like(User::getPhone, keyword))
                .eq(role != null, User::getRole, role)
                .eq(status != null, User::getStatus, status)
                .orderByDesc(User::getId);
        return userMapper.selectList(wrapper);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void save(User user) {
        user.setId(null);
        if (StrUtil.isBlank(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).last("limit 1");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateProfileByUsername(String username, UserProfileUpdateRequest request) {
        User currentUser = getByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (request.getGender() != null && request.getGender() != 0 && request.getGender() != 1) {
            throw new RuntimeException("性别参数不合法");
        }

        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setNickname(request.getNickname());
        updateUser.setPhone(request.getPhone());
        updateUser.setEmail(request.getEmail());
        updateUser.setAvatar(request.getAvatar());
        updateUser.setGender(request.getGender());
        updateUser.setBirthday(request.getBirthday());
        userMapper.updateById(updateUser);
    }
}
\n`\n`\n### util\ExcelExportUtil.java\n`\n`java\n  package cn.mikuyun.qjshopbackend.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel导出工具类
 */
public class ExcelExportUtil {

    /**
     * 导出Excel
     *
     * @param response  HttpServletResponse
     * @param fileName  文件名
     * @param sheetName Sheet名称
     * @param headers   表头
     * @param dataList  数据列表
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                   String[] headers, List<List<Object>> dataList) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建工作表
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 填充数据
            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<Object> rowData = dataList.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = rowData.get(j);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(dataStyle);
                }
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
        }
    }
}
\n`\n`\n### util\JwtTokenUtil.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.util;

import cn.mikuyun.qjshopbackend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final JwtProperties jwtProperties;

    public String generateToken(Long userId, String username, Integer role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperties.getExpiration());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
\n`\n`\n### vo\DashboardStatsVO.java\n`\n`java\npackage cn.mikuyun.qjshopbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsVO {

    private Long userCount;
    private Long productCount;
    private Long orderCount;
    private Long todayOrderCount;
    private BigDecimal totalSales;
    private BigDecimal todaySales;
}
\n`\n