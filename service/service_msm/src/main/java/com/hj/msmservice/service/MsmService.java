package com.hj.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(String phone, String templateCode, Map<String, Object> params);
}
