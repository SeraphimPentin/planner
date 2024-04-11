package polytech.components.planner;

import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import polytech.domain.Event;
import polytech.domain.Task;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public class EventTest extends PlannerTestBase {


    @Test
    public void lowTaskProceededBecauseHighWaitsForEvent() {
        BlockingQueue<Task> queue = startPlannerAndGetIsQueue();

        TestCondition highIsRunning = TestCondition.create();
        TestCondition check1 = TestCondition.create();

        TestCondition lowIsRunning = TestCondition.create();
        TestCondition eventIsRunning = TestCondition.create();
        TestCondition check2 = TestCondition.create();

        Task high = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    highIsRunning.signal();
                    check1.await();
                    doWork(SMALL_WORK);
                },
                new Event() {
                    @Override
                    public void run() {
                        eventIsRunning.signal();
                        check2.await();
                        doWork(VERY_SMALL_WORK);
                    }
                },
                () -> {
                    doWork(SMALL_WORK);
                }
        ));

        Task low = new TaskImpl(Priority.LOW, listOf(
                () -> {
                    lowIsRunning.signal();
                    check2.await();
                    doWork(HUGE_WORK);
                }
        ));

        queue.add(high);
        queue.add(low);

        highIsRunning.await();
        Assertions.assertSame(TaskState.RUNNING, high.getState());
        Assertions.assertSame(TaskState.READY, low.getState());
        check1.signal();

        eventIsRunning.await();
        lowIsRunning.await();
        Assertions.assertTrue(TaskState.WAITING == high.getState() || TaskState.READY == high.getState());
        Assertions.assertSame(TaskState.RUNNING, low.getState());
        check2.signal();









    }
}
