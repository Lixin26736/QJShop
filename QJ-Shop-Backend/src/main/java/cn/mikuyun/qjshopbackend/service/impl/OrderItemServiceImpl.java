package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.OrderItem;
import cn.mikuyun.qjshopbackend.mapper.OrderItemMapper;
import cn.mikuyun.qjshopbackend.service.OrderItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> listByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    @Override
    public void batchSave(List<OrderItem> items) {
        for (OrderItem item : items) {
            item.setId(null);
            orderItemMapper.insert(item);
        }
    }
}
