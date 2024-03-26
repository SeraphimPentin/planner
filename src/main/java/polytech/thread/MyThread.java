package polytech.thread;

import polytech.enums.Priority;


public class MyThread extends Thread {

    private final Priority priority;

    MyThread(String name, Priority priority){
        super(name);
        this.priority = priority;
    }


    public Integer getMyPriority() {
        return priority.getValue();
    }

    @Override
    public void run() {
        try {
            startThreadWithPriority(priority);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startThreadWithPriority(Priority priority) throws InterruptedException {
        if (priority == Priority.LOWEST) {
            Thread.sleep(200);
        }
        if (priority == Priority.LOW) {
            Thread.sleep(400);
        }
        if (priority == Priority.MIDDLE) {
            Thread.sleep(600);
        }
        if (priority == Priority.HIGH) {
            Thread.sleep(800);
        }
    }
}



