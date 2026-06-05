package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;

import java.util.List;

public interface UserService {

    PageResult<User> page(int pageNum, int pageSize, String keyword, Integer role, Integer status);

    List<User> listAll(String keyword, Integer role, Integer status);

    User getById(Long id);

    void save(User user);

    void update(User user);

    void delete(Long id);

    User getByUsername(String username);

    void updateProfileByUsername(String username, UserProfileUpdateRequest request);
}
