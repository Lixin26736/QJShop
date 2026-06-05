package cn.mikuyun.qjshopbackend.controller;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.entity.Banner;
import cn.mikuyun.qjshopbackend.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ApiResponse<List<Banner>> list(@RequestParam(defaultValue = "home") String position) {
        return ApiResponse.success(bannerService.listActiveByPosition(position));
    }
}
