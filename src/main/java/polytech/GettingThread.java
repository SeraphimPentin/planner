package polytech;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class GettingThread extends Thread {

    private final Queue<Task> queue;
    private final Planner planner;
    private volatile boolean stop;


    public GettingThread(Queue<Task> queue, Planner planner) {
        this.queue = queue;
        this.planner = planner;
    }

    @Override
    public void run() {
        while (!stop) {
            queue.remove();
//            planner.addTask();
            //TimeUnit.SECONDS.sleep(1);
        }
    }
}
