package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TaskState;

/**
 * Задача в нашей доменной модели:
 * - Имеет приоритет
 * - Может получать и устанваливать статус задачи
 * - Обязана иметь пошаговую логику выполнения (extends IterativeTask)
 */
public interface Task extends IterativeTask {

    Priority priority();

    TaskState getState();

    void setState(TaskState state);

}
