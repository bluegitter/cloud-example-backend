package com.tellhow.example.test.service;

import com.tellhow.example.test.entity.TestEntity;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 15:11 yanfei 新建
 * @since JDK1.8
 */
// Service 接口和实现类
public interface TestService {
    TestEntity getById(int id);
    void create(TestEntity entity);
}