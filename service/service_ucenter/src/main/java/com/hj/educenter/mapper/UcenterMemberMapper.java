package com.hj.educenter.mapper;

import com.hj.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.xml.crypto.Data;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author hj
 * @since 2021-02-15
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    Integer selectRegisterCount(String day);
}
