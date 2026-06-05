package cn.mikuyun.qjshopbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}
