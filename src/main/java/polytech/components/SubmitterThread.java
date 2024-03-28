package polytech.components;

import polytech.domain.ATask;
import polytech.domain.Task;
import polytech.enums.Priority;
import polytech.enums.TypeTask;

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
        Task task1 = new ATask(Priority.LOW, TypeTask.BASE) {
            @Override
            public void run() {
                System.out.println("Low doing 1st part of job");
                doSleep(1000);
                notifyListenerAboutIterationDone();
                System.out.println("Low doing 2nd part of job");
                doSleep(1000);
                System.out.println("Low DONE");
            }
        };
        queue.add(task1);

        Task task2 = new ATask(Priority.MIDDLE, TypeTask.BASE) {
            @Override
            public void run() {
                System.out.println("Middle doing 1st part of job");
                doSleep(2000);
                notifyListenerAboutIterationDone();
                System.out.println("Middle doing 2nd part of job");
                doSleep(2000);
                System.out.println("Middle DONE");
            }
        };
        queue.add(task2);

        Task task3 = new ATask(Priority.LOW, TypeTask.BASE) {
            @Override
            public void run() {
                System.out.println("Low2 doing 1st part of job");
                doSleep(1000);
                notifyListenerAboutIterationDone();
                System.out.println("Low2 doing 2nd part of job");
                doSleep(1000);
                System.out.println("Low2 DONE");
            }
        };
        queue.add(task3);

        Task task4 = new ATask(Priority.HIGH, TypeTask.BASE) {
            @Override
            public void run() {
                System.out.println("HIGH doing 1st part of job");
                doSleep(3000);
                notifyListenerAboutIterationDone();
                System.out.println("HIGH doing 2nd part of job");
                doSleep(3000);
                System.out.println("HIGH DONE");
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
