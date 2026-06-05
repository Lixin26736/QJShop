package cn.mikuyun.qjshopbackend.controller;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping("/image")
    public ApiResponse<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "文件不能为空");
        }

        // 限制文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.error(400, "只支持图片文件");
        }

        try {
            // 按日期分目录
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadPath = Paths.get(uploadDir, dateDir);
            Files.createDirectories(uploadPath);

            // 生成唯一文件名
            String originalName = file.getOriginalFilename();
            String ext = originalName != null && originalName.contains(".") ?
                    originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
            String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;

            // 保存文件
            File destFile = uploadPath.resolve(newFileName).toFile();
            file.transferTo(destFile);

            // 返回访问URL
            String fileUrl = "/" + dateDir + "/" + newFileName;
            Map<String, String> result = new LinkedHashMap<>();
            result.put("url", fileUrl);
            result.put("name", originalName);
            return ApiResponse.success(result);
        } catch (IOException e) {
            return ApiResponse.error(500, "文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/images")
    public ApiResponse<List<Map<String, String>>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        List<Map<String, String>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            ApiResponse<Map<String, String>> result = uploadImage(file);
            if (result.getCode() == 200 && result.getData() != null) {
                results.add(result.getData());
            }
        }
        return ApiResponse.success(results);
    }
}
