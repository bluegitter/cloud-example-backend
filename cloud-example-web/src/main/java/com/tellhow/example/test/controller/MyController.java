package com.tellhow.example.test.controller;

import com.tellhow.example.test.entity.TestEntity;
import com.tellhow.example.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/10/24 15:11 yanfei 新建
 * @since JDK1.8
 */
@RestController
@RequestMapping("/api/test")
public class MyController {

    @Autowired
    private TestService testService;

    @GetMapping("/{id}")
    public ResponseEntity<TestEntity> getById(@PathVariable("id") int id) {
        TestEntity entity = testService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody TestEntity entity) {
        testService.create(entity);
        return ResponseEntity.ok("Created successfully");
    }
}
