package polytech.components;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import polytech.domain.Task;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public class WaitsOnStartTest extends PlannerTestBase{

    @Test
    void lowWaitsHighWhileItsRuns() {
        PriorityBlockingQueue<Task> queue = getTaskQueue();

        TestCondition highIsRunning = TestCondition.create();
        TestCondition checksDone = TestCondition.create();

        Task task1 = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    doWork(100);
                },
                () -> {
                    doWork(100);
                }
        ));

        Task task2 = new TaskImpl(Priority.MIDDLE, listOf(
                () -> {
                    doWork(100);
                    highIsRunning.signal();;
                    checksDone.await();
                    doWork(100);
                }
        ));

        queue.add(task1);
        queue.add(task2);

        startPlanner(queue);

        highIsRunning.await();
        Assertions.assertEquals(TaskState.RUNNING, task2.getState());
        Assertions.assertEquals(TaskState.READY, task1.getState());
        checksDone.signal();

    }

    @Test
    void multipleLowWaitsHighWhileItsRuns() {
        PriorityBlockingQueue<Task> queue = getTaskQueue();

        TestCondition highIsRunning = TestCondition.create();
        TestCondition checksDone = TestCondition.create();

        List<Task> lows = List.of(
                new TaskImpl(Priority.LOW, listOf(
                        () -> {
                            doWork(100_000);
                        },
                        () -> {
                            doWork(100_000);
                        }
                )),
                new TaskImpl(Priority.LOW, listOf(
                        () -> {
                            doWork(100_000);
                        },
                        () -> {
                            doWork(100_000);
                        }
                ))
        );

        Task high = new TaskImpl(Priority.MIDDLE, listOf(
                () -> {
                    doWork(1_000L);
                    highIsRunning.signal();;
                    checksDone.await();
                    doWork(1_000L);
                }
        ));


        queue.add(high);
        queue.addAll(lows);

        startPlanner(queue);

        highIsRunning.await();

        Assertions.assertEquals(TaskState.RUNNING, high.getState());
        for (Task low : lows) {
            Assertions.assertEquals(TaskState.READY, low.getState());
        }

        checksDone.signal();

    }


}