package cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.service.OrderInfoService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrderInfoAdminController {

    private final OrderInfoService orderInfoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    public ApiResponse<PageResult<OrderInfo>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId
    ) {
        return ApiResponse.success(orderInfoService.page(pageNum, pageSize, orderNo, status, userId));
    }

    /**
     * 导出订单数据到Excel
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId,
            HttpServletResponse response
    ) throws IOException {
        List<OrderInfo> orderList = orderInfoService.listAll(orderNo, status, userId);

        String[] headers = {"ID", "订单号", "用户ID", "订单金额", "支付方式", "订单状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (OrderInfo order : orderList) {
            List<Object> row = new ArrayList<>();
            row.add(order.getId());
            row.add(order.getOrderNo());
            row.add(order.getUserId());
            row.add(order.getTotalAmount() != null ? "¥" + order.getTotalAmount() : "¥0");
            row.add(order.getPayType() != null && order.getPayType() == 1 ? "微信" : "支付宝");
            row.add(getStatusText(order.getStatus()));
            row.add(order.getCreateTime() != null ? order.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        ExcelExportUtil.exportExcel(response, "订单数据", "订单列表", headers, dataList);
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待付款";
            case 1: return "待发货";
            case 2: return "待收货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<OrderInfo> getById(@PathVariable Long id) {
        return ApiResponse.success(orderInfoService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Void> create(@RequestBody OrderInfo orderInfo) {
        orderInfoService.save(orderInfo);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody OrderInfo orderInfo) {
        orderInfo.setId(id);
        orderInfoService.update(orderInfo);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        orderInfoService.delete(id);
        return ApiResponse.success();
    }
}
