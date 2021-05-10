package com.hj.simulinkservice.service.impl;

import com.hj.commonutils.SaveFile;
import com.hj.commonutils.SimulinkUtils;
import com.hj.servicebase.exceptionhandler.SLException;
import com.hj.simulinkservice.client.AttrClient;
import com.hj.simulinkservice.service.SimulinkService;
import com.hj.simulinkservice.vo.ModelParams;
import com.hj.simulinkservice.vo.TargetSettingVo;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SimulinkServiceImpl implements SimulinkService {
    /**
     * 得到target的设置
     * @param port
     * @return
     */
    @Override
    public TargetSettingVo GetSetting(int port) {
        TargetSettingVo settingVo = new TargetSettingVo();
        settingVo.setTimeOut(SimulinkUtils.getInstance.xPCGetLoadTimeOut(port));
        settingVo.setTs(SimulinkUtils.getInstance.xPCGetSampleTime(port));
        settingVo.setTFinal(SimulinkUtils.getInstance.xPCGetStopTime(port));
        return settingVo;
    }

    /**
     *将模型保存到本地，并且编译为可执行文件
     * @param file
     * @param port
     * @return
     */
    @Override
    public String UseSimulink(MultipartFile file,int port) {
            String originalFilename = file.getOriginalFilename();
            String filename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //保存到本地
            String dir = System.getProperty("user.dir")  +"\\service\\service_simulink\\target\\model";
            try {
                SaveFile.savePic(file.getInputStream(), originalFilename,dir);
            } catch (IOException e) {
                throw new SLException(20001,"文件保存失败！");
            }
            try {
                MatlabEngine eng = MatlabEngine.startMatlab();
                //进入文件所在目录
                eng.feval("cd",dir);

                //编译
                String name1="rtwbuild('"+ filename+"')";
                Future<Void> future = eng.evalAsync(name1);
                while (!future.isDone()){
                    System.out.println("build Simulink model...");
                    Thread.sleep(10000);
                }
            } catch (EngineException | InterruptedException e) {
                throw new SLException(20001,"编译文件失败！");
            } catch (ExecutionException e) {
                throw new SLException(20001,"进入文件夹失败！");
            }
        return file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
    }

    /**
     * 连接target
     * @param connectId
     * @param port
     * @return
     */
    @Override
    public int Conection(String connectId,String port) {
        int ipPort = SimulinkUtils.getInstance.xPCOpenTcpIpPort(connectId, port);
        int ping = SimulinkUtils.getInstance.xPCTargetPing(ipPort);
        if(ping == 0){
            throw new SLException(20001,"目标及连接失败！");
        }
        return ipPort;
    }

    /**
     *
     *修改xpc的参数
     * @param port
     * @param fileName
     * @param targetSettingVo
     * @return
     */
    @Override
    public boolean ChangeTargetSetting(int port,String fileName, TargetSettingVo targetSettingVo) {
        String message = "";
        SimulinkUtils.getInstance.xPCSetSampleTime(port,targetSettingVo.getTs());
        SimulinkUtils.getInstance.xPCSetLoadTimeOut(port,targetSettingVo.getTimeOut());
        SimulinkUtils.getInstance.xPCSetStopTime(port,targetSettingVo.getTFinal());
        SimulinkUtils.getInstance.xPCSaveParamSet(port,fileName);
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        if(lastError !=0){
            throw new SLException(20001,SimulinkUtils.getInstance.xPCErrorMsg(lastError,message));
        }else {
            return true;
        }
    }

    @Override
    public boolean RestoreSetting(int port, String fileName) {
        SimulinkUtils.getInstance.xPCLoadParamSet(port,fileName);
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        return lastError==0;
    }

    /**
     * 将模型加载到目标机
     * @param port
     * @param fileName
     */
    @Override
    public boolean XpcLoad(int port, String fileName) {
        String dir = System.getProperty("user.dir") + "\\service\\service_simulink\\target\\model";
        SimulinkUtils.getInstance.xPCOpenConnection(port);
        SimulinkUtils.getInstance.xPCLoadApp(port,dir,fileName);
        //SimulinkUtils.getInstance.xPCStartApp(port);
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        if(lastError !=0){
            String message ="";
            throw new SLException(20001,SimulinkUtils.getInstance.xPCErrorMsg(lastError,message)+"---模型加载失败！");
        }else {
            return true;
        }
    }

    /**
     * 修改模型
     * @param fileName
     */
    @SneakyThrows
    @Override
    public void ModelOpen(String fileName) {
        MatlabEngine engine = MatlabEngine.startMatlab();
        System.out.println(System.getProperty("user.dir"));
        String dir = System.getProperty("user.dir") + "\\service\\service_simulink\\target\\model";
        engine.feval("cd",dir);
        String open = "uiopen('" + fileName + "', 1)";
        Future<Void> voidFuture = engine.evalAsync(open);
        engine.close();
    }

    /**
     * 关闭连接
     * @param port
     * @return
     */

    @Override
    public boolean CloseConnection(int port) {
        //卸载模型
        SimulinkUtils.getInstance.xPCUnloadApp(port);
        //关闭目标机连接
        SimulinkUtils.getInstance.xPCCloseConnection(port);
        // 关闭目标机tcpip
        SimulinkUtils.getInstance.xPCClosePort(port);
       return SimulinkUtils.getInstance.xPCTargetPing(port)==0;
    }

    @Override
    public boolean ModelStart(int port) {
        SimulinkUtils.getInstance.xPCStartApp(port);
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        if(lastError !=0){
            String message ="";
            throw new SLException(20001,SimulinkUtils.getInstance.xPCErrorMsg(lastError,message)+"---运行模型失败！");
        }else {
            return true;
        }
    }

    @Override
    public Boolean stopModel(int port) {
        SimulinkUtils.getInstance.xPCStopApp(port);
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        if(lastError !=0){
            String message ="";
            throw new SLException(20001,SimulinkUtils.getInstance.xPCErrorMsg(lastError,message)+"---运行模型失败！");
        }else {
            return true;
        }
    }

    @Override
    public ModelParams[] getModelParams(int port) throws UnsupportedEncodingException {
        System.setProperty("jna.encoding","GBK");
        int numParams = SimulinkUtils.getInstance.xPCGetNumParams(port);
        ModelParams[] modelParamsList = new ModelParams[numParams];
        for(int i=0;i<numParams;i++){
            int[] nums = new int[2];
            ModelParams modelParams = new ModelParams();
            Pointer n1 = new Memory(128);
            Pointer n2 = new Memory(128);
            SimulinkUtils.getInstance.xPCGetParamName(port,i,n1,n2);
            modelParams.setBlockName(param(n1));
            modelParams.setParamName(param(n2));
            SimulinkUtils.getInstance.xPCGetParamDims(port,i,nums);
            double []paramValue = new double[nums[0]*nums[1]];
            SimulinkUtils.getInstance.xPCGetParam(port,i,paramValue);
            modelParams.setVal(paramValue);
            modelParamsList[i] = modelParams;
        }
        return modelParamsList;
    }

    @Override
    public Boolean updateModelParams(int port, String fileName, ModelParams[] modelParams) {
        for(int i=0;i<modelParams.length;i++){
            SimulinkUtils.getInstance.xPCSetParam(port,i,modelParams[i].getVal());
        }
        SimulinkUtils.getInstance.xPCSaveParamSet(port,fileName);
        //判断是否出错
        int lastError = SimulinkUtils.getInstance.xPCGetLastError();
        if(lastError !=0){
            String message ="";
            throw new SLException(20001,SimulinkUtils.getInstance.xPCErrorMsg(lastError,message)+"---运行模型失败！");
        }else {
            return true;
        }
    }

    private String param(Pointer pointer) throws UnsupportedEncodingException {
        String paramName = new String(pointer.getByteArray(0, 128),"utf-8");
        String result="";
        for (int j=0;j<paramName.length();j++){
            char c = paramName.charAt(j);
            if(('a'<=c&&c<='z')||('A'<=c&&c<='Z'))
                result +=c;
            else break;
        }

        long peer = Pointer.nativeValue(pointer);
        Native.free(peer);//手动释放内存
        Pointer.nativeValue(pointer, 0);
        return  result;
    }
}
