package cn.mikuyun.qjshopbackend.controller.user;

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
