package com.tellhow.example.test.service;

import com.tellhow.cloud.common.user.service.UserInfo;
import com.tellhow.example.test.dao.TestDao;
import com.tellhow.example.test.entity.TestEntity;
import com.tellhow.example.test.service.TestServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.tellhow.example.test.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 16:11 yanfei 新建
 * @since JDK1.8
 */
@ExtendWith(MockitoExtension.class)
public class MyServiceTests {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private TestDao testDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testServiceGetById() {
        TestEntity entity = new TestEntity();
        entity.setId(1);
        entity.setName("Test Name");
        entity.setAge(25);

        Mockito.when(testDao.getById(1)).thenReturn(entity);

        TestEntity result = testService.getById(1);

        assertEquals(entity, result);
    }

    @Test
    public void testServiceCreate() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setAge(25);

        Mockito.doNothing().when(testDao).insert(entity);

        testService.create(entity);

        Mockito.verify(testDao, Mockito.times(1)).insert(entity);
    }

    @Test
    public void shouldReturnErrorWhenInvalidUserIdProvided() {
        // Arrange: 准备无效的用户 ID
        String invalidUserId = "invalid-id";
        UserService userService = new UserService();

        // Act: 调用方法，获取响应
        ErrorResponse response = userService.getUserDetails(invalidUserId);

        // Assert: 验证是否返回错误响应
        assertThat(response.getErrorCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.getMessage()).isEqualTo("User not found");
    }
}