package cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {

    @TableId
    private Long id;
    private Long orderId;
    private Long productId;
    private Long specId;
    private String productName;
    private String productImage;
    private String specInfo;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
}
