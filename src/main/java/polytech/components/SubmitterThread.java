package polytech.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.ATask;
import polytech.domain.Task;
import polytech.enums.Priority;
import polytech.enums.TypeTask;

import java.util.concurrent.BlockingQueue;

public class SubmitterThread extends Thread {
    private final BlockingQueue<Task> queue;
    private volatile boolean stop;
    private static final Logger logger = LoggerFactory.getLogger(SubmitterThread.class);

    public SubmitterThread(BlockingQueue<Task> queue) {
        super("SubmitterThread");
        this.queue = queue;
    }

    @Override
    public void run() {
        Task task1 = new ATask(Priority.LOW, TypeTask.BASE) {
            @Override
            public void run() {
                logger.info("LOW BASE task doing 1st part of job");
                doSleep(1000);
                notifyListenerAboutIterationDone();
                logger.info("LOW BASE task doing 2nd part of job");
                doSleep(1000);
                logger.info("LOW BASE task DONE");
            }
        };
        queue.add(task1);

        Task task2 = new ATask(Priority.MIDDLE, TypeTask.BASE) {
            @Override
            public void run() {
                logger.info("MIDDLE BASE  task doing 1st part of job");
                doSleep(2000);
                notifyListenerAboutIterationDone();
                logger.info("MIDDLE BASE task doing 2nd part of job");
                doSleep(2000);
                logger.info("MIDDLE BASE task DONE");
            }
        };
        queue.add(task2);

        Task task3 = new ATask(Priority.LOW, TypeTask.BASE) {
            @Override
            public void run() {
                logger.info("LOW2 BASE task doing 1st part of job");
                doSleep(1000);
                notifyListenerAboutIterationDone();
                logger.info("LOW2 BASE task doing 2nd part of job");
                doSleep(1000);
                logger.info("LOW2 BASE task DONE");
            }
        };
        queue.add(task3);

        Task task4 = new ATask(Priority.HIGH, TypeTask.BASE) {
            @Override
            public void run() {
                logger.info("HIGH BASE task doing 1st part of job");
                doSleep(3000);
                notifyListenerAboutIterationDone();
                logger.info("HIGH BASE task doing 2nd part of job");
                doSleep(3000);
                logger.info("HIGH BASE task DONE");
            }
        };
        queue.add(task4);

    }

    private static void doSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
