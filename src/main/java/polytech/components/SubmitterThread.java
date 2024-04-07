package polytech.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polytech.domain.Event;
import polytech.domain.TaskImpl;
import polytech.domain.Task;
import polytech.enums.Priority;

import java.util.LinkedList;
import java.util.List;
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
        Task task1 = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    doWork(100_000);
                },
                () -> {
                    doWork(100_000);
                    logger.info("LOW1 DONE");

                }
        ));

        queue.add(task1);

        Task task2 = new TaskImpl(Priority.MIDDLE, listOf(
                () -> {
                    doWork(1_000_000);
                },
                () -> {
                    doWork(1_000_000);
                    logger.info("MIDDLE1 DONE");

                }
        ));
        queue.add(task2);

        Task task3 = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    doWork(500_000);
                },
                () -> {
                    doWork(500_000);
                    logger.info("LOW2 DONE");
                }
        ));
        queue.add(task3);

        Task task4 = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    doWork(3_000_000);
                },
                new Event() {
                    @Override
                    public void run() {
                        doWork(3_000_000);
                    }
                },
                () -> {
                    doWork(3_000_000);
                    logger.info("HIGH DONE");

                }
        ));

        queue.add(task4);

        Task task5 = new TaskImpl(Priority.LOWEST, listOf(
                () -> {
                    doWork(100_000);
                },
                () -> {
                    doWork(100_000);
                    logger.info("LOWEST DONE");
                }
        ));
        queue.add(task5);

        Task task6 = new TaskImpl(Priority.MIDDLE, listOf(
                () -> {
                    doWork(1_000_000);
                },
                new Event() {
                    @Override
                    public void run() {
                        doWork(1_000_000);
                    }
                },
                () -> {
                    doWork(1_000_000);
                    logger.info("MIDDLE2 DONE");
                }
        ));

        queue.add(task6);
    }



    private static List<Runnable> listOf(Runnable... iterations) {
        List<Runnable> list = new LinkedList<>();
        for (Runnable iteration : iterations) {
            list.add(iteration);
        }
        return list;
    }

    private static void doWork(int count) {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += i;
        }
    }
}
