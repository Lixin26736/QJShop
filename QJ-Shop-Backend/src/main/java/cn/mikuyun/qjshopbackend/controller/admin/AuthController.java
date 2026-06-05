package cn.mikuyun.qjshopbackend.controller.admin;

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
