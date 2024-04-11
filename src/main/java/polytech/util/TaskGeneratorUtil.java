package polytech.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import polytech.Properties;
import polytech.domain.Event;

public class TaskGeneratorUtil {

    private static void doWork(int count) {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += i;
        }
    }
    private static int workCount(){
        Random random = new Random();
        return random.nextInt(1_000_000);
    }

    public static List<Runnable> listOf(int countIteration) {
        List<Runnable> list = new LinkedList<>();
        double probability = Math.random();
        for (int count = 0; count < countIteration; count++) {
            if (probability > Properties.EVENT_PROBABILITY) {
                list.add(new Runnable() {
                    @Override
                    public void run() {
                        doWork(workCount());
                    }
                });
            }
            else {
                list.add(
                        new Event() {
                            @Override
                            public void run() {
                                doWork(workCount());
                            }
                        }
                );
            }
        }
        return list;
    }
}
