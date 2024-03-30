package polytech.components;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.Event;
import polytech.domain.planner.FJPTask;
import polytech.domain.Task;
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

public class Planner {
    private static final Logger logger = LoggerFactory.getLogger(Planner.class);
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(1);
    private final ExecutorService eventPool = Executors.newSingleThreadExecutor();

    private final Map<Integer, Queue<FJPTask>> suspendedTasks = new HashMap<>();

    {
        suspendedTasks.put(Priority.HIGH.getValue(), new ConcurrentLinkedQueue<>()); //middle задачи ожидающие выполнения high задач
        suspendedTasks.put(Priority.MIDDLE.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOW.getValue(), new ConcurrentLinkedQueue<>());
        suspendedTasks.put(Priority.LOWEST.getValue(), new ConcurrentLinkedQueue<>()); //FIXME remove
    }

    public void addTask(Task task) {
        int priority = task.priority().getValue();

        Queue<FJPTask> highPrioritized = suspendedTasks.get(priority + 1);
        Queue<FJPTask> lowPrioritized = suspendedTasks.get(priority);

        FJPTask fjpTask = new FJPTask(task, highPrioritized, this::handleEventTask);
        lowPrioritized.add(fjpTask);
        forkJoinPool.submit(fjpTask);
    }

    //FIXME Достаточно стрёмный контракт получается. Этот метод, знает что первая итерация у таски - это ивент
    private void handleEventTask(Task task) {
        Iterator<Runnable> iterator = task.iterations().iterator();
        Event event = (Event) iterator.next();
        iterator.remove();
        CompletableFuture.runAsync(event, eventPool).thenRun(() -> this.addTask(task));
    }

}

