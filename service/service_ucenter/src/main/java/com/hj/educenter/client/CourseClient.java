package com.hj.educenter.client;


import com.hj.commonutils.vo.CourseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "service-manage")
public interface CourseClient {
    @GetMapping("/manageservice/educourse/batches")
    public List<CourseVo> getBatchesCourse(@RequestParam("ids") List<String> ids);
}
