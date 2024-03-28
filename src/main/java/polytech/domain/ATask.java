package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TypeTask;

public abstract class ATask implements Task {
    private volatile TaskListener listener;
    private final Priority priority;
    private final TypeTask type;

    protected ATask(Priority priority, TypeTask type) {
        this.priority = priority;
        this.type = type;
    }

    protected void notifyListenerAboutIterationDone() {
        if (listener != null) {
            listener.iterationDone();
        }
    }

    @Override
    public Priority priority() {
        return priority;
    }

    @Override
    public TypeTask type() {
        return type;
    }

    @Override
    public void setListener(TaskListener listener) {
        this.listener = listener;
    }
}
