package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TaskState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Collection;

public class TaskImpl implements Task {
    private volatile TaskState state = TaskState.SUSPENDED;
    private final Priority priority;
    private final Collection<Runnable> iterations;
    private final List<TaskState> listStates = Collections.synchronizedList(new ArrayList<>());
    private final UUID uuid;

    public TaskImpl(Priority priority, Collection<Runnable> iterations) {
        this.priority = priority;
        this.iterations = iterations;
        this.uuid = UUID.randomUUID();
        listStates.add(state);
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
    public String uuid() {
        return uuid.toString();
    }

    @Override
    public Iterable<Runnable> iterations() {
        return iterations;
    }
    @Override
    public String toString() {
        return "TaskImpl{" +
                "priority=" + priority +
                ", iterations_size=" + iterations.size() +
                ", uuid=" + uuid +
                '}';
    }
}
