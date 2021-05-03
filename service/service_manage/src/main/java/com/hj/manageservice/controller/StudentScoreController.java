package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.commonutils.R;
import com.hj.commonutils.vo.UcenterMember;
import com.hj.manageservice.client.UcenterClient;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.entity.StudentScore;
import com.hj.manageservice.entity.vo.Scores;
import com.hj.manageservice.service.EduExperimentService;
import com.hj.manageservice.service.StudentScoreService;
import com.hj.manageservice.entity.vo.MaxMin;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private EduExperimentService experimentService;
    @Autowired
    private UcenterClient ucenterClient;
    @PostMapping("finish")
    public R finishEx(@RequestBody StudentScore studentScore){
        studentScore.setFinished(true);
        studentScore.setScore(0);
        studentScore.setIsScore(false);
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
        //按照是否评分分组
        wrapper.groupBy("is_score","score");
       studentScoreService.page(pageParam, wrapper);
       List<Scores> scoresList = new ArrayList<>();
       if(!pageParam.getRecords().isEmpty()){
        //根据实验id获取实验和根据用户id获取用户
        for(StudentScore studentScore:pageParam.getRecords()){
            Scores scores = new Scores();
            BeanUtils.copyProperties(studentScore,scores);
            EduExperiment experiment = experimentService.getById(studentScore.getExperimentId());
            scores.setExperimentName(experiment.getName());
            UcenterMember member = ucenterClient.getInfo(studentScore.getUserId());
            scores.setUserName(member.getNickname());
            scoresList.add(scores);
        }
       }

        return R.ok().data("items", scoresList).data("total", pageParam.getTotal());
    }
    // 修改学生成绩
    @PutMapping("setScore/{id}/{score}")
    public R setScore(@PathVariable String id,@PathVariable int score){
        StudentScore studentScore = new StudentScore();
        studentScore.setId(id);
        studentScore.setScore(score);
        studentScore.setIsScore(true);
        studentScore.setFinished(true);
        boolean update = studentScoreService.updateById(studentScore);
        return update?R.ok():R.error();
    }
    //根据用户id获取分数的最大最小值
    @GetMapping("/score/minmax/{id}")
    public R getScoreMaxMinById(@PathVariable String id){
      MaxMin maxMin = studentScoreService.getMaxMin(id);
      return R.ok().data("score",maxMin);
    }

}

