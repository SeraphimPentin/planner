package polytech.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import polytech.Properties;
import polytech.domain.TaskImpl;
import polytech.enums.Priority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class TaskGeneratorTest {

    @Test
    void checkThatAllPrioritiesCanBeCreated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TaskGenerator taskGenerator = new TaskGenerator();
        int totalPriorities = Priority.values().length;
        Set<Priority> counts = new HashSet<>();
        int iterations = 20;

        Method randomPriority = TaskGenerator.class.getDeclaredMethod("randomPriority");
        randomPriority.setAccessible(true);

        for (int i = 0; i < iterations; i++) {
            Priority priority = (Priority) randomPriority.invoke(taskGenerator);
            counts.add(priority);
        }
        Assertions.assertEquals(counts.size(), totalPriorities);
    }

    @Test
    void checkCreateRandomTask() {
        TaskGenerator taskGenerator = new TaskGenerator();
        TaskImpl task = taskGenerator.createRandomTask();
        Iterable<Runnable> iterations = task.iterations();
        int iterationsCount = 0;

        for (Runnable iteration : iterations) {
            iterationsCount++;
        }

        Assertions.assertNotNull(task);
        Assertions.assertNotNull(iterations);
        Assertions.assertTrue(iterationsCount <= Properties.MAX_ITERATION_COUNT);
    }
}