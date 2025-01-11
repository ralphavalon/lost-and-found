package com.demo.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.demo.project.model.User;

@FeignClient(name = "users", url = "${users.url}")
public interface UserClient {

    @GetMapping(value = "/users/{id}")
    User getUser(@PathVariable Long id);

}


