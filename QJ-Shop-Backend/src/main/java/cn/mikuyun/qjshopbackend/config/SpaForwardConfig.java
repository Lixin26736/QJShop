package cn.mikuyun.qjshopbackend.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SPA前端路由回退: 非API路径全部返回index.html
 */
@Configuration
public class SpaForwardConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // SPA路由回退: 非API路径全部返回index.html
        registry.addViewController("/admin").setViewName("forward:/index.html");
        registry.addViewController("/admin/").setViewName("forward:/index.html");
        registry.addViewController("/client").setViewName("forward:/index.html");
        registry.addViewController("/client/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/register").setViewName("forward:/index.html");
        registry.addViewController("/admin/login").setViewName("forward:/index.html");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> spaWebServerCustomizer() {
        return factory -> {
            // 确保自定义错误页也回到 index.html
        };
    }
}
