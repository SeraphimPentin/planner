package polytech.domain;

import polytech.enums.Priority;
import polytech.enums.TypeTask;

public interface ITask extends IterativeTask {

    Priority priority();

    TypeTask type();

}
