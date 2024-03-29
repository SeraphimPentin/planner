package polytech.domain;

import polytech.enums.Priority;

public abstract class ATask implements ExtendedTask {
    private volatile TaskListener listener;
    private volatile EventListener eventListener;
    private final Priority priority;

    protected ATask(Priority priority) {
        this.priority = priority;
    }

    @Override
    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    protected void notifyListenerAboutIterationDone() {
        if (listener != null) {
            listener.iterationDone();
        }
    }

    protected void fireEvent(Runnable event) {
        if (eventListener != null) {
            eventListener.eventFired(event);
        }
    }

    @Override
    public Priority priority() {
        return priority;
    }

    @Override
    public void setListener(TaskListener listener) {
        this.listener = listener;
    }
}
