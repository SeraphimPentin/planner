package polytech.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import polytech.domain.Event;

import java.util.List;


class TaskGeneratorUtilTest {

    @Test
    void testListOfEmpty() {
        int countIteration = 0;
        List<Runnable> list = TaskGeneratorUtil.listOf(countIteration);

        Assertions.assertTrue(list.isEmpty(), "List should be empty");
    }

    @Test
    void testListOf() {
        int countIteration = 10;
        List<Runnable> list = TaskGeneratorUtil.listOf(countIteration);

        for (Runnable runnable : list) {
           Assertions.assertTrue(runnable instanceof Runnable || runnable instanceof Event, "Element is not instance of Runnable or Event");
        }
        Assertions.assertEquals(countIteration, list.size());
    }

}