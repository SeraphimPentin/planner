package polytech.domain.planner;

import polytech.domain.Event;
import polytech.domain.Task;
import polytech.enums.TaskState;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class FJPTask extends RecursiveAction {
    private final Task task;
    private final Queue<FJPTask> highPriorityTasks;
    private final Consumer<Task> eventConsumer;

    protected volatile TaskState state = TaskState.SUSPENDED;

    public FJPTask(Task task, Queue<FJPTask> highPriorityTasks, Consumer<Task> eventConsumer) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
        this.eventConsumer = eventConsumer;
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        state = TaskState.READY;
        runTask();
        state = TaskState.SUSPENDED;
    }

    protected void runTask() {
        Iterable<Runnable> iterations = task.iterations();
        for (Iterator<Runnable> i = iterations.iterator(); i.hasNext(); ) {
            Runnable iteration = i.next();
            if (iteration instanceof Event) {
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
        state = TaskState.READY;
        for (FJPTask task : highPriorityTasks) {
            //TODO check for done and remove task
            task.join();
        }
        state = TaskState.RUNNING;
    }
}
