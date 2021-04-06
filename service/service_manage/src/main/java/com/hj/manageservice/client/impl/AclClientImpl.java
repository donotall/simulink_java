package com.hj.manageservice.client.impl;

import com.hj.commonutils.R;
import com.hj.commonutils.vo.UserVos;
import com.hj.manageservice.client.AclClient;
import com.hj.servicebase.exceptionhandler.SLException;
import org.springframework.stereotype.Component;

@Component
public class AclClientImpl implements AclClient {
    @Override
    public UserVos getUserList() {
        throw new RuntimeException();
    }
}
