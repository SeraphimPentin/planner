package polytech.components;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import polytech.Properties;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;
import  polytech.util.Util;


public class TaskGenerator {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public TaskImpl createRandomTask() {
        Random random = new Random();

        int iterationsCount = random.nextInt(Properties.MAX_ITERATION_COUNT);
        int countToSum = random.nextInt(1_000_000);

        Collection<Runnable> iterations = Util.listOf(iterationsCount, countToSum);
        Priority priority = randomPriority();

        return new TaskImpl(priority, iterations);
    }

    private static Priority randomPriority(){
        Priority[] priorities = Priority.values();
        int i = random.nextInt(priorities.length);
        return priorities[i];
    }
}