package com.hj.educenter.service;

import com.hj.commonutils.vo.CourseVo;
import com.hj.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.educenter.entity.vo.LoginVo;
import com.hj.educenter.entity.vo.RegisterVo;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author hj
 * @since 2021-02-15
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    UcenterMember getByOpenid(String openid);

    Integer countRegisterByDay(String day);

    List<UcenterMember> getListByCourseId(String courseId);

    boolean ChangeDisabled(String id, Integer disable);

    List<CourseVo> getCourseList(String id, String judge);
}
