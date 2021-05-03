package com.hj.manageservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.client.UcenterClient;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.entity.StudentScore;
import com.hj.manageservice.entity.vo.ExData;
import com.hj.manageservice.mapper.StudentScoreMapper;
import com.hj.manageservice.service.EduExperimentService;
import com.hj.manageservice.service.StudentScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.manageservice.entity.vo.MaxMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hj
 * @since 2021-04-24
 */
@Service
public class StudentScoreServiceImpl extends ServiceImpl<StudentScoreMapper, StudentScore> implements StudentScoreService {

    @Autowired
    private EduExperimentService experimentService;
    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private StudentScoreService studentScoreService;
    @Transactional
    @Override
    public void endTimeScore(String startTime, String endTime) {
        //获取已经结束的实验
        QueryWrapper<EduExperiment> eduExperimentQueryWrapper = new QueryWrapper<>();
        eduExperimentQueryWrapper.ge("gmt_end",startTime);
        eduExperimentQueryWrapper.le("gmt_end",endTime);
        List<EduExperiment> list = experimentService.list(eduExperimentQueryWrapper);
        List<StudentScore> studentScoreList = new ArrayList<>();

        //根据已经结束的实验获取当前实验所在的班课
        Set<String> courseIds = new HashSet<>();
        for (EduExperiment experiment :list) {
            courseIds.add(experiment.getCourseId());
        }
        //根据班课名称获取班课中所有用户
        for (String courseId: courseIds ) {
            Map<String, String> userCourseId = ucenterClient.getUserCourseId(courseId);
            for (EduExperiment experiment :list) {
                if(experiment.getCourseId().equals(courseId)){
                    QueryWrapper<StudentScore> scoreQueryWrapper  = new QueryWrapper<>();
                    scoreQueryWrapper.eq("experiment_id",experiment.getId());
                    //根据实验id获取已完成的用户id
                    List<StudentScore> studentScores = baseMapper.selectList(scoreQueryWrapper);
                    //获取不同的用户id
                    for (StudentScore studentScore:studentScores) {
                        String value = userCourseId.get(studentScore.getUserId());
                        if(value!=null){
                            userCourseId.put(studentScore.getUserId(),"0");
                        }
                    }
                    //遍历
                    userCourseId.forEach((key,value)->{
                        //判断是否为已完成学生
                        if(value.equals(courseId)){
                            StudentScore studentScore = new StudentScore();
                            studentScore.setUserId(key);
                            studentScore.setScore(0);
                            studentScore.setFinished(false);
                            studentScore.setIsScore(true);
                            studentScore.setExperimentId(experiment.getId());
                            studentScoreList.add(studentScore);
                        }
                    });
                }
            }

        }
        //批量添加数据
        studentScoreService.saveBatch(studentScoreList);
    }

    // 返回最大最小值
    @Override
    public MaxMin getMaxMin(String id) {
        return baseMapper.getMaxMIn(id);
    }

    @Override
    public ExData getExMaxMin(String id) {
        return baseMapper.getExData(id);
    }
}
