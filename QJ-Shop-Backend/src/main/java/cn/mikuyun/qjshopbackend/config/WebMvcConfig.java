package cn.mikuyun.qjshopbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 映射上传文件目录到URL路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + new File(uploadDir).getAbsolutePath() + "/");

        // 默认静态资源（用于部署前端打包文件）
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
