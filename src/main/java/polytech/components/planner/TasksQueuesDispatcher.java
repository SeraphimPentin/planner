package polytech.components.planner;

import org.apache.commons.collections4.iterators.ReverseListIterator;
import polytech.domain.Task;
import polytech.domain.planner.FJPTask;
import polytech.enums.Priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TasksQueuesDispatcher {
    private final List<Queue<FJPTask>> tasksQueues = new ArrayList<>();

    {
        for (int i = 0; i < Priority.values().length; i++) {
            tasksQueues.add(new ConcurrentLinkedQueue<>());
        }
    }

    public Queue<FJPTask> getSamePriorityTasksQueue(Task task) {
        return tasksQueues.get(task.priority().getValue());
    }

    public Iterator<Queue<FJPTask>> getPrioritizedTasksQueues(Task task) {
        int currentPriority = task.priority().getValue() + 1;
        int prioritiesCount = Priority.values().length;
        List<Queue<FJPTask>> prioritizedTasksQueues = tasksQueues.subList(currentPriority, prioritiesCount);
        return new ReverseListIterator<>(prioritizedTasksQueues);
    }


}
