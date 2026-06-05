package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.entity.ProductSpec;

import java.util.List;

public interface ProductSpecService {

    List<ProductSpec> listByProductId(Long productId);

    ProductSpec getById(Long id);

    void save(ProductSpec productSpec);

    void update(ProductSpec productSpec);

    void delete(Long id);

    void batchSave(Long productId, List<ProductSpec> specs);
}
