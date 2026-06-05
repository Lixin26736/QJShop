package cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<User> getProfile(Authentication authentication) {
        String username = getUsername(authentication);
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(null);
        return ApiResponse.success(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ApiResponse<Void> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        String username = getUsername(authentication);
        userService.updateProfileByUsername(username, request);
        return ApiResponse.success();
    }

    private String getUsername(Authentication authentication) {
        if (authentication == null || authentication.getDetails() == null) {
            throw new RuntimeException("未登录");
        }
        return authentication.getDetails().toString();
    }
}
