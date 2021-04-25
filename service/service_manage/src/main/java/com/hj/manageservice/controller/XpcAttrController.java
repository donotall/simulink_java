package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.XpcAttr;
import com.hj.manageservice.service.XpcAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hj
 * @since 2021-04-18
 */
@RestController
@RequestMapping("/manageservice/xpcAttr")
public class XpcAttrController {
    @Autowired
    private XpcAttrService xpcAttrService;
    @GetMapping("/getAddr")
    public R getAttr(){
        QueryWrapper<XpcAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("is_use",false);
        wrapper.last("limit 10");
        return R.ok().data("attrList",xpcAttrService.list(wrapper));
    }
    @PostMapping("/addAttr")
    public R AddAttr(@RequestBody XpcAttr xpcAttr){
        boolean save = xpcAttrService.save(xpcAttr);
        return save?R.ok():R.error();
    }
    // 修改xpc的使用情况
    @PutMapping("/updateAttr/{id}")
    public Boolean updateAttr(@PathVariable String id){
        XpcAttr xpcAttr = xpcAttrService.getById(id);
        XpcAttr xpcAttr1 = new XpcAttr();
        xpcAttr1.setIsUse(!xpcAttr.getIsUse());
        xpcAttr1.setId(id);
        return xpcAttrService.updateById(xpcAttr1);
    }
    @GetMapping("/getAttr/{id}")
    public R getAddrById(@PathVariable String id){
        XpcAttr byId = xpcAttrService.getById(id);
        return R.ok().data("attr",byId);
    }
    @DeleteMapping("/deleteAttr/{id}")
    public R deleteAttr(@PathVariable String id){
        boolean b = xpcAttrService.removeById(id);
        return b?R.ok():R.error();
    }
}

