package com.hj.simulinkservice.controller;

import com.hj.commonutils.R;
import com.hj.simulinkservice.service.SimulinkService;
import com.hj.simulinkservice.vo.TargetSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/simulink/")
public class SimulinkController {
    @Autowired
    private SimulinkService simulinkService;
    //2.上传文件以及文件的编译
    @ApiOperation("2.上传文件以及文件的编译")
    @PostMapping("file/{port}")
    public R getSimulinkFile(@ApiParam(required = true,
            name = "file",value = "simulink文件") MultipartFile file,@PathVariable int port){
        String filename = simulinkService.UseSimulink(file,port);
        return R.ok().data("fileName",filename);
    }
    //1.获取连接
    @ApiOperation("1.获取连接")
    @GetMapping("connect/{connectId}/{port}")
    public R ConnectSimulinkRealTime(@ApiParam(required = true,value = "simulink机器地址")
                                         @PathVariable(required = true) String connectId,@PathVariable String port){
         int linkport = simulinkService.Conection(connectId,port);
        return linkport!=-1?R.ok().message("连接成功！").data("port",linkport):R.error().message("连接失败！");
    }
    // xpc目标机注册
    @PostMapping("registerTarget")
    public R RegisterTarget(int commType,String ip,String port){
        return R.ok();
    }
    //4.得到目标机设置
    @ApiOperation("4.得到目标机设置")
    @GetMapping("getSetting/{port}")
    public R GetTargetSetting(@PathVariable int port){
        TargetSettingVo settingVo = simulinkService.GetSetting(port);
        return R.ok().data("setting",settingVo);
    }
    //5.修改目标机设置
    @ApiOperation("5.修改目标机设置,注意ts的设置,太小会cpu爆红")
    @PutMapping("changeSetting/{fileName}/{port}")
    public R ChangeTargetSetting(@PathVariable int port,@PathVariable String fileName,@RequestBody TargetSettingVo targetSettingVo){
        boolean flag = simulinkService.ChangeTargetSetting(port,fileName,targetSettingVo);
        return flag? R.ok(): R.error();
    }
    //重置设置的参数值
    @PutMapping("restore/{fileName}/{port}")
    public R RestoreSetting(@PathVariable int port,@PathVariable String fileName){
        boolean b = simulinkService.RestoreSetting(port, fileName);
        return b?R.ok().message("重置设置成功！"):R.error().message("重置设置失败！");
    }
    // 3.将编译好的模型加载到目标机上
    @ApiOperation("3.将编译好的模型加载到目标机上")
    @GetMapping("xpcLoad/{fileName}/{port}")
    public R XpcLoad(@PathVariable int port,@PathVariable String fileName){
        boolean xpcLoad = simulinkService.XpcLoad(port, fileName);
        return xpcLoad?R.ok():R.error();
    }
    // 6.开始运行模型
    @ApiOperation("6.开始运行模型")
    @GetMapping("startModel/{port}")
    public R ModelStrat(@PathVariable int port){
        boolean modelStart = simulinkService.ModelStart(port);
        return modelStart?R.ok():R.error();
    }
    //打开模型
    @ApiOperation("打开模型")
    @GetMapping("model/open/{fileName}")
    public R ModelOpen(@PathVariable String fileName){
        simulinkService.ModelOpen(fileName);
        return R.ok();
    }
    //7.关闭连接卸载模型
    @ApiOperation("7.关闭连接卸载模型")
    @GetMapping("close/{port}")
    public R CloseModel(@PathVariable int port){
        boolean connection = simulinkService.CloseConnection(port);
        return connection?R.ok():R.error();
    }
}
