package polytech.domain.planner;

import polytech.domain.Event;
import polytech.domain.Task;
import polytech.enums.TaskState;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public class FJPTask extends RecursiveAction {
    private final Queue<FJPTask> highPriorityTasks;
    private final Task task;

    protected volatile TaskState state = TaskState.SUSPENDED;

    public FJPTask(Task task, Queue<FJPTask> highPriorityTasks) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
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
                //return; //Make task computed
            }
            iteration.run();
            i.remove();
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
