package cn.mikuyun.qjshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.mikuyun.qjshopbackend.mapper")
public class QjShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QjShopBackendApplication.class, args);
        System.out.println("成功启动,访问接口:http://localhost:8080");
    }

}
