package cn.mikuyun.qjshopbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * SPA路由拦截过滤器: 非API/静态资源的请求全部转发到index.html
 */
@Component
@Order(Integer.MIN_VALUE)
public class SpaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI();

        // API请求放行
        if (path.startsWith("/api/")) {
            chain.doFilter(req, res);
            return;
        }
        // 静态资源请求放行(含扩展名)
        if (path.contains(".") && !path.endsWith(".html")) {
            chain.doFilter(req, res);
            return;
        }
        // 其他所有路径转发到index.html (SPA前端路由)
        String query = request.getQueryString();
        String target = "/index.html" + (query != null ? "?" + query : "");
        request.getRequestDispatcher(target).forward(req, res);
    }
}
