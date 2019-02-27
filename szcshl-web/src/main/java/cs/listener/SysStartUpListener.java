package cs.listener;

import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.sysprop.BusinessProperties;
import cs.common.utils.Validate;
import cs.domain.sys.Quartz;
import cs.service.meeting.MeetingRoomService;
import cs.service.sys.OrgDeptService;
import cs.service.sys.QuartzService;
import cs.service.sys.UserService;
import cs.spring.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

import static cs.common.cache.CacheConstant.IP_CACHE;
import static cs.common.constants.SysConstants.SYS_BUSI_PROP_BEAN;

/**
 * 系统启动完成监听
 * Created by ldm on 2017/9/5 0005.
 */
@Component
public class SysStartUpListener implements ApplicationListener {
    private static Logger logger = Logger.getLogger(SysStartUpListener.class);

    @Autowired
    private QuartzService quartzService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgDeptService orgDeptService;
    @Autowired
    private MeetingRoomService meetingRoomService;

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
                    //3、会议室放到缓存
                    meetingRoomService.fleshMeetingCache();
                    //3、启动默认启动的定时器
                    List<Quartz> quartzList = quartzService.findDefaultQuartz();
                    if(Validate.isList(quartzList)){
                        for (Quartz quartz : quartzList) {
                            quartzService.quartzExecute(quartz.getId());
                        }
                        logger.info("\n==                 启动定时器成功！             ==");
                    }
                    ICache cache = CacheManager.getCache();
                    InetAddress myip= InetAddress.getLocalHost();
                    cache.put(IP_CACHE,myip.getHostAddress());
                } catch (Exception e) {
                    logger.info("\n==启动定执行方法异常："+e.getMessage());
                    e.printStackTrace();
                }
                logger.info("\n" +
                        "=====================================================" +
                        "\n==                 系统启动成功             ==" +
                        "\n=====================================================");
            }


        }
    }
}
