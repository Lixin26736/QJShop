package cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReviewMapper extends BaseMapper<Review> {

    @Select("select avg(rating) from review where product_id = #{productId} and status = 1")
    Double avgRating(@Param("productId") Long productId);

    @Select("select count(1) from review where product_id = #{productId} and status = 1")
    Long countByProductId(@Param("productId") Long productId);
}
