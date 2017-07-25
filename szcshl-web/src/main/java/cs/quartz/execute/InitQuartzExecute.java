package cs.quartz.execute;

import cs.common.utils.QuartzManager;
import cs.domain.sys.Quartz;
import cs.service.sys.QuartzService;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InitQuartzExecute {

    @Autowired
    private QuartzService quartzService;

    public void initQuartzExecute() {
        List<Quartz> quartzList = quartzService.findDefaultQuartz();
        try {
            for (Quartz quartz : quartzList) {
                SchedulerFactory schedulderFactory = new StdSchedulerFactory();
                Scheduler sched;
                sched = schedulderFactory.getScheduler();
                String cls = quartz.getClassName();
                String jobName = quartz.getQuartzName();
                String time = quartz.getCronExpression();
                QuartzManager.addJob(sched, jobName, Class.forName(cls), time);
                quartzService.changeCurState(quartz.getId(), "9");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
