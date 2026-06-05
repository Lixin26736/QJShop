package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> listByUserId(Long userId);

    Address getById(Long id);

    void save(Address address);

    void update(Address address);

    void delete(Long id);

    void setDefault(Long id, Long userId);
}
