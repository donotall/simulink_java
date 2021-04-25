package com.hj.simulinkservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Component
@FeignClient(value = "service-manage")
public interface AttrClient {
    @PutMapping("/manageservice/xpcAttr/updateAttr/{id}")
    public Boolean updateAttr(@PathVariable("id") String id);
}
