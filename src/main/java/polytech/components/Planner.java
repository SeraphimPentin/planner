package polytech.components;


import polytech.domain.Event;
import polytech.domain.Task;
import polytech.domain.planner.FJPTask;
import polytech.domain.planner.TaskActable;
import polytech.enums.Priority;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;

public class Planner implements TaskActable {
    public static final int READY_TASKS_LIMIT = 10;

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(1);
    private final ExecutorService eventPool = Executors.newSingleThreadExecutor();
    private final Semaphore readyTasksSemaphore = new Semaphore(READY_TASKS_LIMIT);

    private final Map<Integer, Queue<FJPTask>> suspendedTasks = new HashMap<>();

    {
        suspendedTasks.put(Priority.HIGH.getValue(), new ConcurrentLinkedQueue<>()); //middle задачи ожидающие выполнения high задач
        suspendedTasks.put(Priority.MIDDLE.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOW.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOWEST.getValue(), new ConcurrentLinkedQueue<>()); //FIXME remove
    }

    public void addTask(Task task) {
        activateIfCan(task);

        int priority = task.priority().getValue();

        Queue<FJPTask> highPrioritized = suspendedTasks.get(priority + 1);
        Queue<FJPTask> lowPrioritized = suspendedTasks.get(priority);

        FJPTask fjpTask = new FJPTask(task, highPrioritized, readyTasksSemaphore, this::handleEventTask);
        lowPrioritized.add(fjpTask);
        forkJoinPool.submit(fjpTask);
    }

    private void activateIfCan(Task task) {
        try {
            readyTasksSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        activate(task);
    }

    //FIXME Достаточно стрёмный контракт получается. Этот метод, знает что первая итерация у таски - это ивент
    private void handleEventTask(Task task) {
        Iterator<Runnable> iterator = task.iterations().iterator();
        Event event = (Event) iterator.next();
        iterator.remove();
        CompletableFuture.runAsync(event, eventPool).thenRun(() -> {
            release(task);
            this.addTask(task);
        });
    }
}

