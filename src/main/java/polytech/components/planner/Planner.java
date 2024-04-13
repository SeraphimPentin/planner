package polytech.components.planner;


import polytech.Properties;
import polytech.domain.Event;
import polytech.domain.Task;
import polytech.domain.planner.FJPTask;
import polytech.domain.planner.TaskActable;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Planner extends Thread implements TaskActable {

    private static final Logger log = LoggerFactory.getLogger(Planner.class);

    private final ForkJoinPool forkJoinPool;
    private final BlockingQueue<Task> taskQueue;
    private final TasksQueuesDispatcher tasksQueuesDispatcher = new TasksQueuesDispatcher();

    private final Semaphore readyTasksSemaphore = new Semaphore(Properties.READY_TASKS_LIMIT);
    private final ExecutorService eventPool = Executors.newSingleThreadExecutor();

    public Planner(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
        this.forkJoinPool = new ForkJoinPool(1);
    }

    public Planner(BlockingQueue<Task> taskQueue, ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            Task task;
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            scheduleTaskExecution(task);
        }
    }

    void scheduleTaskExecution(Task task) {
        activateIfReadyTasksAmountAllows(task);


        Iterator<Queue<FJPTask>> prioritizedTasksQueue = tasksQueuesDispatcher.getPrioritizedTasksQueues(task);
        Queue<FJPTask> tasksQueueWithSamePriority = tasksQueuesDispatcher.getSamePriorityTasksQueue(task);

        FJPTask fjpTask = new FJPTask(task, prioritizedTasksQueue, readyTasksSemaphore, this::handleEventTask);
        tasksQueueWithSamePriority.add(fjpTask);
        forkJoinPool.submit(fjpTask);
    }

    //FIXME Достаточно стрёмный контракт получается. Этот метод, знает что первая итерация у таски - это ивент
    private void handleEventTask(Task task) {
        Iterator<Runnable> iterator = task.iterations().iterator();
        Event event = (Event) iterator.next();
        iterator.remove();
        CompletableFuture.runAsync(event, eventPool).thenRun(() -> {
            //release(task);
            this.scheduleTaskExecution(task);
        });
    }

    private void activateIfReadyTasksAmountAllows(Task task) {
        log.info(String.format(
                "Activating task %s %s. Ready tasks count: %d/%d",
                task.priority(), task.uuid(), countReadyTasks(), Properties.READY_TASKS_LIMIT
        ));
        try {
            readyTasksSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        activate(task);
    }

    private int countReadyTasks() {
        return Properties.READY_TASKS_LIMIT - readyTasksSemaphore.availablePermits();
    }

}

