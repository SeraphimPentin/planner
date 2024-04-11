package polytech.components;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import polytech.Properties;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import polytech.util.TaskGeneratorUtil;


public class TaskGenerator {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public TaskImpl createRandomTask() {
        int iterationsCount = random.nextInt(Properties.MAX_ITERATION_COUNT);

        Collection<Runnable> iterations = TaskGeneratorUtil.listOf(iterationsCount);
        Priority priority = randomPriority();

        return new TaskImpl(priority, iterations);
    }

    private Priority randomPriority(){
        Priority[] priorities = Priority.values();
        int i = random.nextInt(priorities.length);
        return priorities[i];
    }
}