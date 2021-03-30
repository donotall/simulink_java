package com.hj.manageservice.service;

import com.hj.manageservice.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
public interface FileService extends IService<File> {

    List<String> getFileList(String experimentId);
    boolean deleteByExperimentId(String experimentId);
}
