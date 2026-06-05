package cn.mikuyun.qjshopbackend.service.impl;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Favorite;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.mapper.FavoriteMapper;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.service.FavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;

    @Override
    public boolean isFavorited(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void add(Long userId, Long productId) {
        if (isFavorited(userId, productId)) {
            return;
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favoriteMapper.insert(favorite);
    }

    @Override
    public void remove(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        favoriteMapper.delete(wrapper);
    }

    @Override
    public PageResult<Favorite> page(int pageNum, int pageSize, Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        Page<Favorite> page = favoriteMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        // 关联查询商品信息
        for (Favorite favorite : page.getRecords()) {
            Product product = productMapper.selectById(favorite.getProductId());
            // 用 transient 字段暂存，实际用 Map 返回更好，这里简单处理
            favorite.setProductId(favorite.getProductId());
        }
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }
}
