package cs.common.cache.executor;

import cs.common.cache.SmsAbstractWork;
import cs.common.cache.queue.OrderedQueuePool;
import cs.common.cache.queue.TasksQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 有序的队列线程池
 *
 * @author
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor {

    protected static Logger log = LoggerFactory.getLogger(OrderedQueuePoolExecutor.class);

    private OrderedQueuePool<String, SmsAbstractWork> pool = new OrderedQueuePool<>();

    private String name;

    private int maxQueueSize;

    public OrderedQueuePoolExecutor(String name, int corePoolSize,
                                    int maxQueueSize) {
        super(corePoolSize, 2 * corePoolSize, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        this.name = name;
        this.maxQueueSize = maxQueueSize;
    }

    public OrderedQueuePoolExecutor(int corePoolSize) {
        this("queue-pool", corePoolSize, 1000);
    }

    /**
     * 增加执行任务
     *
     * @param key
     * @param task
     * @return
     */
    public boolean addTask(String key, SmsAbstractWork task) {
        TasksQueue<SmsAbstractWork> queue = pool.getTasksQueue(key);
        boolean run = false;
        boolean result;
        synchronized (queue) {
            if (maxQueueSize > 0) {
                if (queue.size() > maxQueueSize) {
                    log.error("队列" + name + "(" + key + ")" + "抛弃指令!");
                    queue.clear();
                }
            }
            result = queue.add(task);
            if (result) {
                task.setTasksQueue(queue);
                {
                    if (queue.isProcessingCompleted()) {
                        queue.setProcessingCompleted(false);
                        run = true;
                    }
                }
            } else {
                log.error("队列添加任务失败");
            }
        }
        if (run) {
            execute(queue.poll());
        }
        return result;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        SmsAbstractWork work = (SmsAbstractWork) r;
        TasksQueue<SmsAbstractWork> queue = work.getTasksQueue();
        if (queue != null) {
            SmsAbstractWork afterWork;
            synchronized (queue) {
                afterWork = queue.poll();
                if (afterWork == null) {
                    queue.setProcessingCompleted(true);
                }
            }
            if (afterWork != null) {
                execute(afterWork);
            }
        } else {
            log.error("执行队列为空");
        }
    }

}
