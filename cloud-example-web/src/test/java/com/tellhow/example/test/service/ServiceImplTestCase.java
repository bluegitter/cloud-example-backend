package com.tellhow.example.test.service;

import com.tellhow.example.test.App;
import com.tellhow.example.test.dao.TestDao;
import com.tellhow.example.test.entity.TestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 16:28 yanfei 新建
 * @since JDK1.8
 */
@SpringBootTest(classes = {App.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ServiceImplTestCase {
    @Autowired
    private TestService testService;

    @Autowired
    private TestDao testDao;

    @BeforeEach
    public void testGetById() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setAge(25);

        testDao.insert(entity);

        TestEntity result = testService.getById(entity.getId());

        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getAge(), result.getAge());
    }

    @Test
    public void testCreate() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setAge(25);

        testService.create(entity);

        TestEntity result = testDao.getById(entity.getId());
        assertNotNull(result);
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getAge(), result.getAge());
    }
}
