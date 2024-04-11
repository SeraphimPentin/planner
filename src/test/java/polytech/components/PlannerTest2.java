package polytech.components;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import polytech.domain.Task;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public class PlannerTest2 extends PlannerTestBase {

    @Test
    public void highSuspendsLowExecution() throws InterruptedException {
        BlockingQueue<Task> queue = startPlannerAndGetIsQueue();

        ConditionWithLock lowIsRunning = ConditionWithLock.createNew();
        ConditionWithLock check1 = ConditionWithLock.createNew();
        ConditionWithLock highIsRunning = ConditionWithLock.createNew();
        ConditionWithLock check2 = ConditionWithLock.createNew();


        TaskImpl low = new TaskImpl(Priority.LOW, listOf(
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
        Thread.sleep(100);
        lowIsRunning.await();
        queue.add(high);

        Assertions.assertEquals(TaskState.RUNNING, low.getState());
        Assertions.assertTrue(TaskState.READY == high.getState() || TaskState.SUSPENDED == high.getState());
        check1.signal();


        highIsRunning.await();
        Assertions.assertEquals(TaskState.RUNNING, high.getState());
        Assertions.assertEquals(TaskState.READY, low.getState());
        check2.signal();

    }
}
