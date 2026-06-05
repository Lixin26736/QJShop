package cn.mikuyun.qjshopbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.dto.user.UserProfileUpdateRequest;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.mapper.UserMapper;
import cn.mikuyun.qjshopbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<User> page(int pageNum, int pageSize, String keyword, Integer role, Integer status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(keyword),
                        w -> w.like(User::getUsername, keyword)
                                .or().like(User::getNickname, keyword)
                                .or().like(User::getPhone, keyword))
                .eq(role != null, User::getRole, role)
                .eq(status != null, User::getStatus, status)
                .orderByDesc(User::getId);
        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public List<User> listAll(String keyword, Integer role, Integer status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(keyword),
                        w -> w.like(User::getUsername, keyword)
                                .or().like(User::getNickname, keyword)
                                .or().like(User::getPhone, keyword))
                .eq(role != null, User::getRole, role)
                .eq(status != null, User::getStatus, status)
                .orderByDesc(User::getId);
        return userMapper.selectList(wrapper);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void save(User user) {
        user.setId(null);
        if (StrUtil.isBlank(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).last("limit 1");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateProfileByUsername(String username, UserProfileUpdateRequest request) {
        User currentUser = getByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (request.getGender() != null && request.getGender() != 0 && request.getGender() != 1) {
            throw new RuntimeException("性别参数不合法");
        }

        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setNickname(request.getNickname());
        updateUser.setPhone(request.getPhone());
        updateUser.setEmail(request.getEmail());
        updateUser.setAvatar(request.getAvatar());
        updateUser.setGender(request.getGender());
        updateUser.setBirthday(request.getBirthday());
        userMapper.updateById(updateUser);
    }
}
