package com.hj.test;


import com.hj.commonutils.SimulinkUtils;
import com.hj.simulinkservice.vo.TargetSettingVo;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatlabTest {
    static ExecutorService executorService = Executors.newFixedThreadPool(20);
    @Test
    public void test1() throws InterruptedException, ExecutionException {
        MatlabEngine eng = MatlabEngine.startMatlab();
        Future<Void>  fLoad = eng.evalAsync("load_system('vdp')");
        while (!fLoad.isDone()){
            System.out.println("Loading Simulink model...");
            Thread.sleep(10000);
        }
        Future<Void> fSim = eng.evalAsync("simOut = sim('vdp','SaveOutput'," +
                "'on','OutputSaveName','yOut'," +
                "'SaveTime','on','TimeSaveName','tOut');");
        while (!fSim.isDone()) {
            System.out.println("Running Simulation...");
            Thread.sleep(10000);
        }
        // Get simulation data
        eng.eval("y = simOut.get('yOut');");
        eng.eval("t = simOut.get('tOut');");
        // Graph results and create image file
        eng.eval("plot(t,y)");
        eng.eval("print('vdpPlot','-djpeg')");
        // Return results to Java
        double[][] y = eng.getVariable("y");
        double[] t = eng.getVariable("t");
        // Display results
        System.out.println("Simulation result " + Arrays.deepToString(y));
        System.out.println("Time vector " + Arrays.toString(t));
        eng.close();
    }
    @Test
    public void test2() throws InterruptedException, ExecutionException {
          MatlabEngine eng = MatlabEngine.startMatlab();
        eng.evalAsync("[X, Y] = meshgrid(-2:0.2:2);");
        eng.evalAsync("Z = X .* exp(-X.^2 - Y.^2);");
        Object[] Z = eng.getVariable("Z");
        System.out.println(Arrays.toString(Z));
        eng.close();
    }
    @Test
    public void test3() throws InterruptedException, ExecutionException {
        /*MatlabEngine eng = MatlabEngine.startMatlab();
        eng.evalAsync("tg = slrt('TargetPC1');");
        // Object[] variable = eng.getVariable("tg");
        //System.out.println(Arrays.toString(variable));
        //Object[] tg = eng.getVariable("Target");
        //System.out.println(Arrays.toString(tg));
        eng.close();*/
        MatlabEngine engine = MatlabEngine.startMatlab();
        String fileName = "untitled";
        System.out.println(System.getProperty("user.dir"));
        String dir = System.getProperty("user.dir") + "\\target\\model";
        engine.feval("cd",dir);
        String open = "uiopen('" + fileName + "', 1)";
        Future<Void> voidFuture = engine.evalAsync(open);
        engine.close();
    }
    @Test
    public  void test4(){
        //System.out.println(SimulinkUtils.getInstance.xPCInitAPI());
        System.out.println(SimulinkUtils.getInstance.xPCGetAPIVersion());
        int ipPort = SimulinkUtils.getInstance.xPCOpenTcpIpPort("192.168.7.10", "22222");
        SimulinkUtils.getInstance.xPCOpenConnection(ipPort);
        System.out.println(ipPort);
        System.out.println(SimulinkUtils.getInstance.xPCTargetPing(ipPort));
    }
    @Test
    public void  test5(){
        String message[]= new String[20];
                int port = SimulinkUtils.getInstance.xPCOpenTcpIpPort("192.168.7.10","22222");
        System.out.println(port);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message[2]));

        System.out.println(SimulinkUtils.getInstance.xPCTargetPing(port));
                String fileName = "";
        String s = SimulinkUtils.getInstance.xPCGetAppName(port, fileName);
        System.out.println(s);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message[0]));

        String dir = System.getProperty("user.dir") + "\\target\\model";
        System.out.println(dir);
                SimulinkUtils.getInstance.xPCOpenConnection(port);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message[3]));

        SimulinkUtils.getInstance.xPCLoadApp(port,"F:\\DataWorkeBase\\simulink\\service\\service_simulink\\target\\model","test");
        int i1 = SimulinkUtils.getInstance.xPCGetLastError();

        System.out.println(i1);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(i1,message[1]));
                SimulinkUtils.getInstance.xPCStartApp(port);
        int i = SimulinkUtils.getInstance.xPCGetNumScopes(port);
        System.out.println(i);

        int []data = new int[10];

        SimulinkUtils.getInstance.xPCGetScopes(port,data);

        /*Scopedata.scopedata scopedata = SimulinkUtils.getInstance.xPCGetScope(port, 0);
        System.out.println(scopedata.toString());*/
    }
    @Test
    public void test6() throws InterruptedException, EngineException {
        MatlabEngine eng = MatlabEngine.startMatlab();
        Future<Void>  fLoad = eng.evalAsync("load_system('untitled')");
        while (!fLoad.isDone()){
            System.out.println("Loading Simulink model...");
            Thread.sleep(10000);
        }
    }
    @Test
    public void test7(){
        int port = SimulinkUtils.getInstance.xPCOpenTcpIpPort("192.168.7.10","22222");
        System.out.println(port);
        //卸载模型
        SimulinkUtils.getInstance.xPCUnloadApp(0);
        //关闭目标机连接
        SimulinkUtils.getInstance.xPCCloseConnection(0);
        // 关闭目标机tcpip
        SimulinkUtils.getInstance.xPCClosePort(0);
        System.out.println(SimulinkUtils.getInstance.xPCGetLastError());
        System.out.println(SimulinkUtils.getInstance.xPCTargetPing(0));
    }
    @Test
    public void test8() throws InterruptedException {

       int port = SimulinkUtils.getInstance.xPCOpenTcpIpPort("192.168.7.10","22222");
        TargetSettingVo settingVo = new TargetSettingVo();
        SimulinkUtils.getInstance.xPCLoadApp(port,"F:\\DataWorkeBase\\simulink\\service\\service_simulink\\target\\model","xpctank");
        settingVo.setTimeOut(SimulinkUtils.getInstance.xPCGetLoadTimeOut(port));
        settingVo.setTs(SimulinkUtils.getInstance.xPCGetSampleTime(port));
        settingVo.setTFinal(SimulinkUtils.getInstance.xPCGetStopTime(port));
        System.out.println(settingVo.toString());
        SimulinkUtils.getInstance.xPCSetSampleTime(port,0.1);
        SimulinkUtils.getInstance.xPCSetLoadTimeOut(port,settingVo.getTimeOut());
        SimulinkUtils.getInstance.xPCSetStopTime(port,100);
        settingVo.setTs(SimulinkUtils.getInstance.xPCGetSampleTime(port));
        String message ="";
        int scopes = SimulinkUtils.getInstance.xPCGetNumScopes(port);
        System.out.println(scopes);
        SimulinkUtils.getInstance.xPCStartApp(port);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message));
        System.out.println(settingVo.toString());
        int []scopeList = new int[scopes];
        SimulinkUtils.getInstance.xPCGetScopes(port,scopeList);
        Thread.sleep(200);
        int [] data = new int[10];
        SimulinkUtils.getInstance.xPCScGetSignalList(port,scopeList[0],data);
        for (int i: data) {
            System.out.println(i);
        }
        //t(port);

    }
    @Test
    public void test89() throws InterruptedException {
        t(0);
    }
    public void t( int port) throws InterruptedException {
        String message="";
        // 得到总数
        int num = SimulinkUtils.getInstance.xPCGetNumSignals(port);
        System.out.println(num);
        String sigLabel ="";
        int []data = {0,1,2,3,4,5,6,7,8};
        double sigVal[]= new double[num];
        String s1=SimulinkUtils.getInstance.xPCGetSignalName(port,1,sigLabel);
        // 获取名字
        System.out.println(s1);
        System.out.println(sigLabel);
        Thread.sleep(1);
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message));
        int j =SimulinkUtils.getInstance.xPCGetSignals(port, 9, data, sigVal);
        System.out.println(j);
        for (int i = 0; i <sigVal.length ; i++) {
            System.out.print(sigVal[i]+"  ");
        }
        System.out.println();
        //Thread.sleep(1000);
        SimulinkUtils.getInstance.xPCGetSignals(port, 9, data, sigVal);
        for (int i = 0; i <sigVal.length ; i++) {
            System.out.print(sigVal[i]+"  ");
        }
        // System.out.println(SimulinkUtils.getInstance.xPCGetSignals(port,1));
        /**/
        // System.out.println(SimulinkUtils.getInstance.xPCGetSignalLabel(port,,""));
        System.out.println(SimulinkUtils.getInstance.xPCErrorMsg(SimulinkUtils.getInstance.xPCGetLastError(),message));
    }
}
