package polytech;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.enums.BaseStates;
import polytech.enums.ExtendedState;
import polytech.enums.TypeTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Planner {

    private final Queue<Task> readyQueue;
    private final ConcurrentMap<Task, BaseStates> baseTasksState;
    private final ConcurrentMap<Task, ExtendedState> extendedTasksState;
    private Task currentTask;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public Planner(Queue<Task> queue) {
        readyQueue = queue;
        baseTasksState = new ConcurrentHashMap<>();
        extendedTasksState = new ConcurrentHashMap<>();
    }
    public void activate(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.SUSPENDED) {
            extendedTasksState.put(task, ExtendedState.READY);
        } else {
            baseTasksState.put(task, BaseStates.READY);
        }
        readyQueue.add(task);
        logger.info(task.getTaskName() +  " с приоритетом "+ task.getPriority() + " suspended -> ready");
    }

    /** Выполняется готовая задача, выбранная планировщиком.*/
    public void start(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.READY) {
            extendedTasksState.put(task, ExtendedState.RUNNING);
        } else {
            baseTasksState.put(task, BaseStates.RUNNING);
        }
        // todo Отпаравка задачи FJP ?!
        logger.info(task.getTaskName() +  " с приоритетом "+ task.getPriority() + " ready -> running");
    }

    /**
     * Переход в состояние ожидания.
     * Выполнение задачи продолжится только после выполнения события.
     */
    public void waitEvent(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.RUNNING) {
            extendedTasksState.put(task, ExtendedState.WAITING);
        }
        // todo Выполнение задачи продолжится только после выполнения события.
        logger.info(task.getTaskName() +  " с приоритетом "+ task.getPriority() +  " running -> waiting");
    }

    /**
     * Произошло по крайней мере одно событие, которое ожидала задача.
     */
    public void release(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.WAITING) {
            extendedTasksState.put(task, ExtendedState.READY);
        }
        // todo Ожидаем хотя бы одно событие, которое ожидает программа
        logger.info(task.getTaskName() + " с приоритетом "+ task.getPriority() +  " waiting -> ready");
    }

    /**
    * Планировщик решает начать другую задачу.
    * Запущенная задача переводится в готовности.*/
    public void preempt(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.RUNNING) {
            extendedTasksState.put(task, ExtendedState.READY);
        } else {
            baseTasksState.put(task, BaseStates.READY);
        }
        // todo
        logger.info(task.getTaskName() + " с приоритетом "+ task.getPriority() + " running -> ready");
    }

    /**
     * Задача переходит в подвешенное состояние
     */
    public void terminate(Task task) {
        if (task.getType() == TypeTask.EXTENDED && extendedTasksState.get(task) == ExtendedState.RUNNING) {
            extendedTasksState.put(task, ExtendedState.SUSPENDED);
        } else {
            baseTasksState.put(task, BaseStates.SUSPENDED);
        }
        // todo Задача переходит в подвешенное состояние
        logger.info(task.getTaskName() + " с приоритетом "+ task.getPriority() + " running -> suspended");
    }


    public void addTask(Task task) {
        if (task.getType() == TypeTask.BASE) {
            baseTasksState.put(task, BaseStates.READY);
        } else {
            extendedTasksState.put(task, ExtendedState.READY);
        }
        readyQueue.add(task);
    }
}

