package com.hj.educenter.client;


import com.hj.commonutils.vo.CourseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(value = "service-manage")
public interface CourseClient {
    @GetMapping("/manageservice/educourse/client/batches")
    public List<CourseVo> getBatchesCourse(@RequestParam("ids") List<String> ids);
    @GetMapping("/manageservice/educourse/client/courseById/{id}")
    public  CourseVo getCourse(@PathVariable("id") String id);
    @PutMapping("/manageservice/educourse/client/changeNumber")
    public Boolean changeCourseNumber(@RequestBody CourseVo courseVo);
}
