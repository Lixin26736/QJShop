package cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.entity.CustomerService;
import cn.mikuyun.qjshopbackend.service.CustomerServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/cs")
@RequiredArgsConstructor
public class CustomerServiceClientController {

    private final CustomerServiceService customerServiceService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/messages")
    public ApiResponse<List<CustomerService>> listMessages(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(customerServiceService.listByUserId(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public ApiResponse<CustomerService> sendMessage(@RequestBody Map<String, Object> body,
                                                     Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String content = body.get("content") != null ? body.get("content").toString() : "";
        String image = body.get("image") != null ? body.get("image").toString() : null;

        CustomerService msg = new CustomerService();
        msg.setUserId(userId);
        msg.setContent(content);
        msg.setImage(image);
        msg.setSenderType(0); // 0=用户发送

        return ApiResponse.success(customerServiceService.sendMessage(msg));
    }
}
