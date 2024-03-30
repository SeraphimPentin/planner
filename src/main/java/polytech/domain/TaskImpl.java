package polytech.domain;

import polytech.enums.Priority;

public class TaskImpl implements Task {
    private final Priority priority;
    private final Iterable<Runnable> iterations;

    public TaskImpl(Priority priority, Iterable<Runnable> iterations) {
        this.priority = priority;
        this.iterations = iterations;
    }

    @Override
    public Priority priority() {
        return priority;
    }

    @Override
    public Iterable<Runnable> iterations() {
        return iterations;
    }
}
