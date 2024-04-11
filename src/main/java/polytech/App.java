package polytech;


import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import polytech.components.Planner;
import polytech.components.SubmitterThread;
import polytech.components.TaskGenerator;
import polytech.domain.Task;

public class App {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Task> queue = new PriorityBlockingQueue<>(20, Comparator.comparing(Task::priority).reversed());
        Planner planner = new Planner(queue);
        TaskGenerator taskGenerator = new TaskGenerator();
        SubmitterThread submitterThread = new SubmitterThread(queue, taskGenerator);

        submitterThread.start();
        planner.start();

        submitterThread.join();
        planner.join();
    }
}
