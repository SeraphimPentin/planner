package polytech.model;

import polytech.Task;
import polytech.enums.Priority;
import polytech.enums.TypeTask;


public class TestPriorityBlockingQueue {

    public static void main(String[] args) throws InterruptedException {

        MyPriorityBlockingQueue<Task> priorityQueue = new MyPriorityBlockingQueue<>(6);


        priorityQueue.add(new Task("Taks 1", Priority.LOWEST, TypeTask.BASE));
        priorityQueue.add(new Task("Taks 2", Priority.HIGH, TypeTask.BASE));
        priorityQueue.add(new Task("Taks 3", Priority.MIDDLE, TypeTask.EXTENDED));
        priorityQueue.add(new Task("Taks 4", Priority.HIGH,TypeTask.EXTENDED));
        priorityQueue.add(new Task("Taks 5", Priority.LOW ,TypeTask.EXTENDED));
        priorityQueue.add(new Task("Taks 6", Priority.LOWEST,TypeTask.BASE));


        System.out.println(priorityQueue.peek()); // возвращает головной элемент очереди, не удаляя его.
        System.out.println(priorityQueue.poll()); // извлекает и удаляет головной элемент очереди, если он существует.
        System.out.println(priorityQueue.take()); // извлекает и удаляет головной элемент очереди, ждет, пока он не станет доступен.

        System.out.println("_______________________");
        System.out.println(priorityQueue.size());
        System.out.println("_______________________");

        while (!priorityQueue.isEmpty()) {
            System.out.println(priorityQueue.poll().toString());
        }
    }


}
