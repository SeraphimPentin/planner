package polytech.components;

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

    public SubmitterThread(BlockingQueue<Task> queue) {
        super("SubmitterThread");
        this.queue = queue;
    }

    @Override
    public void run() {
        Task task1 = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    System.out.println("Low doing 1st part of job");
                    doSleep(1000);
                },
                () -> {
                    System.out.println("Low doing 2nd part of job");
                    doSleep(1000);
                    System.out.println("Low DONE");
                }
        ));

        queue.add(task1);

        Task task2 = new TaskImpl(Priority.MIDDLE, listOf(
                () -> {
                    System.out.println("Middle doing 1st part of job");
                    doSleep(2000);
                },
                () -> {
                    System.out.println("Middle doing 2nd part of job");
                    doSleep(2000);
                    System.out.println("Middle DONE");
                }
        ));
        queue.add(task2);

        Task task3 = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    System.out.println("Low2 doing 1st part of job");
                    doSleep(1000);
                },
                () -> {
                    System.out.println("Low2 doing 2nd part of job");
                    doSleep(1000);
                    System.out.println("Low2 DONE");
                }
        ));
        queue.add(task3);

        Task task4 = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    System.out.println("HIGH doing 1st part of job");
                    doSleep(3000);
                    System.out.println("HIGH done 1st part of job");
                },
                new Event() {
                    @Override
                    public void run() {
                        System.out.println("Event is being done");
                        doSleep(5000);
                        System.out.println("Event is done");
                    }
                },
                () -> {
                    System.out.println("HIGH doing 2nd part of job");
                    doSleep(3000);
                    System.out.println("HIGH DONE");
                }
        ));

        queue.add(task4);

    }

    private static List<Runnable> listOf(Runnable... iterations) {
        List<Runnable> list = new LinkedList<>();
        for (Runnable iteration : iterations) {
            list.add(iteration);
        }
        return list;
    }

    private static void doSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
