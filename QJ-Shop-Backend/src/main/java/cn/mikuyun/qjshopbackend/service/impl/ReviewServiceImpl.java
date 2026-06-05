package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Review;
import cn.mikuyun.qjshopbackend.mapper.ReviewMapper;
import cn.mikuyun.qjshopbackend.service.ReviewService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public PageResult<Review> pageByProductId(int pageNum, int pageSize, Long productId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getProductId, productId)
                .eq(Review::getStatus, 1)
                .orderByDesc(Review::getCreateTime);
        Page<Review> page = reviewMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<Review> pageAll(int pageNum, int pageSize, Long productId, Integer status) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(productId != null, Review::getProductId, productId)
                .eq(status != null, Review::getStatus, status)
                .orderByDesc(Review::getCreateTime);
        Page<Review> page = reviewMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public void save(Review review) {
        review.setId(null);
        review.setStatus(1);
        reviewMapper.insert(review);
    }

    @Override
    public void reply(Long id, String reply) {
        Review review = new Review();
        review.setId(id);
        review.setReply(reply);
        review.setReplyTime(LocalDateTime.now());
        reviewMapper.updateById(review);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Review review = new Review();
        review.setId(id);
        review.setStatus(status);
        reviewMapper.updateById(review);
    }

    @Override
    public void delete(Long id) {
        reviewMapper.deleteById(id);
    }
}
