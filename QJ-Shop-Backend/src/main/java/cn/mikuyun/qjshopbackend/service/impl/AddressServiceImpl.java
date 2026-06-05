package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.Address;
import cn.mikuyun.qjshopbackend.mapper.AddressMapper;
import cn.mikuyun.qjshopbackend.service.AddressService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    public List<Address> listByUserId(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        return addressMapper.selectList(wrapper);
    }

    @Override
    public Address getById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public void save(Address address) {
        address.setId(null);
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.insert(address);
    }

    @Override
    public void update(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.updateById(address);
    }

    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    @Override
    public void setDefault(Long id, Long userId) {
        addressMapper.clearDefault(userId);
        Address address = new Address();
        address.setId(id);
        address.setIsDefault(1);
        addressMapper.updateById(address);
    }
}
