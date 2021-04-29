package com.hj.manageservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "service-oss")
public interface OssClient {
    @PostMapping("eduoss/fileoss/img/{id}/{eid}")
    List<String> uploadImg(@RequestBody String[] imgLists, @PathVariable("id") String id,@PathVariable("eid") String eid);
}
