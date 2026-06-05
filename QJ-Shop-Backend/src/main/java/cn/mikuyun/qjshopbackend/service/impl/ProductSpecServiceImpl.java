package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.entity.ProductSpec;
import cn.mikuyun.qjshopbackend.mapper.ProductSpecMapper;
import cn.mikuyun.qjshopbackend.service.ProductSpecService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecServiceImpl implements ProductSpecService {

    private final ProductSpecMapper productSpecMapper;

    @Override
    public List<ProductSpec> listByProductId(Long productId) {
        LambdaQueryWrapper<ProductSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSpec::getProductId, productId)
                .orderByAsc(ProductSpec::getCreateTime);
        return productSpecMapper.selectList(wrapper);
    }

    @Override
    public ProductSpec getById(Long id) {
        return productSpecMapper.selectById(id);
    }

    @Override
    public void save(ProductSpec productSpec) {
        productSpec.setId(null);
        productSpecMapper.insert(productSpec);
    }

    @Override
    public void update(ProductSpec productSpec) {
        productSpecMapper.updateById(productSpec);
    }

    @Override
    public void delete(Long id) {
        productSpecMapper.deleteById(id);
    }

    @Override
    public void batchSave(Long productId, List<ProductSpec> specs) {
        // 先删除旧规格
        LambdaQueryWrapper<ProductSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSpec::getProductId, productId);
        productSpecMapper.delete(wrapper);
        // 批量插入新规格
        for (ProductSpec spec : specs) {
            spec.setId(null);
            spec.setProductId(productId);
            productSpecMapper.insert(spec);
        }
    }
}
