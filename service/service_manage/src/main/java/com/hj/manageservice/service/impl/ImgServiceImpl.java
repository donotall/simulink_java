package com.hj.manageservice.service.impl;

import com.hj.manageservice.client.OssClient;
import com.hj.manageservice.entity.Img;
import com.hj.manageservice.mapper.ImgMapper;
import com.hj.manageservice.service.ImgService;
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
 * @since 2021-04-24
 */
@Service
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements ImgService {

    @Autowired
    private OssClient ossClient;
    @Autowired
    private ImgService imgService;
    @Override
    public Boolean uploadImg(String[] imgLists, String uId,String eId) {
        List<String> strings = ossClient.uploadImg(imgLists, uId,eId);
        List<Img> imgList = new ArrayList<>();
        for (String img:strings) {
            Img img1 = new Img();
            img1.setExperimentId(eId);
            img1.setUserId(uId);
            img1.setUrl(img);
            imgList.add(img1);
        }
        return imgService.saveBatch(imgList);
    }
}
