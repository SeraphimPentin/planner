//package polytech.oldVersion;
//
//
//import java.util.concurrent.*;
//
//public class ConcurrencyShit {
//    public static void main(String[] args) throws InterruptedException {
//        ForkJoinPool forkJoinPool = new ForkJoinPool(1);
//        Task lowPriorityTask = new Task("low");
//        Task highPriorityTask = new Task("high");
//
//        forkJoinPool.submit(lowPriorityTask);
//        TimeUnit.SECONDS.sleep(2);
//
//        //Мы хотим засабмитить приоритетную задачу
//        //Откуда-то мы знаем, что все потоки у пула заняты задачами с низким приоритетом
//        // и мы знаем ссылку на низкоприоритетную задачу
//        lowPriorityTask.addHighPriorityTask(highPriorityTask);
//        forkJoinPool.submit(highPriorityTask);
//
//        forkJoinPool.awaitTermination(1, TimeUnit.MINUTES);
//    }
//}
