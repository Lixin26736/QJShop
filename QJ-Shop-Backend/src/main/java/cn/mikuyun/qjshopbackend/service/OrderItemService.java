package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> listByOrderId(Long orderId);

    void batchSave(List<OrderItem> items);
}
