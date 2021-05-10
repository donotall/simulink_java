package com.hj.manageservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.entity.XpcAttr;
import com.hj.manageservice.entity.excel.XpcAttrData;
import com.hj.manageservice.service.XpcAttrService;
import com.hj.servicebase.exceptionhandler.SLException;
import org.springframework.beans.BeanUtils;

public class XpcAttrListener extends AnalysisEventListener<XpcAttrData> {
    private XpcAttrService xpcAttrService;
    public XpcAttrListener(){}
    public  XpcAttrListener(XpcAttrService xpcAttrService){
        this.xpcAttrService = xpcAttrService;
    }

    @Override
    public void invoke(XpcAttrData xpcAttrData, AnalysisContext analysisContext) {
        if(xpcAttrData ==null) {
            throw new SLException(20001, "文件数据为空");
        }
        Integer integer = this.existByIpPort(xpcAttrService, xpcAttrData.getIp(), xpcAttrData.getPort());
        if(integer==0){
            XpcAttr xpcAttr = new XpcAttr();
            xpcAttr.setIp(xpcAttrData.getIp());
            xpcAttr.setPort(xpcAttrData.getPort());
            xpcAttr.setIsUse(false);
            xpcAttrService.save(xpcAttr);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    public Integer existByIpPort(XpcAttrService xpcAttrService,String ip, Integer port) {
        QueryWrapper<XpcAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("ip",ip);
        wrapper.eq("port",port);
        return xpcAttrService.count(wrapper);
    }
}
