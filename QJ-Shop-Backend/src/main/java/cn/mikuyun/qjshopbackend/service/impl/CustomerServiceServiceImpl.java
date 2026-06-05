package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.CustomerService;
import cn.mikuyun.qjshopbackend.mapper.CustomerServiceMapper;
import cn.mikuyun.qjshopbackend.service.CustomerServiceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceServiceImpl implements CustomerServiceService {

    private final CustomerServiceMapper customerServiceMapper;

    @Override
    public List<CustomerService> listByUserId(Long userId) {
        LambdaQueryWrapper<CustomerService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerService::getUserId, userId)
                .orderByAsc(CustomerService::getCreateTime);
        return customerServiceMapper.selectList(wrapper);
    }

    @Override
    public CustomerService sendMessage(CustomerService message) {
        message.setId(null);
        message.setIsRead(0);
        customerServiceMapper.insert(message);
        return message;
    }

    @Override
    public List<CustomerService> listUnreadByAdmin() {
        LambdaQueryWrapper<CustomerService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerService::getIsRead, 0)
                .eq(CustomerService::getSenderType, 0) // 用户发的消息
                .orderByAsc(CustomerService::getCreateTime);
        return customerServiceMapper.selectList(wrapper);
    }

    @Override
    public Long countUnreadByAdmin() {
        LambdaQueryWrapper<CustomerService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerService::getIsRead, 0)
                .eq(CustomerService::getSenderType, 0);
        return customerServiceMapper.selectCount(wrapper);
    }

    @Override
    public void markRead(Long id) {
        CustomerService cs = new CustomerService();
        cs.setId(id);
        cs.setIsRead(1);
        customerServiceMapper.updateById(cs);
    }
}
