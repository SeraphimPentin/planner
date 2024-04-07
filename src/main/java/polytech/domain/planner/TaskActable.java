package polytech.domain.planner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.Task;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public interface TaskActable{

    Logger logger = LoggerFactory.getLogger(TaskActable.class);

    default void activate(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.READY);
        task.setState(TaskState.READY);
    }

    default void start(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.RUNNING);
        task.setState(TaskState.RUNNING);
    }

    default void wait(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.WAITING);
        task.setState(TaskState.WAITING);
    }

    default void terminate(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.SUSPENDED);
        task.setState(TaskState.SUSPENDED);
    }

    default void release(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.READY);
        task.setState(TaskState.READY);
    }

    default void preempt(Task task) {
        checkDuplicatesAndLogging(task.priority(), task.getState(), TaskState.READY);
        task.setState(TaskState.READY);
    }

    private void checkDuplicatesAndLogging(Priority priority, TaskState oldState, TaskState newState) {
        if (oldState == newState) {
            logger.info(String.format("Task with %s priority in state: %s ", priority, newState));
        } else {
            logger.info(String.format("Task with %s priority in state: %s -> %s ", priority, oldState, newState));
        }
    }
}
