package polytech.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.Task;

import java.util.concurrent.BlockingQueue;

public class GetterThread extends Thread {

    private final BlockingQueue<Task> queue;
    private final Planner planner;
    //private volatile boolean stop = true;
    private static final Logger logger = LoggerFactory.getLogger(GetterThread.class);


    public GetterThread(BlockingQueue<Task> queue, Planner planner) {
        super("GetterThread");
        this.queue = queue;
        this.planner = planner;
    }

    @Override
    public void run() {
        while (true) {
            Task task;
            try {
                task = queue.take();
                logger.info("Get task " + task.priority() + " " + task.type());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            planner.addTask(task);
        }
    }
}
