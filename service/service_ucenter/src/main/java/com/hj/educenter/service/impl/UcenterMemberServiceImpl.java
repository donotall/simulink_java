package com.hj.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.commonutils.JwtUtils;
import com.hj.commonutils.MD5;
import com.hj.commonutils.vo.CourseVo;
import com.hj.educenter.client.CourseClient;
import com.hj.educenter.entity.UcenterMember;
import com.hj.educenter.entity.UserCourse;
import com.hj.educenter.entity.vo.LoginVo;
import com.hj.educenter.entity.vo.RegisterVo;
import com.hj.educenter.mapper.UcenterMemberMapper;
import com.hj.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.educenter.service.UserCourseService;
import com.hj.servicebase.exceptionhandler.SLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author hj
 * @since 2021-02-15
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserCourseService userCourseService;
    @Autowired
    private UcenterMemberMapper ucenterMemberMapper;
    @Autowired
    private CourseClient courseClient;

    /**
     * 会员登陆
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password)) {
            throw new SLException(20001,"error");
        }

        //获取会员
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(null == member) {
            throw new SLException(20001,"error");
        }

        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())) {
            throw new SLException(20001,"error");
        }

        //校验是否被禁用
        if(member.getIsDisabled()) {
            throw new SLException(20001,"error");
        }

        //使用JWT生成token字符串
        return JwtUtils.getJwtToken(member.getId(), member.getNickname());
    }

    /**
     * 会员注册
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new SLException(20001,"注册失败，有空值");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String mobleCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobleCode)) {
            throw new SLException(20001,"验证码过期");
        }

        //查询数据库中是否存在相同的手机号码
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(count.intValue() > 0) {
            throw new SLException(20001,"手机号已存在");
        }

        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        this.save(member);
    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);

        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }

    /**
     * 根据课程id得到所有在该课程的用户列表
     * @param courseId
     * @return
     */
    @Override
    public List<UcenterMember> getListByCourseId(String courseId) {
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("classid",courseId);
        List<UserCourse> userCourses = userCourseService.list(wrapper);
        List<String> userIds= new ArrayList<>();
        for (UserCourse userCourse : userCourses) {
            userIds.add(userCourse.getId());
        }
        List<UcenterMember> ucenterMembers = baseMapper.selectBatchIds(userIds);
        return ucenterMembers;
    }

    @Override
    public boolean ChangeDisabled(String id, Integer disable) {
        return ucenterMemberMapper.changeDisable(id,disable);
    }

    // 根据用户信息获取班课id
    @Override
    public List<CourseVo> getCourseList(String id, String judge) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        if(judge.equals("openid")){
            wrapper.eq("openid",id);
        }
        if(judge.equals("mobile")){
            wrapper.eq("mobile",id);
        }
        UcenterMember member = baseMapper.selectOne(wrapper);
        // 根据用户id查询班课id
        QueryWrapper<UserCourse> wrapper2 = new QueryWrapper<>();
        wrapper.eq("userid",member.getId());
        List<UserCourse> list = userCourseService.list(wrapper2);
        List<String> ids = new ArrayList<>();
        for(UserCourse userCourse:list){
            ids.add(userCourse.getClassid());
        }
        List<CourseVo> batchesCourse = courseClient.getBatchesCourse(ids);
        return batchesCourse;
    }
}
