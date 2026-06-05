package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;

import java.util.List;

public interface OrderInfoService {

    PageResult<OrderInfo> page(int pageNum, int pageSize, String orderNo, Integer status, Long userId);

    List<OrderInfo> listAll(String orderNo, Integer status, Long userId);

    OrderInfo getById(Long id);

    void save(OrderInfo orderInfo);

    void update(OrderInfo orderInfo);

    void delete(Long id);
}
