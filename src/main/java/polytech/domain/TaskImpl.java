package polytech.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.enums.Priority;
import polytech.enums.TaskState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskImpl implements Task {
    private volatile TaskState state;
    private final Priority priority;
    private final Iterable<Runnable> iterations;
    private final List<TaskState> listStates = Collections.synchronizedList(new ArrayList<>());
    private static final Logger logger = LoggerFactory.getLogger(TaskImpl.class);

    public TaskImpl(Priority priority, Iterable<Runnable> iterations) {
        this.priority = priority;
        this.iterations = iterations;
    }

    public List<TaskState> getListStates() {
        return listStates;
    }

    @Override
    public Priority priority() {
        return priority;
    }

    @Override
    public TaskState getState() {
        return state;
    }

    @Override
    public void setState(TaskState oldState, TaskState newState) {
        this.state = newState;
        listStates.add(state);
        if (oldState ==  newState) {
            logger.info("Task with " + priority() + " priority in state: " + newState);
        } else {
            logger.info("Task with " + priority() + " priority in state: " + oldState + " -> " + newState);
        }
    }

    @Override
    public Iterable<Runnable> iterations() {
        return iterations;
    }
}
