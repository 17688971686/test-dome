package cs.listener;

import cs.common.utils.QuartzManager;
import cs.domain.sys.Quartz;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.sys.OrgDeptService;
import cs.service.sys.QuartzService;
import cs.service.sys.UserService;
import cs.service.sys.UserServiceImpl;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统启动完成监听
 * Created by ldm on 2017/9/5 0005.
 */
@Component
public class SysStartUpListener implements ApplicationListener {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrgDeptService orgDeptService;

    private boolean isInit = false;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent){
            if(!isInit){
                isInit = true;
                try {
                    //1、添加用户缓存
                    userService.fleshPostUserCache();
                    //2、添加部门缓存
                    orgDeptService.fleshOrgDeptCache();
                    //3、启动默认启动的定时器
                    List<Quartz> quartzList = quartzService.findDefaultQuartz();
                    for (Quartz quartz : quartzList) {
                        SchedulerFactory schedulderFactory = new StdSchedulerFactory();
                        String cls = quartz.getClassName();
                        QuartzManager.addJob(schedulderFactory.getScheduler(), quartz.getQuartzName(),
                                Class.forName(cls), quartz.getCronExpression());
                        quartzService.changeCurState(quartz.getId(), "9");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("\n" +
                        "=====================================================" +
                        "\n==                 深圳评审中心管理系统             ==" +
                        "\n=====================================================");
            }


        }
    }
}
