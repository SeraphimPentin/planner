package polytech.domain;

/**
 * Интерфейс таски, выполннение которой подразумевает выполнение с явными шагами или итерациями.
 * На таску можно повестить листенер, который таска будет оповещать при выполнении очередной итерации
 */
public interface IterativeTask extends Runnable {

    void setListener(TaskListener listener);

    interface TaskListener {
        void iterationDone();
    }

}
