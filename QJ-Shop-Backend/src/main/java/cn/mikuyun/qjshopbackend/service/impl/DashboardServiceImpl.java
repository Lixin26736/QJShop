package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.mapper.OrderInfoMapper;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.mapper.UserMapper;
import cn.mikuyun.qjshopbackend.service.DashboardService;
import cn.mikuyun.qjshopbackend.vo.DashboardStatsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final OrderInfoMapper orderInfoMapper;

    @Override
    public DashboardStatsVO stats() {
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>());
        Long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>());
        Long orderCount = orderInfoMapper.selectCount(new LambdaQueryWrapper<OrderInfo>());

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        Long todayOrderCount = orderInfoMapper.countTodayOrders(start, end);
        BigDecimal totalSales = orderInfoMapper.sumPayAmount();
        BigDecimal todaySales = orderInfoMapper.sumTodayPayAmount(start, end);

        return new DashboardStatsVO(
                userCount == null ? 0L : userCount,
                productCount == null ? 0L : productCount,
                orderCount == null ? 0L : orderCount,
                todayOrderCount == null ? 0L : todayOrderCount,
                totalSales == null ? BigDecimal.ZERO : totalSales,
                todaySales == null ? BigDecimal.ZERO : todaySales
        );
    }
}
