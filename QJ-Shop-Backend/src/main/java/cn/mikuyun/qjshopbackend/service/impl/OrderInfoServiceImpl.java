package cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.OrderInfo;
import cn.mikuyun.qjshopbackend.mapper.OrderInfoMapper;
import cn.mikuyun.qjshopbackend.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl implements OrderInfoService {

    private final OrderInfoMapper orderInfoMapper;

    @Override
    public PageResult<OrderInfo> page(int pageNum, int pageSize, String orderNo, Integer status, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(orderNo), OrderInfo::getOrderNo, orderNo)
                .eq(status != null, OrderInfo::getStatus, status)
                .eq(userId != null, OrderInfo::getUserId, userId)
                .orderByDesc(OrderInfo::getId);
        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public List<OrderInfo> listAll(String orderNo, Integer status, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(orderNo), OrderInfo::getOrderNo, orderNo)
                .eq(status != null, OrderInfo::getStatus, status)
                .eq(userId != null, OrderInfo::getUserId, userId)
                .orderByDesc(OrderInfo::getId);
        return orderInfoMapper.selectList(wrapper);
    }

    @Override
    public OrderInfo getById(Long id) {
        return orderInfoMapper.selectById(id);
    }

    @Override
    public void save(OrderInfo orderInfo) {
        orderInfo.setId(null);
        orderInfoMapper.insert(orderInfo);
    }

    @Override
    public void update(OrderInfo orderInfo) {
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public void delete(Long id) {
        orderInfoMapper.deleteById(id);
    }

    @Override
    public Long countByUserAndStatus(Long userId, Integer status) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId)
                .eq(status != null, OrderInfo::getStatus, status);
        return orderInfoMapper.selectCount(wrapper);
    }

    @Override
    public PageResult<OrderInfo> pageByUser(int pageNum, int pageSize, Long userId, Integer status) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId)
                .eq(status != null, OrderInfo::getStatus, status)
                .orderByDesc(OrderInfo::getId);
        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }
}
