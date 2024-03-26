//package polytech;
//
//import polytech.enums.Priority;
//import polytech.enums.TypeTask;
//import polytech.model.MyPriorityBlockingQueue;
//
//import java.util.Random;
//
//public class Utils {
//    public MyPriorityBlockingQueue<TaskOld> crateTasks(){
//        Random random = new Random();
//        int value = random.nextInt(10);
//        MyPriorityBlockingQueue<Task> tasks = new MyPriorityBlockingQueue<>(value);
//
//        for (int i = 0; i <= value; i++){
//            tasks.add(new TaskOld("Task "+ i,
//                    Priority.values()[random.nextInt(4)],
//                    TypeTask.values()[random.nextInt(2)]));
//        }
//        return tasks;
//    }
//}
