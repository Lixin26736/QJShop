package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.dto.auth.LoginRequest;
import cn.mikuyun.qjshopbackend.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
