package cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.service.ProductService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @GetMapping("/page")
    public ApiResponse<PageResult<Product>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId
    ) {
        return ApiResponse.success(productService.page(pageNum, pageSize, keyword, status, categoryId));
    }

    /**
     * 导出商品数据到Excel
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            HttpServletResponse response
    ) throws IOException {
        List<Product> productList = productService.listAll(keyword, status, categoryId);

        String[] headers = {"ID", "商品名称", "价格", "原价", "库存", "销量", "状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Product product : productList) {
            List<Object> row = new ArrayList<>();
            row.add(product.getId());
            row.add(product.getName());
            row.add(product.getPrice() != null ? "¥" + product.getPrice() : "¥0");
            row.add(product.getOriginalPrice() != null ? "¥" + product.getOriginalPrice() : "¥0");
            row.add(product.getStock() != null ? product.getStock() : 0);
            row.add(product.getSales() != null ? product.getSales() : 0);
            row.add(product.getStatus() != null && product.getStatus() == 1 ? "上架" : "下架");
            row.add(product.getCreateTime() != null ? product.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        ExcelExportUtil.exportExcel(response, "商品数据", "商品列表", headers, dataList);
    }

    @GetMapping("/hot")
    public ApiResponse<List<Product>> getHotProducts() {
        return ApiResponse.success(productService.getHotProducts());
    }

    @GetMapping("/new")
    public ApiResponse<List<Product>> getNewProducts() {
        return ApiResponse.success(productService.getNewProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Void> create(@RequestBody Product product) {
        productService.save(product);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.update(product);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success();
    }
}
