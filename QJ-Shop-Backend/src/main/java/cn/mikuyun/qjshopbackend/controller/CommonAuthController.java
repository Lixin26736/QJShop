package cn.mikuyun.qjshopbackend.controller;

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
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setPhone(phone);
        user.setRole(0); // 普通用户
        user.setStatus(1); // 正常状态
        
        userService.save(user);
        
        return ApiResponse.success();
    }
}
