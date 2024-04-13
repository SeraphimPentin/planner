package polytech.domain.planner;

import polytech.domain.Event;
import polytech.domain.Task;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class FJPTask extends RecursiveAction implements TaskActable {
    private final Task task;
    private final Iterator<Queue<FJPTask>> highPriorityTasks;
    private final Semaphore readyTasksSemaphore;
    private final Consumer<Task> eventConsumer;


    public FJPTask(Task task, Iterator<Queue<FJPTask>> highPriorityTasks, Semaphore readyTasksSemaphore, Consumer<Task> eventConsumer) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
        this.readyTasksSemaphore = readyTasksSemaphore;
        this.eventConsumer = eventConsumer;
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        runTask();
    }

    protected void runTask() {
        startTask();
        Iterable<Runnable> iterations = task.iterations();
        for (Iterator<Runnable> iterator = iterations.iterator(); iterator.hasNext(); ) {
            Runnable iteration = iterator.next();
            if (iteration instanceof Event) {
                wait(task);
                eventConsumer.accept(task);
                return; //Make task computed. Release tasks joined on this one
            }
            iterator.remove();
            iteration.run();
            preemptIfNeeded();
        }
        terminate(task);
    }

    private void startTask() {
        readyTasksSemaphore.release(); //transition from READY state
        start(task);
    }

    protected void preemptIfNeeded() {
        boolean wasPreempted = false;
        while (highPriorityTasks.hasNext()) {
            Queue<FJPTask> prioritizedTasks = highPriorityTasks.next();
            for (Iterator<FJPTask> iterator = prioritizedTasks.iterator(); iterator.hasNext(); ) {
                FJPTask task = iterator.next();
                if (task.isDone()) {
                    iterator.remove();
                    continue;
                }
                preempt(this.task);
                wasPreempted = true;
                task.join();
            }
        }
        if (wasPreempted) {
            start(task);
        }
    }
}
