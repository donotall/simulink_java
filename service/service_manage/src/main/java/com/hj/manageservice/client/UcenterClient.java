package com.hj.manageservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Component
@FeignClient(value = "service-ucenter")
public interface UcenterClient {
    @GetMapping("/educenter/member/course/{courseId}")
    Map<String,String> getUserCourseId(@PathVariable("courseId") String courseId );
    @GetMapping("/educenter/member/getInfoUc/{memberId}")
    com.hj.commonutils.vo.UcenterMember getInfo(@PathVariable String memberId);
}
