package com.hj.manageservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.commonutils.R;
import com.hj.manageservice.entity.XpcAttr;
import com.hj.manageservice.entity.vo.XpcAttrVo;
import com.hj.manageservice.service.XpcAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/{page}/{limit}")
    public  R getAttrPage(@PathVariable Long page, @PathVariable Long limit,XpcAttrVo xpcAttrVo){
        Page<XpcAttr> pageParam = new Page<>(page, limit);
        QueryWrapper<XpcAttr> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(xpcAttrVo.getIp())){
            wrapper.like("ip",xpcAttrVo.getIp());
        }
        if(!StringUtils.isEmpty(xpcAttrVo.getPort())){
            wrapper.like("port",xpcAttrVo.getPort());
        }
        xpcAttrService.page(pageParam,wrapper);
        return R.ok().data("list",pageParam.getRecords()).data("total",pageParam.getTotal());
    }
    @PutMapping("/update")
    public R updateXpcAttr(@RequestBody XpcAttr xpcAttr){
        boolean updateById = xpcAttrService.updateById(xpcAttr);
        return updateById?R.ok():R.error();
    }
    @PostMapping("/saveBatch")
    public R saveBatch(@RequestParam(value = "file", required = true) MultipartFile file){
       xpcAttrService.saveBatchUseFile(file,xpcAttrService);
       return R.ok();
    }
}

