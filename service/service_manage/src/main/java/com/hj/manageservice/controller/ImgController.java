package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.Img;
import com.hj.manageservice.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/manageservice/img")
public class ImgController {
    @Autowired
    private ImgService imgService;
    @PostMapping("img/{userId}/{exId}")
    private R upload(@RequestBody String[] imgLists ,@PathVariable String userId,@PathVariable String exId){
        Boolean flag = imgService.uploadImg(imgLists,userId,exId);
        return flag? R.ok():R.error();
    }
    @GetMapping("img/{userId}/{exId}")
    private R getImgList(@PathVariable String userId,@PathVariable String exId){
        QueryWrapper<Img> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("experiment_id",exId);
        List<Img> list = imgService.list(wrapper);
        return R.ok().data("imgUrl",list);
    }

}

