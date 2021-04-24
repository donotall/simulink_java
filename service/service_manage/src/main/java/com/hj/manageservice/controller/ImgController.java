package com.hj.manageservice.controller;


import com.hj.commonutils.R;
import com.hj.manageservice.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/manageservice/img")
public class ImgController {
    @Autowired
    private ImgService imgService;
    @PostMapping("img/{userId}/{exId}")
    private R upload(@RequestBody String[] imgLists ,@PathVariable String userId,@PathVariable String exId){
        Boolean flag = imgService.uploadImg(imgLists,userId,exId);
        return flag? R.ok():R.error();
    }

}

