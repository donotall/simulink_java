package com.hj.manageservice.service;

import com.hj.manageservice.entity.StudentScore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.manageservice.entity.vo.ExData;
import com.hj.manageservice.entity.vo.MaxMin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-04-24
 */
public interface StudentScoreService extends IService<StudentScore> {
    // 根据截至时间生成未完成实验学生并给分
    void endTimeScore(String startTime,String endTime);

    MaxMin getMaxMin(String id);

    ExData getExMaxMin(String id);
}
