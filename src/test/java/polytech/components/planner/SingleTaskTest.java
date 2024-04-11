package polytech.components.planner;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import polytech.domain.Event;
import polytech.domain.Task;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.enums.TaskState;

public class SingleTaskTest extends PlannerTestBase {

    @Test
    public void checkSingleTaskExecutionGraph() throws InterruptedException {
        BlockingQueue<Task> queue = startPlannerAndGetIsQueue();
        TaskImpl task = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    doWork(HUGE_WORK);
                },
                () -> {
                    doWork(SMALL_WORK);
                },
                () -> {
                    doWork(VERY_SMALL_WORK);
                }
        ));
        queue.add(task);
        TimeUnit.SECONDS.sleep(1);

        List<TaskState> listStates = task.getListStates();
        Assertions.assertEquals(
                List.of(TaskState.SUSPENDED, TaskState.READY, TaskState.RUNNING, TaskState.SUSPENDED),
                listStates
        );
    }

    @Test
    public void checkSingleExtendedTaskExecutionGraph() throws InterruptedException {
        BlockingQueue<Task> queue = startPlannerAndGetIsQueue();
        TaskImpl task = new TaskImpl(Priority.HIGH, listOf(
                () -> {
                    doWork(HUGE_WORK);
                },
                new Event() {
                    @Override
                    public void run() {
                        doWork(SMALL_WORK);
                    }
                },
                () -> {
                    doWork(VERY_SMALL_WORK);
                }
        ));
        queue.add(task);
        TimeUnit.SECONDS.sleep(1);

        List<TaskState> listStates = task.getListStates();
        Assertions.assertEquals(
                List.of(
                        TaskState.SUSPENDED,
                        TaskState.READY,
                        TaskState.RUNNING,
                        TaskState.WAITING,
                        TaskState.READY,
                        TaskState.RUNNING,
                        TaskState.SUSPENDED
                ),
                listStates
        );
    }

}
