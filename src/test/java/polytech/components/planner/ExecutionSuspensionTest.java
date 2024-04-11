package polytech.components.planner;

import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import polytech.domain.Task;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public class ExecutionSuspensionTest extends PlannerTestBase {

    @Test
    public void highTasksSuspendsLowExecution() throws InterruptedException {
        BlockingQueue<Task> queue = startPlannerAndGetIsQueue();

        TestCondition lowIsRunning = TestCondition.create();
        TestCondition check1 = TestCondition.create();
        TestCondition highIsRunning = TestCondition.create();
        TestCondition check2 = TestCondition.create();


        Task low = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    doWork(100);
                    lowIsRunning.signal();
                    check1.await();
                    doWork(100_000);

                },
                () -> {
                    doWork(100);
                }
        ));

        TaskImpl high = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    doWork(100);
                    highIsRunning.signal();
                    check2.await();
                    doWork(100);
                }
        ));

        queue.add(low);
        lowIsRunning.await();
        queue.add(high);

        Assertions.assertEquals(TaskState.RUNNING, low.getState());
        Assertions.assertTrue(TaskState.READY == high.getState() || TaskState.SUSPENDED == high.getState());
        check1.signal();


        highIsRunning.await();
        Assertions.assertEquals(TaskState.RUNNING, high.getState());
        Assertions.assertEquals(TaskState.SUSPENDED, low.getState());
        check2.signal();

    }
}
