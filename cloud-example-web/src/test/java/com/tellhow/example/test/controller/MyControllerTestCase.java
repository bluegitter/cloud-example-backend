package com.tellhow.example.test.controller;

import com.tellhow.example.test.entity.TestEntity;
import com.tellhow.example.test.service.TestService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 15:30 yanfei 新建
 * @since JDK1.8
 */
// 测试类

@ExtendWith(MockitoExtension.class)
public class MyControllerTestCase {

    @InjectMocks
    private MyController myController;

    @Mock
    private TestService testService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() {
        TestEntity entity = new TestEntity();
        entity.setId(1);
        entity.setName("Test Name");
        entity.setAge(25);

        Mockito.when(testService.getById(1)).thenReturn(entity);

        ResponseEntity<TestEntity> response = myController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entity, response.getBody());
    }

    @Test
    public void testCreate() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setAge(25);

        Mockito.doNothing().when(testService).create(entity);

        ResponseEntity<String> response = myController.create(entity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Created successfully", response.getBody());
    }
}