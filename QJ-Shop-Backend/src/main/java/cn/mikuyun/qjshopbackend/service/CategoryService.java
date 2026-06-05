package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Category;

import java.util.List;

public interface CategoryService {

    PageResult<Category> page(int pageNum, int pageSize, String keyword, Integer status, Long parentId);

    Category getById(Long id);

    void save(Category category);

    void update(Category category);

    void delete(Long id);

    /**
     * 获取所有一级分类(parent_id = 0)
     */
    List<Category> getFirstCategories();

    /**
     * 获取指定一级分类下的二级分类
     */
    List<Category> getSecondCategories(Long parentId);
}
