package polytech.domain;

/**
 * Интерфейс таски, выполннение которой подразумевает выполнение с явными шагами или итерациями
 */
public interface IterativeTask {

    Iterable<Runnable> iterations();

}
