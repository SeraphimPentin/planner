package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TypeTask;

public interface ITask extends SuspendableTask{

    Priority priority();

    TypeTask type();

    void doWork();
}
