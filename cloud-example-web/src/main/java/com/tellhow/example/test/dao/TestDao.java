package com.tellhow.example.test.dao;

import com.tellhow.example.test.entity.TestEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 15:13 yanfei 新建
 * @since JDK1.8
 */
@Mapper
public interface TestDao {
    @Select("SELECT * FROM test_table WHERE id = #{id}")
    TestEntity getById(int id);

    @Insert("INSERT INTO test_table(name, age) VALUES(#{name}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TestEntity entity);
}
