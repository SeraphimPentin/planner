package polytech.domain.planner;

import polytech.domain.Task;
import polytech.enums.TaskState;

public interface TaskAction {
    default void activate(Task task){
        task.setState(task.getState(), TaskState.READY);
    }

    default void start(Task task){
        task.setState(task.getState(), TaskState.RUNNING);
    }

    default void wait(Task task){
        task.setState(task.getState(), TaskState.WAITING);
    }

    default void terminate(Task task){
        task.setState(task.getState(), TaskState.SUSPENDED);
    }

    default void release(Task task){
        task.setState(task.getState(), TaskState.READY);
    }

    default void preempt(Task task){
        task.setState(task.getState(), TaskState.READY);
    }
}
