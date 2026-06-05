package cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {

    @TableId
    private Long id;
    private Long productId;
    private Long userId;
    private Long orderId;
    private Integer rating;
    private String content;
    private String images;
    private String reply;
    private LocalDateTime replyTime;
    private Integer isAnonymous;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
