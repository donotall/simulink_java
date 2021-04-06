package com.hj.aclservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.aclservice.entity.User;
import com.hj.aclservice.service.UserService;
import com.hj.commonutils.R;
import com.hj.commonutils.vo.UserVo;
import com.hj.commonutils.vo.UserVos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/acl/user")
public class ManageContoller {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public UserVos getUserList(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ne("username","admin");
        List<User> users = userService.list(wrapper);
        UserVos userVos1 = new UserVos();
        List<UserVo>  userVos2 = new ArrayList<>();
        for (User user:users) {
            UserVo userVo = new UserVo();
            userVo.setId(user.getId());
            userVo.setName(user.getNickName());
            userVos2.add(userVo);
        }
        userVos1.setUserVoList(userVos2);
        return userVos1;
    }
}
