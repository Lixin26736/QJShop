package cn.mikuyun.qjshopbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(
                jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE);
        String uri = (String) request.getAttribute(
                jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI);

        // 非API路径的404全部返回index.html (SPA路由)
        if (statusCode != null && statusCode == 404 && uri != null && !uri.startsWith("/api/")) {
            return "forward:/index.html";
        }
        return null;
    }
}
