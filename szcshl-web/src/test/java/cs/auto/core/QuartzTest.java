package cs.auto.core;

public class QuartzTest {

    public void quartz() throws ClassNotFoundException {
        try {
            /*SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
            Scheduler sche = gSchedulerFactory.getScheduler();
            String job_name = "动态任务调度";
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            String clazz = "cs.auto.HelloWorld";
            //String cron = "*//*10 * * * * ?";  //使用Class.forName动态的创建
            //QuartzManager.addJob(sche, job_name, Class.forName(clazz), cron);

            Thread.sleep(3000);
            System.out.println("【修改时间】开始(每2秒输出一次)...");
            QuartzManager.modifyJobTime(sche, job_name, "10/2 * * * * ?");
            Thread.sleep(4000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(sche, job_name);
            System.out.println("【移除定时】成功");

            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
            //QuartzManager.addJob(sche, job_name, Class.forName(clazz), "*//*10 * * * * ?");
            Thread.sleep(30000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(sche, job_name);
            System.out.println("【移除定时】成功");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
