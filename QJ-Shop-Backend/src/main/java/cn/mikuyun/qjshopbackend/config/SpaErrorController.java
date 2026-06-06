package cn.mikuyun.qjshopbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import jakarta.servlet.ServletException;

@Controller
public class SpaErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer statusCode = (Integer) request.getAttribute(
                jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE);
        String uri = (String) request.getAttribute(
                jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI);

        // 非API路径的所有错误全部返回index.html
        if (uri != null && !uri.startsWith("/api/")) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("text/html;charset=UTF-8");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }
        // API路径返回JSON错误
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode != null ? statusCode : 500);
        response.getWriter().write("{\"code\":500,\"message\":\"服务器异常\"}");
    }
}
