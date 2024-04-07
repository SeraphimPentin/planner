package polytech.domain.planner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.Task;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public interface TaskActable{

    Logger logger = LoggerFactory.getLogger(TaskActable.class);

    default void activate(Task task) {
        transit(task, TaskState.READY);
    }

    default void start(Task task) {
        transit(task, TaskState.RUNNING);
    }

    default void wait(Task task) {
        transit(task, TaskState.WAITING);
    }

    default void terminate(Task task) {
        transit(task, TaskState.SUSPENDED);
    }

    default void release(Task task) {
        transit(task, TaskState.READY);
    }

    default void preempt(Task task) {
        transit(task, TaskState.READY);
    }

    private void transit(Task task, TaskState ready) {
        logging(task.priority(), task.getState(), ready);
        task.setState(ready);
    }
    private void logging(Priority priority, TaskState oldState, TaskState newState) {
        if (oldState == newState) {
            logger.error(String.format("Task with %s priority in state: %s ", priority, newState));
        } else {
            logger.info(String.format("Task with %s priority in state: %s -> %s ", priority, oldState, newState));
        }
    }
}
