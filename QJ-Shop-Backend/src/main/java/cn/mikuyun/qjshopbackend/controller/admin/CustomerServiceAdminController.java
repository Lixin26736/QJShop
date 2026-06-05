package cn.mikuyun.qjshopbackend.controller.admin;

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
@RequestMapping("/api/admin/cs")
@RequiredArgsConstructor
public class CustomerServiceAdminController {

    private final CustomerServiceService customerServiceService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unread")
    public ApiResponse<List<CustomerService>> listUnread() {
        return ApiResponse.success(customerServiceService.listUnreadByAdmin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unread-count")
    public ApiResponse<Long> unreadCount() {
        return ApiResponse.success(customerServiceService.countUnreadByAdmin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ApiResponse<List<CustomerService>> listByUserId(@PathVariable Long userId) {
        return ApiResponse.success(customerServiceService.listByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reply")
    public ApiResponse<CustomerService> reply(@RequestBody Map<String, Object> body,
                                               Authentication authentication) {
        String content = body.get("content") != null ? body.get("content").toString() : "";
        Long userId = body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : 0L;

        CustomerService msg = new CustomerService();
        msg.setUserId(userId);
        msg.setAdminId((Long) authentication.getPrincipal());
        msg.setContent(content);
        msg.setSenderType(1); // 1=管理员发送（AI回复）
        msg.setIsRead(1);

        return ApiResponse.success(customerServiceService.sendMessage(msg));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        customerServiceService.markRead(id);
        return ApiResponse.success();
    }
}
