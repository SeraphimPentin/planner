package polytech;
import polytech.model.MyPriorityBlockingQueue;
import polytech.enums.BaseStates;
import polytech.enums.ExtendedState;
import polytech.enums.TypeTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Planner {
    private final MyPriorityBlockingQueue<Task> readyQueue;
    private final ConcurrentMap<Task, BaseStates> baseTasksState;
    private final ConcurrentMap<Task, ExtendedState> extendedTasksState;
    private Task currentTask;

    public Planner(int capacity) {
        readyQueue = new MyPriorityBlockingQueue<>(capacity);
        baseTasksState = new ConcurrentHashMap<>();
        extendedTasksState = new ConcurrentHashMap<>();
    }

    public void addTask(Task task) {
        if (task.getType() == TypeTask.BASE) {
            baseTasksState.put(task, BaseStates.READY);
        } else {
            extendedTasksState.put(task, ExtendedState.READY);
        }
        readyQueue.add(task);
    }

//    public void start() {
//        while (true) {
//            Task task = readyQueue.poll();
//            if (task == null) {
//                break;
//            }
//            currentTask = task;
//            if (task.getType() == TypeTask.BASE) {
//                baseTasksState.put(task, BaseStates.RUNNING);
//                task.run();
//                baseTasksState.put(task, BaseStates.READY);
//            } else {
//                extendedTasksState.put(task, ExtendedState.RUNNING);
//                task.run();
//                extendedTasksState.put(task, ExtendedState.READY);
//            }
//            currentTask = null;
//        }
//    }

    public void suspend() {
        if (currentTask != null) {
            if (currentTask.getType() == TypeTask.BASE) {
                baseTasksState.put(currentTask, BaseStates.SUSPENDED);
            } else {
                extendedTasksState.put(currentTask, ExtendedState.SUSPENDED);
            }
        }
    }

    public void resume() {
        if (currentTask != null) {
            if (currentTask.getType() == TypeTask.BASE) {
                baseTasksState.put(currentTask, BaseStates.READY);
            } else {
                extendedTasksState.put(currentTask, ExtendedState.READY);
            }
        }
    }

    public void waitEvent(Task task) {
        if (task.getType() == TypeTask.EXTENDED) {
            extendedTasksState.put(task, ExtendedState.WAITING);
        }
    }

    public void activate(Task task) {
        if (task.getType() == TypeTask.EXTENDED) {
            extendedTasksState.put(task, ExtendedState.READY);
            readyQueue.add(task);
        }
    }

    public boolean isEmpty() {
        return readyQueue.isEmpty();
    }
}

