package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.entity.StudentScore;
import com.hj.manageservice.service.StudentScoreService;
import com.hj.manageservice.vo.EduCourseVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hj
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/manageservice/studentScore")
public class StudentScoreController {
    @Autowired
    private StudentScoreService studentScoreService;
    @PostMapping("finish")
    public R finishEx(@RequestBody StudentScore studentScore){
        studentScore.setFinished(true);
        studentScore.setScore(0);
        studentScoreService.save(studentScore);
        return R.ok();
    }
    //根据实验id获取学生的成绩
    @ApiOperation(value = "根据实验id获取学生的成绩")
    @GetMapping("client/{id}/{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @PathVariable String id) {
        Page<StudentScore> pageParam = new Page<>(page, limit);
        QueryWrapper<StudentScore> wrapper = new QueryWrapper<>();
        wrapper.eq("experiment_id", id);
        studentScoreService.page(pageParam, wrapper);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }
    // 修改学生成绩
    @PutMapping("setScore")
    public R setScore(@RequestBody StudentScore studentScore){
        QueryWrapper<StudentScore> wrapper = new QueryWrapper<>();
        wrapper.eq("experiment_id",studentScore.getExperimentId());
        wrapper.eq("user_id",studentScore.getUserId());
        boolean update = studentScoreService.update(studentScore, wrapper);
        return update?R.ok():R.error();
    }

}

