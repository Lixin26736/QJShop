package cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Category;
import cn.mikuyun.qjshopbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    /**
     * 获取所有一级分类
     */
    @GetMapping("/first")
    public ApiResponse<List<Category>> getFirstCategories() {
        return ApiResponse.success(categoryService.getFirstCategories());
    }

    /**
     * 获取指定一级分类下的二级分类
     */
    @GetMapping("/second/{parentId}")
    public ApiResponse<List<Category>> getSecondCategories(@PathVariable Long parentId) {
        return ApiResponse.success(categoryService.getSecondCategories(parentId));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Category>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long parentId
    ) {
        return ApiResponse.success(categoryService.page(pageNum, pageSize, keyword, status, parentId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getById(@PathVariable Long id) {
        return ApiResponse.success(categoryService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody Category category) {
        categoryService.save(category);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success();
    }
}
