package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.service.EduExperimentService;
import com.hj.manageservice.service.FileService;
import com.hj.manageservice.vo.EduCourseVo;
import com.hj.manageservice.vo.EduExperimentVo;
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
@RequestMapping("/manageservice/edu-experiment")
public class EduExperimentController {
    @Autowired
    private EduExperimentService experimentService;
    @Autowired
    private FileService fileService;
    // 添加实验
    @PostMapping("/{courserId}")
    public R AddCourse(@RequestBody EduExperimentVo eduExperiment, @PathVariable String courseId){
        boolean flag=  experimentService.AddExperiment(eduExperiment,courseId);
        return flag?R.ok():R.error();
    }
    // 修改实验
    @PutMapping("update")
    public R UpdateCourse(@RequestBody EduExperiment eduExperiment){
        boolean b = experimentService.updateById(eduExperiment);
        return b?R.ok():R.error();
    }
    // 根据课程id得到实验
    @GetMapping("/list/{courseId}")
    public R GetExperimentByCourseId(@PathVariable String courseId){

        List<EduExperiment> eduExperimentList =  experimentService.GetListEXperiment(courseId);
        return R.ok().data("list",eduExperimentList);
    }
    // 根据实验id获取实验详情
    @GetMapping("/detailed/{experimentId}")
    public R GetExperimentById(@PathVariable String experimentId ){
        //获取实验信息
        EduExperiment eduExperiment = experimentService.getById(experimentId);
        //获取实验文件列表
       List<String> urlList = fileService.getFileList(experimentId);
       return R.ok().data("experiment",eduExperiment).data("fileUrls",urlList);
    }
    // 删除班课
    @DeleteMapping("/{id}")
    public R DeleteCourse(@PathVariable String id){
        boolean flag = experimentService.removeById(id);
        return flag?R.ok():R.error();
    }

}

