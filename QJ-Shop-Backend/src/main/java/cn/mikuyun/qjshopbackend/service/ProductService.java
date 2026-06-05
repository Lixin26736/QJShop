package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Product;

import java.util.List;

public interface ProductService {

    PageResult<Product> page(int pageNum, int pageSize, String keyword, Integer status, Long categoryId);

    List<Product> listAll(String keyword, Integer status, Long categoryId);

    Product getById(Long id);

    void save(Product product);

    void update(Product product);

    void delete(Long id);

    List<Product> getHotProducts();

    List<Product> getNewProducts();
}
