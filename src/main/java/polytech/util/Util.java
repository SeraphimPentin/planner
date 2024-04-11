package polytech.util;

import java.util.LinkedList;
import java.util.List;

import polytech.Properties;
import polytech.domain.Event;

public class Util {

    private static void doWork(int count) {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += i;
        }
    }

    public static List<Runnable> listOf(int countIteration, int countToSum) {
        List<Runnable> list = new LinkedList<>();
        double probability = Math.random();
        for (int count = 0; count < countIteration; count++) {
            if (probability > Properties.EVENT_PROBABILITY) {
                list.add(new Runnable() {
                    @Override
                    public void run() {
                        doWork(countToSum);
                    }

                });
            }
            else {
                list.add(
                        new Event() {
                            @Override
                            public void run() {
                                doWork(countToSum);
                            }
                        }
                );
            }
        }
        return list;
    }
}
