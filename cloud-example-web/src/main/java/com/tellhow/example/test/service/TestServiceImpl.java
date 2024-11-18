package com.tellhow.example.test.service;

import com.tellhow.example.test.dao.TestDao;
import com.tellhow.example.test.entity.TestEntity;
import com.tellhow.example.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 15:12 yanfei 新建
 * @since JDK1.8
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public TestEntity getById(int id) {
        return testDao.getById(id);
    }

    @Override
    public void create(TestEntity entity) {
        testDao.insert(entity);
    }
}