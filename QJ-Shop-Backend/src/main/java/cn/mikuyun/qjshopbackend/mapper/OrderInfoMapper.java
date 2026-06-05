package cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("select ifnull(sum(pay_amount), 0) from order_info")
    BigDecimal sumPayAmount();

    @Select("select count(1) from order_info where create_time >= #{start} and create_time < #{end}")
    Long countTodayOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("select ifnull(sum(pay_amount), 0) from order_info where create_time >= #{start} and create_time < #{end}")
    BigDecimal sumTodayPayAmount(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
