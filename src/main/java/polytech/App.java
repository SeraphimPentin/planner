package polytech;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.components.GetterThread;
import polytech.components.Planner;
import polytech.components.SubmitterThread;
import polytech.domain.Task;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void init() {
        logger.info("---Start application---");
    }

    public void run() {
        logger.info("---The end---");
    }

    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        //app.init();
        //app.run();

        BlockingQueue<Task> queue = new PriorityBlockingQueue<>(20, Comparator.comparing(Task::priority).reversed());
        Planner planner = new Planner();

        SubmitterThread submitterThread = new SubmitterThread(queue);
        GetterThread getterThread = new GetterThread(queue, planner);
        submitterThread.start();
        getterThread.start();

        submitterThread.join();
        getterThread.join();
    }
}
