package cn.mikuyun.qjshopbackend.controller.user;

import cn.hutool.core.util.IdUtil;
import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.entity.OrderItem;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.service.OrderInfoService;
import cn.mikuyun.qjshopbackend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;
    private final ProductMapper productMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<PageResult<OrderInfo>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ApiResponse.success(orderInfoService.pageByUser(pageNum, pageSize, userId, status));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        OrderInfo order = orderInfoService.getById(id);
        List<OrderItem> items = orderItemService.listByOrderId(id);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("order", order);
        result.put("items", items);
        return ApiResponse.success(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ApiResponse<Map<String, Object>> create(@RequestBody Map<String, Object> body, Authentication authentication) {
        Long userId = getUserId(authentication);
        Long addressId = body.get("addressId") != null ?
                Long.valueOf(body.get("addressId").toString()) : null;
        String remark = body.get("remark") != null ? body.get("remark").toString() : null;

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) body.get("items");

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("订单商品不能为空");
        }

        // 创建订单
        OrderInfo order = new OrderInfo();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setStatus(0); // 待付款
        order.setRemark(remark);
        order.setPayType(1); // 默认微信

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (Map<String, Object> cartItem : cartItems) {
            Long productId = Long.valueOf(cartItem.get("productId").toString());
            Integer quantity = Integer.valueOf(cartItem.get("quantity").toString());
            Product product = productMapper.selectById(productId);

            if (product == null) continue;

            BigDecimal price = product.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(productId);
            item.setProductName(product.getName());
            item.setProductImage(product.getMainImage());
            item.setPrice(price);
            item.setQuantity(quantity);
            item.setTotalPrice(itemTotal);
            items.add(item);

            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreight(BigDecimal.ZERO);

        orderInfoService.save(order);

        // 保存订单明细
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
        }
        orderItemService.batchSave(items);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("totalAmount", totalAmount);
        return ApiResponse.success(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        OrderInfo order = orderInfoService.getById(id);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(4); // 已取消
            orderInfoService.update(order);
        }
        return ApiResponse.success();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/pay")
    public ApiResponse<Void> pay(@PathVariable Long id) {
        OrderInfo order = orderInfoService.getById(id);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1); // 待发货
            orderInfoService.update(order);
        }
        return ApiResponse.success();
    }

    private Long getUserId(Authentication authentication) {
        if (authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        return (Long) authentication.getPrincipal();
    }
}
