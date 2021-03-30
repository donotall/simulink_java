package com.hj.simulinkservice.service;

import com.hj.simulinkservice.vo.ScopeVo;

public interface SimulinkDataService {
    boolean XPCAddScope(ScopeVo scopeVo);

    double[] GetDataFromXpc(int port);
}
