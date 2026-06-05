package cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.entity.Address;
import cn.mikuyun.qjshopbackend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<List<Address>> list(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ApiResponse.success(addressService.listByUserId(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ApiResponse<Address> getById(@PathVariable Long id) {
        return ApiResponse.success(addressService.getById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody Address address, Authentication authentication) {
        address.setUserId(getUserId(authentication));
        addressService.save(address);
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody Address address, Authentication authentication) {
        address.setId(id);
        address.setUserId(getUserId(authentication));
        addressService.update(address);
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/default")
    public ApiResponse<Void> setDefault(@PathVariable Long id, Authentication authentication) {
        addressService.setDefault(id, getUserId(authentication));
        return ApiResponse.success();
    }

    private Long getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        return (Long) authentication.getPrincipal();
    }
}
