package com.hj.commonutils;


import com.hj.commonutils.vo.Scopedata;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 调用dll,xpcapi的接口
 */
public interface SimulinkUtils extends Library {
   SimulinkUtils getInstance = (SimulinkUtils) Native.loadLibrary("xpcapi", SimulinkUtils.class);

   public int xPCTargetPing(int port);

   public void xPCOpenConnection(int port);

   public void xPCCloseConnection(int port);

   public String xPCGetAPIVersion();

   public int xPCOpenTcpIpPort(String ipAddress, String ipPort);

   public void xPCClosePort(int port);

   public void xPCReboot(int port);

   public int xPCRegisterTarget(int commType, String ipAddress, String ipPort, int comPort, int baudRate);

   public void xPCSetSampleTime(int port, double ts);

   public double xPCGetSampleTime(int port);

   public void xPCSetStopTime(int port, double tfinal);

   public double xPCGetStopTime(int port);

   public void xPCSaveParamSet(int port, String filename);

   public void xPCLoadParamSet(int port, String filename);

   public void xPCStartApp(int port);

   public void xPCStopApp(int port);

   public void xPCSetLoadTimeOut(int port, int timeOut);

   public int xPCGetLoadTimeOut(int port);

   public String xPCGetAppName(int port, String model_name);

   public void xPCLoadApp(int port, String pathstr, String filename);

   public int xPCGetNumSignals(int port);

   public void xPCGetScopeList(int port, int data[]);

   public Scopedata.scopedata xPCGetScope(int port, int scNum);

   public void xPCGetScopes(int port, int data[]);

   public int xPCGetNumScopes(int port);

   public int xPCGetLastError();

   public String xPCErrorMsg(int error_number, String error_message);

   public void xPCUnloadApp(int port);

   public void xPCAddScope(int port, int scType, int scNum);

   public int xPCGetSigIdxfromLabel(int port, String sigLabel, int[] sigIds);

   public String xPCGetSignalLabel(int port, int sigIdx, String sigLabel);

   public double xPCGetSignal(int port, int sigNum);

   public int xPCGetSignals(int port, int numSignals, int[] signals, double[] values);

   public String xPCGetSignalName(int port, int sigIdx, String sigName);

   public void xPCScGetSignalList(int port, int scNum, int[] data);

   public double xPCGetExecTime(int port);

   public int xPCGetNumParams(int port);

   public void xPCGetParamName(int port, int paramIdx, Pointer blockName, Pointer paramName);

   public void xPCGetParamDims(int port, int paramIndex, int[] dimension);

   public void xPCGetParam(int port, int paramIndex, double[] paramValue);

   public void xPCSetParam(int port, int paramIdx, double[] paramValue);
}
