package cs.common.cache;


import cs.common.cache.queue.TasksQueue;

public abstract class SmsAbstractWork implements Runnable {

    private TasksQueue<SmsAbstractWork> tasksQueue;

    public TasksQueue<SmsAbstractWork> getTasksQueue() {
        return tasksQueue;
    }

    public void setTasksQueue(TasksQueue<SmsAbstractWork> tasksQueue) {
        this.tasksQueue = tasksQueue;
    }
}
