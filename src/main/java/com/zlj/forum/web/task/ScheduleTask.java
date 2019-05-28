package com.zlj.forum.web.task;

import com.zlj.forum.recom.RecomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-28 12:38
 */

@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    private RecomService recomService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void recommend() {

        log.info("-------------------------------每日推荐定时任务开启-----------------------------------");
        recomService.executeJobForAllUsers(true, true, true);
        log.info("-------------------------------每日推荐定时任务结束-----------------------------------");
    }
}
