package com.hj.simulinkservice.service.impl;

import com.hj.commonutils.SimulinkUtils;
import com.hj.servicebase.exceptionhandler.SLException;
import com.hj.simulinkservice.service.SimulinkDataService;
import com.hj.simulinkservice.vo.ScopeVo;
import org.springframework.stereotype.Service;

@Service
public class SimulinkDataServiceImpl implements SimulinkDataService {
    @Override
    public boolean XPCAddScope(ScopeVo scopeVo) {
        int [] data = new int[20];
        SimulinkUtils.getInstance.xPCGetScopes(scopeVo.getPort(),data);
        SimulinkUtils.getInstance.xPCAddScope(scopeVo.getPort(),1,1);
        SimulinkUtils.getInstance.xPCGetScope(scopeVo.getPort(),1);
        return false;
    }
    // 获取数据
    @Override
    public double[] GetDataFromXpc(int port){
        // 获取连接总数
        int  numSignals= SimulinkUtils.getInstance.xPCGetNumSignals(port);
        int[] data = new int[numSignals];
        double sigVal[]= new double[numSignals];
        for (int i = 0; i <numSignals ; i++) {
            data[i]=i;
        }
        int i = SimulinkUtils.getInstance.xPCGetSignals(port, numSignals, data, sigVal);
        if(i<0){
            throw new SLException(20001,"获取数据失败！");
        }
        return sigVal;
    }
}
