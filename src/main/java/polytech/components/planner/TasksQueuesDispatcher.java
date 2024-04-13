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

/**
 * Компонент, инкапсулирующий диспетчеризацию очередей задач.
 * Может для данной задачи дать:
 * <ol>
 *     <li>Очередь с задачами того же приоритета, что текущая задача</li>
 *     <li>
 *         Итератор по очередям задач большего приоритета, чем текущая задача.
 *         Порядок итерирования - от очередей с большим приоритетом к меньшим.
 *     </li>
 * </ol>
 * -
 * <p>
 * Хранит очереди для задач следующим списком:
 *  0     1     2    3
 * HIGH MIDDLE LOW LOWEST
 */
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
        int currentPriority = task.priority().getValue();
        int prioritiesCount = Priority.values().length;
        List<Queue<FJPTask>> prioritizedTasksQueues = tasksQueues.subList(currentPriority + 1, prioritiesCount);
        return new ReverseListIterator<>(prioritizedTasksQueues);
    }


}
