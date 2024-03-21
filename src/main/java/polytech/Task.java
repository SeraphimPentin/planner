package polytech;

import polytech.enums.Priority;
import polytech.enums.TypeTask;


public class Task {

    private final String name;
    private final Priority priority;
    private TypeTask type;

    public Task(String name, Priority priority, TypeTask type) {
        this.name = name;
        this.priority = priority;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public TypeTask getType() {
        return type;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", type=" + type +
                '}';
    }
}
