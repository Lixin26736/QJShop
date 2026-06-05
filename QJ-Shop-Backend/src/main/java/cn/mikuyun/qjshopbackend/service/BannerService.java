package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.Banner;

import java.util.List;

public interface BannerService {

    List<Banner> listActiveByPosition(String position);

    PageResult<Banner> page(int pageNum, int pageSize, String keyword, Integer status);

    Banner getById(Long id);

    void save(Banner banner);

    void update(Banner banner);

    void delete(Long id);
}
