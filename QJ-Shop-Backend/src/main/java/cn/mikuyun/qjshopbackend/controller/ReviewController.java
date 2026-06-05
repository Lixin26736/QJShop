package cn.mikuyun.qjshopbackend.controller;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Review;
import cn.mikuyun.qjshopbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ApiResponse<PageResult<Review>> listByProduct(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @PathVariable Long productId) {
        return ApiResponse.success(reviewService.pageByProductId(pageNum, pageSize, productId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ApiResponse<Void> submit(@RequestBody Review review, Authentication authentication) {
        review.setUserId((Long) authentication.getPrincipal());
        reviewService.save(review);
        return ApiResponse.success();
    }
}
