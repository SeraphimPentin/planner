package polytech.components;

import polytech.domain.Task;
import java.util.concurrent.BlockingQueue;

public class SubmitterThread extends Thread {
    private final BlockingQueue<Task> queue;
    private volatile boolean stop;

    private final TaskGenerator taskGenerator;


    public SubmitterThread(BlockingQueue<Task> queue, TaskGenerator taskGenerator) {
        super("SubmitterThread");
        this.queue = queue;
        this.taskGenerator = taskGenerator;
    }

    @Override
    public void run() {
        while (true) {
            queue.add(taskGenerator.createRandomTask());
        }
    }
}
