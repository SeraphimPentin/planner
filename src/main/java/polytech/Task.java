package polytech;

import polytech.enums.BaseStates;
import polytech.enums.Priority;
import polytech.enums.TypeTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class Task extends RecursiveAction implements SuspendableTask {
    private final String taskName;
    private final Queue<ForkJoinTask<?>> highPriorityTasks = new ConcurrentLinkedDeque<>();

    private TypeTask type;


    private Priority priority;

    private BaseStates baseStates = BaseStates.SUSPENDED;

    public Task(String taskName,TypeTask typeTask,  Priority priority) {
        this.taskName = taskName;
        this.type = typeTask;
        this.priority = priority;
    }


    @Override
    protected void compute() {
        baseStates = BaseStates.RUNNING;
        for (int progress = 0; progress < 100; progress += 10) {
            preemptIfNeeded();
            System.out.printf("%s: Progress %s%%\n", taskName, progress);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Progress 100%. Task done");
        baseStates = BaseStates.RUNNING;
    }

    private void preemptIfNeeded() {
        if (highPriorityTasks != null) {
            System.out.printf("%s: I have to do join()\n", taskName);
            baseStates = BaseStates.READY;
            highPriorityTasks.remove().join();
        }
        baseStates = BaseStates.RUNNING;
    }

    public String getTaskName() {
        return taskName;
    }
    public Priority getPriority() {
        return priority;
    }

    public TypeTask getType() {
        return type;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public void addHighPriorityTask(Task task) {
        highPriorityTasks.add(task);
    }
}
