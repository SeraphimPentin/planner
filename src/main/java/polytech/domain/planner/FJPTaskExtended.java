package polytech.domain.planner;

import polytech.domain.Task;
import polytech.enums.TaskState;

import java.util.Queue;
import java.util.concurrent.ForkJoinTask;

public class FJPTaskExtended extends FJPTask {


    private volatile ForkJoinTask event;

    public FJPTaskExtended(Task task, Queue<FJPTask> highPriorityTasks) {
        super(task, highPriorityTasks);
    }

    @Override
    protected void preemptIfNeeded() {
        if (event != null) {
            state = TaskState.WAITING;
            event.join();
            state = TaskState.READY;
        }
        super.preemptIfNeeded();
    }

    public void setEvent(ForkJoinTask event) {
        this.event = event;
    }

}
