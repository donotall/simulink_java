package com.hj.manageservice.mapper;

import com.hj.manageservice.entity.StudentScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.manageservice.entity.vo.MaxMin;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hj
 * @since 2021-04-24
 */
public interface StudentScoreMapper extends BaseMapper<StudentScore> {
    MaxMin getMaxMIn(String id);
}
