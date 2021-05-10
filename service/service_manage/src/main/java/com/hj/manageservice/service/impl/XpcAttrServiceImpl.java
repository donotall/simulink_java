package com.hj.manageservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.entity.XpcAttr;
import com.hj.manageservice.entity.excel.XpcAttrData;
import com.hj.manageservice.listener.XpcAttrListener;
import com.hj.manageservice.mapper.XpcAttrMapper;
import com.hj.manageservice.service.XpcAttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hj
 * @since 2021-04-18
 */
@Service
public class XpcAttrServiceImpl extends ServiceImpl<XpcAttrMapper, XpcAttr> implements XpcAttrService {

    @Override
    public void saveBatchUseFile(MultipartFile file, XpcAttrService xpcAttrService) {
        try{
            EasyExcel.read(file.getInputStream(), XpcAttrData.class,new XpcAttrListener(xpcAttrService)).sheet().doRead();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
