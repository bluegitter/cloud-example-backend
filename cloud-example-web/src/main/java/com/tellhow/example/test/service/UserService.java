package com.tellhow.example.test.service;

import com.tellhow.cloud.common.user.service.UserInfo;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/11/18 16:54 yanfei 新建
 * @since JDK1.8
 */
public class UserService {
    public UserInfo getUser() {
        return null;
    }

    public ErrorResponse getUserDetails(String invalidUserId) {
        return new ErrorResponse("USER_NOT_FOUND", "User not found");
    }
}
