package polytech.domain;

import polytech.enums.Priority;

/**
 * Задача в нашей доменной модели:
 * - Имеет приоритет
 * - Обязана иметь пошаговую логику выполнения (extends IterativeTask)
 */
public interface Task extends IterativeTask {

    Priority priority();

}
