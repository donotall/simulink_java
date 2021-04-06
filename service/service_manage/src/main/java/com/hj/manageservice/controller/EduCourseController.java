package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.service.EduCourseService;
import com.hj.manageservice.vo.EduCourseVo;
import com.hj.manageservice.vo.TeacherCourse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/manageservice/educourse")
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;
    // 添加班课
    @PostMapping
    public R AddCourse(@RequestBody EduCourse eduCourse){
        boolean flag=  eduCourseService.AddCourse(eduCourse);
        return flag?R.ok():R.error();
    }
    // 修改班课
    @PutMapping("update")
    public R UpdateCourse(@RequestBody EduCourse eduCourse){
        boolean b = eduCourseService.updateById(eduCourse);
        return b?R.ok():R.error();
    }
    // 得到班课
    @ApiOperation(value = "获取班课分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            EduCourseVo eduCourse) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(eduCourse.getTeacherName())) {
            wrapper.like("user_create",eduCourse.getTeacherName());
        }
        if(!StringUtils.isEmpty(eduCourse.getName())) {
            wrapper.like("name",eduCourse.getName());
        }
        if(!StringUtils.isEmpty(eduCourse.getBegin())){
            wrapper.ge("gmt_create",eduCourse.getBegin());
        }
        if(!StringUtils.isEmpty(eduCourse.getEnd())){
            wrapper.le("gmt_modified",eduCourse.getEnd());
        }
        eduCourseService.page(pageParam,wrapper);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }
    // 删除班课
    @DeleteMapping("/{id}")
    public R DeleteCourse(@PathVariable String id){
        boolean flag = eduCourseService.removeCourse(id);
        return flag?R.ok():R.error();
    }
    // 根据id获取班课
    @GetMapping("/{id}")
    public  R getCourseById(@PathVariable String id){
        EduCourse eduCourse = eduCourseService.getById(id);
        return R.ok().data("course",eduCourse);
    }
    // 获取老师和班课的所有关联信息
    @GetMapping("/teachercourse")
    public R getTeacherCourseRelated(){
       List<TeacherCourse> teacherCourseList = eduCourseService.getTeacherCourse();
        return R.ok().data("list",teacherCourseList);
    }
}

