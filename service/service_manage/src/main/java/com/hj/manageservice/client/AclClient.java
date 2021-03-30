package com.hj.manageservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "service-acl")
public interface  AclClient {
    @GetMapping("getName")
    public String getName();
}
