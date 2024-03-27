package polytech.domain.planner;

import polytech.domain.ITask;
import polytech.enums.BaseStates;

import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public class FJPTask extends RecursiveAction {
    private final Queue<FJPTask> highPriorityTasks;
    private final ITask task;

    private volatile BaseStates state = BaseStates.SUSPENDED;

    public FJPTask(ITask task, Queue<FJPTask> highPriorityTasks) {
        this.task = task;
        this.highPriorityTasks = highPriorityTasks;
        task.setListener(this::preemptIfNeeded);
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        state = BaseStates.READY;
        task.run();
        state = BaseStates.SUSPENDED;
    }

    protected void preemptIfNeeded() {
        if (highPriorityTasks == null) {
            return;
        }
        state = BaseStates.READY;
        for (FJPTask task : highPriorityTasks) {
            //TODO check for done and remove task
            task.join();
        }
        state = BaseStates.RUNNING;
    }

}
