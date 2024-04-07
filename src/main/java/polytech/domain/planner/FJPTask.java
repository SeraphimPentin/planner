package polytech.domain.planner;

import polytech.domain.Event;
import polytech.domain.Task;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class FJPTask extends RecursiveAction implements TaskActable {
    private final Task task;
    private final Queue<FJPTask> highPriorityTasks;
    private final Consumer<Task> eventConsumer;


    public FJPTask(Task task, Queue<FJPTask> highPriorityTasks, Consumer<Task> eventConsumer) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
        this.eventConsumer = eventConsumer;
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        activate(task);
        runTask();
    }

    protected void runTask() {
        start(task);
        Iterable<Runnable> iterations = task.iterations();
        for (Iterator<Runnable> i = iterations.iterator(); i.hasNext(); ) {
            Runnable iteration = i.next();
            if (iteration instanceof Event) {
                wait(task);
                eventConsumer.accept(task);
                return; //Make task computed. Release tasks joined on this one
            }
            i.remove();
            iteration.run();
            preemptIfNeeded();
        }
    }

    protected void preemptIfNeeded() {
        if (highPriorityTasks == null) {
            return;
        }
        for (Iterator<FJPTask> iterator = highPriorityTasks.iterator(); iterator.hasNext(); ) {
            FJPTask task = iterator.next();
            if (!task.isDone()) {
                preempt(this.task);
                task.join();
            } else {
                iterator.remove();
            }
        }
    }
}
