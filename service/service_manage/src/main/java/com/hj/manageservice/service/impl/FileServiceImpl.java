package com.hj.manageservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.entity.File;
import com.hj.manageservice.mapper.FileMapper;
import com.hj.manageservice.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    //获取文件url List 集合
    @Override
    public List<File> getFileList(String experimentId) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("experiment_id",experimentId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteByExperimentId(String experimentId) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("experiment_id",experimentId);
        int delete = baseMapper.delete(wrapper);
        return delete>=0;
    }
}
