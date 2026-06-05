package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.entity.CustomerService;

import java.util.List;

public interface CustomerServiceService {

    List<CustomerService> listByUserId(Long userId);

    CustomerService sendMessage(CustomerService message);

    List<CustomerService> listUnreadByAdmin();

    Long countUnreadByAdmin();

    void markRead(Long id);
}
