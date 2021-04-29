package com.hj.manageservice.client;

import com.hj.commonutils.R;
import com.hj.commonutils.vo.UserVos;
import com.hj.manageservice.client.impl.AclClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Component
@FeignClient(value = "service-acl")
public interface  AclClient {

    @GetMapping("/user/acl/user/getAll")
    UserVos getUserList();
}
