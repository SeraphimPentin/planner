package polytech.domain;

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
    public void setState(TaskState state) {
        this.state = state;
        listStates.add(state);
    }

    @Override
    public Iterable<Runnable> iterations() {
        return iterations;
    }
}
