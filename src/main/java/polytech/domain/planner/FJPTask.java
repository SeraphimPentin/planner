package polytech.domain.planner;

import polytech.domain.Task;
import polytech.enums.TaskState;

import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public class FJPTask extends RecursiveAction {
    private final Queue<FJPTask> highPriorityTasks;
    private final Task task;

    protected volatile TaskState state = TaskState.SUSPENDED;

    public FJPTask(Task task, Queue<FJPTask> highPriorityTasks) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
        task.setListener(this::preemptIfNeeded);
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        state = TaskState.READY;
        task.run();
        state = TaskState.SUSPENDED;
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
