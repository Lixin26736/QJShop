package cn.mikuyun.qjshopbackend.controller;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.entity.ProductSpec;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.mapper.ReviewMapper;
import cn.mikuyun.qjshopbackend.service.ProductService;
import cn.mikuyun.qjshopbackend.service.ProductSpecService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductSpecService productSpecService;
    private final ReviewMapper reviewMapper;

    @GetMapping("/page")
    public ApiResponse<PageResult<Product>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(keyword), Product::getName, keyword)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .eq(status != null, Product::getStatus, status)
                .eq(Product::getStatus, 1) // 默认只查上架商品
                .orderByDesc(Product::getSortOrder)
                .orderByDesc(Product::getCreateTime);
        Page<Product> page = productMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return ApiResponse.success(new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/search")
    public ApiResponse<PageResult<Product>> search(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String keyword) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Product::getName, keyword)
                        .or().like(Product::getSubtitle, keyword)
                        .or().like(Product::getDescription, keyword))
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getSales)
                .orderByDesc(Product::getCreateTime);
        Page<Product> page = productMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return ApiResponse.success(new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/hot")
    public ApiResponse<List<Product>> hot() {
        return ApiResponse.success(productService.getHotProducts());
    }

    @GetMapping("/new")
    public ApiResponse<List<Product>> newProducts() {
        return ApiResponse.success(productService.getNewProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        Product product = productService.getById(id);
        List<ProductSpec> specs = productSpecService.listByProductId(id);
        Double avgRating = reviewMapper.avgRating(id);
        Long reviewCount = reviewMapper.countByProductId(id);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product);
        result.put("specs", specs);
        result.put("avgRating", avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0);
        result.put("reviewCount", reviewCount != null ? reviewCount : 0);
        return ApiResponse.success(result);
    }
}
