package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Favorite;

public interface FavoriteService {

    boolean isFavorited(Long userId, Long productId);

    void add(Long userId, Long productId);

    void remove(Long userId, Long productId);

    PageResult<Favorite> page(int pageNum, int pageSize, Long userId);
}
