package com.hj.educenter.controller;


import com.hj.commonutils.JwtUtils;
import com.hj.commonutils.R;
import com.hj.educenter.entity.UcenterMember;
import com.hj.educenter.entity.UserCourse;
import com.hj.educenter.entity.vo.LoginVo;
import com.hj.educenter.entity.vo.RegisterVo;
import com.hj.educenter.mapper.UserCourseMapper;
import com.hj.educenter.service.UcenterMemberService;
import com.hj.educenter.service.UserCourseService;
import com.hj.servicebase.exceptionhandler.SLException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author hj
 * @since 2021-02-15
 */
@RestController
@RequestMapping("/educenter/member/")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;
    @Autowired
    private UserCourseService userCourseService;
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember ucenterMember = memberService.getById(memberId);
            return R.ok().data("item", ucenterMember);
        }catch (Exception e){
            e.printStackTrace();
            throw new SLException(20001,"error");
        }
    }
    @GetMapping("getInfoUc/{memberId}")
    public com.hj.commonutils.vo.UcenterMember getInfo(@PathVariable String memberId) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(memberId);
        com.hj.commonutils.vo.UcenterMember member = new com.hj.commonutils.vo.UcenterMember();
        BeanUtils.copyProperties(ucenterMember,member);
        return member;
    }
    @GetMapping(value = "countregister/{day}")
    public R registerCount(
            @PathVariable String day){
        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }
    // 根据课程id获取参见班课人的信息
    @GetMapping("course/{courseId}")
    public R getUserByCourseId(@PathVariable String courseId ){
        List<UcenterMember> users = memberService.getListByCourseId(courseId);
        return R.ok().data("users",users);
    }
    //加入班课
    @PostMapping("joinCourse/{classId}")
    public R JoinCourse(HttpServletRequest request,@PathVariable String classId){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UserCourse userCourse = new UserCourse();
        userCourse.setClassid(classId);
        userCourse.setUserid(memberId);
        boolean save = userCourseService.save(userCourse);
        return save?R.ok():R.error();
    }
}

