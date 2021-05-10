package com.hj.simulinkservice.controller;

import com.hj.commonutils.R;
import com.hj.commonutils.SimulinkUtils;
import com.hj.simulinkservice.service.SimulinkDataService;
import com.hj.simulinkservice.vo.ScopeVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulink/data/")
public class SimulinkDataController {
    @Autowired
    private SimulinkDataService simulinkDataService;
    @ApiOperation("添加示波器")
    @PostMapping("addScope")
    public R AddScope(@RequestBody ScopeVo scopeVo){
       boolean falg = simulinkDataService.XPCAddScope(scopeVo);
       return falg?R.ok():R.error();
    }
    @ApiOperation("获取数据")
    @GetMapping("/getData/{port}")
    public  R GetData(@PathVariable int port){
        double[] datas= simulinkDataService.GetDataFromXpc(port);
        String[] names = simulinkDataService.GetNameFromXpc(port);
        double time = SimulinkUtils.getInstance.xPCGetExecTime(port);
        return R.ok().data("data",datas).data("currentTime",time).data("SigName",names);
    }
}
