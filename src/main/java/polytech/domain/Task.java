package polytech.domain;

import polytech.enums.BaseStates;
import polytech.enums.Priority;
import polytech.enums.TypeTask;

import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public abstract class Task extends RecursiveAction implements ITask {
    private final String taskName;
    private final TypeTask type;
    private final Priority priority;
    private volatile Queue<Task> highPriorityTasks; //FIXME volatile для поля, которое сеттится один раз?

    private BaseStates state = BaseStates.SUSPENDED;

    public Task(
            TypeTask type,
            Priority priority
    ) {
        this.taskName = "NONAME";
        this.type = type;
        this.priority = priority;
    }

    public Task(
            String taskName,
            TypeTask type,
            Priority priority
    ) {
        this.taskName = taskName;
        this.type = type;
        this.priority = priority;
    }

    @Override
    protected void compute() {
        preemptIfNeeded(); //Перед выполнением подождать более приоритетные
        state = BaseStates.READY;
        doWork();
        state = BaseStates.SUSPENDED;
    }

    protected void preemptIfNeeded() {
        if (highPriorityTasks == null) {
            return;
        }
        state = BaseStates.READY;
        for (Task task : highPriorityTasks) {
            //TODO check for done and remove task
            task.join();
        }
        state = BaseStates.RUNNING;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public Priority priority() {
        return priority;
    }

    @Override
    public TypeTask type() {
        return type;
    }

    public void setHighPriorityTasks(Queue<Task> highPriorityTasks) {
        this.highPriorityTasks = highPriorityTasks;
    }
}
