package cn.mikuyun.qjshopbackend.vo;

import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsVO {

    private Long userCount;
    private Long productCount;
    private Long orderCount;
    private Long todayOrderCount;
    private BigDecimal totalSales;
    private BigDecimal todaySales;
    private List<OrderInfo> recentOrders;
    private List<Product> hotProducts;
}
