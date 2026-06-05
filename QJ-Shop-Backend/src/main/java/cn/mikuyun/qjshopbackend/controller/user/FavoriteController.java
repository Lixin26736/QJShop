package cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Favorite;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final ProductMapper productMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        PageResult<Favorite> page = favoriteService.page(pageNum, pageSize, userId);

        List<Map<String, Object>> items = new ArrayList<>();
        for (Favorite fav : page.getRecords()) {
            Product product = productMapper.selectById(fav.getProductId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", fav.getId());
            item.put("productId", fav.getProductId());
            item.put("createTime", fav.getCreateTime());
            if (product != null) {
                item.put("productName", product.getName());
                item.put("productImage", product.getMainImage());
                item.put("price", product.getPrice());
                item.put("status", product.getStatus());
            }
            items.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("records", items);
        result.put("total", page.getTotal());
        result.put("current", page.getPageNum());
        result.put("size", page.getPageSize());
        return ApiResponse.success(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}")
    public ApiResponse<Void> add(@PathVariable Long productId, Authentication authentication) {
        favoriteService.add(getUserId(authentication), productId);
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> remove(@PathVariable Long productId, Authentication authentication) {
        favoriteService.remove(getUserId(authentication), productId);
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check/{productId}")
    public ApiResponse<Boolean> check(@PathVariable Long productId, Authentication authentication) {
        return ApiResponse.success(favoriteService.isFavorited(getUserId(authentication), productId));
    }

    private Long getUserId(Authentication authentication) {
        if (authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        return (Long) authentication.getPrincipal();
    }
}
