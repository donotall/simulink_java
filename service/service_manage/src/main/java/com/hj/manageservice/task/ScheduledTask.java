package com.hj.manageservice.task;

import com.hj.manageservice.utils.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        String dayA = DateUtil.formatDate(new Date());
        String dayB = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
    }
}
