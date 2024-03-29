package polytech.domain;

public interface ExtendedTask extends Task {
    void setEventListener(EventListener eventListener);

    interface EventListener {
        void eventFired(Runnable event);
    }
}
