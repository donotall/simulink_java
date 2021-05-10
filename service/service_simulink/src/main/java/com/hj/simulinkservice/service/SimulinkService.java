package com.hj.simulinkservice.service;

import com.hj.simulinkservice.vo.ModelParams;
import com.hj.simulinkservice.vo.TargetSettingVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface SimulinkService {
    TargetSettingVo GetSetting(int port);

    String UseSimulink(MultipartFile file,int port);

    int Conection(String connectId,String port);

    boolean ChangeTargetSetting(int port, String fileName,TargetSettingVo targetSettingVo);

    boolean RestoreSetting(int port, String fileName);

    boolean XpcLoad(int port, String fileName);

    void ModelOpen(String fileName);

    boolean CloseConnection(int port);

    boolean ModelStart(int port);

    Boolean stopModel(int port);

    ModelParams[] getModelParams(int port) throws UnsupportedEncodingException;

    Boolean updateModelParams(int port, String fileName, ModelParams[] modelParams);
}
