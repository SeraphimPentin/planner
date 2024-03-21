package polytech.model;

import polytech.Task;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class MyPriorityBlockingQueue<T extends Task> {
    private final PriorityBlockingQueue<T> priorityQueue;

    public MyPriorityBlockingQueue(Integer capacity) {
        this.priorityQueue = new PriorityBlockingQueue<>(capacity,
                Comparator.comparing(Task::getPriority).reversed());
    }

    public boolean add(T task) {
        return priorityQueue.add(task);
    }

    public boolean offer(T task) {
        return priorityQueue.offer(task);
    }

    public T poll() {
        return priorityQueue.poll();
    }

    public T take() throws InterruptedException {
        return priorityQueue.take();
    }

    public T peek() {
        return priorityQueue.peek();
    }

    public int size() {
        return priorityQueue.size();
    }

    public void clear() {
        priorityQueue.clear();
    }

    public Object[] toArray() {
        return priorityQueue.toArray();
    }

    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    @Override
    public String toString() {
        return "MyPriorityBlockingQueue{" +
                "priorityQueue=" + priorityQueue +
                '}';
    }


}