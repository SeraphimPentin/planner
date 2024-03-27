package polytech.components;

import polytech.domain.ITask;

import java.util.concurrent.BlockingQueue;

public class GetterThread extends Thread {

    private final BlockingQueue<ITask> queue;
    private final Planner planner;
    //private volatile boolean stop = true;

    public GetterThread(BlockingQueue<ITask> queue, Planner planner) {
        super("GetterThread");
        this.queue = queue;
        this.planner = planner;
    }

    @Override
    public void run() {
        while (true) {
            ITask task;
            try {
                task = queue.take();
                System.out.println("Get task " + task.priority());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            planner.addTask(task);
        }
    }
}
