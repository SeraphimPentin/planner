package polytech;

import polytech.enums.Priority;
import polytech.enums.TypeTask;

import java.util.Queue;

public class SubmitterThread extends Thread {
    private final Queue<Task> queue;
    private volatile boolean stop;

    public SubmitterThread(Queue<Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Task task = new Task("Task ", TypeTask.EXTENDED, Priority.LOWEST);
        queue.add(task);
    }

    // логика генерации здач!?


}
