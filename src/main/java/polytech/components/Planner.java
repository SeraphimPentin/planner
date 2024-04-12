package polytech.components;


import polytech.Properties;
import polytech.domain.Event;
import polytech.domain.Task;
import polytech.domain.planner.FJPTask;
import polytech.domain.planner.TaskActable;
import polytech.enums.Priority;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private final Semaphore readyTasksSemaphore = new Semaphore(Properties.READY_TASKS_LIMIT);
    private final ExecutorService eventPool = Executors.newSingleThreadExecutor();

    private final Map<Integer, Queue<FJPTask>> suspendedTasks = new HashMap<>();

    {
        suspendedTasks.put(Priority.HIGH.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.MIDDLE.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOW.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOWEST.getValue(), new ConcurrentLinkedQueue<>());
    }

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

    private void scheduleTaskExecution(Task task) {
        activateIfReadyTasksAmountAllows(task);

        int priority = task.priority().getValue();

        Queue<FJPTask> highPrioritized = suspendedTasks.get(priority + 1);
        Queue<FJPTask> lowPrioritized = suspendedTasks.get(priority);

        FJPTask fjpTask = new FJPTask(task, highPrioritized, readyTasksSemaphore, this::handleEventTask);
        lowPrioritized.add(fjpTask);
        forkJoinPool.submit(fjpTask);
    }

    private void handleEventTask(Task task) {
        Iterator<Runnable> iterator = task.iterations().iterator();
        Event event = (Event) iterator.next();
        iterator.remove();
        CompletableFuture.runAsync(event, eventPool).thenRun(() -> {
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

