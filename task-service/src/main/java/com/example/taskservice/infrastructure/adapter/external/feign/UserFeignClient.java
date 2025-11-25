package com.example.taskservice.infrastructure.adapter.external.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/users") // URL for local testing
public interface UserFeignClient {

    @GetMapping("/{id}")
    void getUserById(@PathVariable("id") Long id);
}
