package org.jetlinks.community.standalone.task;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.standalone.goview.v2.model.DeviceInstancesTemplate;
import org.jetlinks.community.standalone.goview.v2.service.DeviceInstancesTemplateService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class QuartzTast {

    @Resource
    private DeviceInstancesTemplateService deviceInstancesTemplateService;

    @PostConstruct
    public void scheduleTasks() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        List<DeviceInstancesTemplate> templateList = deviceInstancesTemplateService.list();
        if(CollectionUtil.isNotEmpty(templateList)){
            for (DeviceInstancesTemplate task : templateList) {
                JobDetail jobDetail = JobBuilder.newJob(TaskJob.class)
                                                .withIdentity(task.getId())
                                                .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                                                .withIdentity(task.getId() + "Trigger")
                                                .withSchedule(CronScheduleBuilder.cronSchedule(task.getSamplingFrequency()))
                                                .build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        }
    }
}
