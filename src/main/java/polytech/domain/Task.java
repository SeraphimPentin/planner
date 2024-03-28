package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TypeTask;

/**
 * Задача в нашей доменной модели:
 * - Имеет тип
 * - Имеет приоритет
 * - Обязана иметь пошаговую логику выполнения (extends IterativeTask)
 */
public interface Task extends IterativeTask {

    Priority priority();

    TypeTask type();

}
