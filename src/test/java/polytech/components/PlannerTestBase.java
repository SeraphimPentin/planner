package polytech.components;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import polytech.domain.Task;

public class PlannerTestBase {

    protected static BlockingQueue<Task> startPlannerAndGetIsQueue() {
        BlockingQueue<Task> queue = getTaskQueue();
        startPlanner(queue);
        return queue;
    }

    protected static PriorityBlockingQueue<Task> getTaskQueue() {
        return new PriorityBlockingQueue<>(20, Comparator.comparing(Task::priority).reversed());
    }

    protected static void startPlanner(BlockingQueue<Task> queue) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(
                1,
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                false,
                1,
                1,
                0,
                forkJoinPool1 -> true,
                60_000L,
                TimeUnit.DAYS
        );
        Planner planner = new Planner(queue, forkJoinPool);
        planner.start();
    }

    protected static class ConditionWithLock {
        private final Lock lock;
        private final Condition condition;

        protected ConditionWithLock(Lock lock) {
            this.lock = lock;
            this.condition = lock.newCondition();
        }

        public static ConditionWithLock createNew() {
            Lock lock = new ReentrantLock();
            return new ConditionWithLock(lock);
        }

        public void signal() {
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public void await() {
            lock.lock();
            try {
                condition.awaitUninterruptibly();
            } finally {
                lock.unlock();
            }
        }
    }


    protected static List<Runnable> listOf(Runnable... iterations) {
        List<Runnable> list = new LinkedList<>();
        for (Runnable iteration : iterations) {
            list.add(iteration);
        }
        return list;
    }

    protected static void doWork(long count) {
        long sum = 0;
        for (int i = 0; i < count; i++) {
            sum += i;
        }
    }
}
