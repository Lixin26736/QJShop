package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Review;

public interface ReviewService {

    PageResult<Review> pageByProductId(int pageNum, int pageSize, Long productId);

    PageResult<Review> pageAll(int pageNum, int pageSize, Long productId, Integer status);

    void save(Review review);

    void reply(Long id, String reply);

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
