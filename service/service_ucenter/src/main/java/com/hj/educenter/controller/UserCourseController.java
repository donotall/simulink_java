package com.hj.educenter.controller;


import com.hj.commonutils.R;
import com.hj.educenter.entity.UserCourse;
import com.hj.educenter.service.UserCourseService;
import com.hj.educenter.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/educenter/usercourse")
public class UserCourseController {
    @Autowired
    private UserCourseService userCourseService;
    @PostMapping("joinCourse")
    public R JoinCourse(@RequestBody UserCourse userCourse){
        ResUtil save = userCourseService.JoinCourse(userCourse);
        return save.getFlag()?R.ok().data("message",save.getMessage()):R.error().data("message",save.getMessage());
    }
    @DeleteMapping("/{classId}")
    private R unJoinCourse(@PathVariable String classId,@RequestParam String userId){
       Boolean flag = userCourseService.UnjoinCourse(classId,userId);
       return flag?R.ok():R.error();
    }
}

