package cn.mikuyun.qjshopbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_spec")
public class ProductSpec {

    @TableId
    private Long id;
    private Long productId;
    private String specName;
    private String specValue;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
