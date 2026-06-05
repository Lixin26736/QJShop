package cn.mikuyun.qjshopbackend.mapper;

import cn.mikuyun.qjshopbackend.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AddressMapper extends BaseMapper<Address> {

    @Update("update address set is_default = 0 where user_id = #{userId}")
    void clearDefault(@Param("userId") Long userId);
}
