package cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("customer_service")
public class CustomerService {

    @TableId
    private Long id;
    private Long userId;
    private Long adminId;
    private String content;
    private String image;
    private Integer senderType;
    private Integer isRead;
    private LocalDateTime createTime;
}
