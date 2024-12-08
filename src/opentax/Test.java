package opentax;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TaskGroup taskGroup1 = new TaskGroup(UUID.randomUUID());
        TaskGroup taskGroup2 = new TaskGroup(UUID.randomUUID());

        Callable<String> taskAction1 = ()->{
            Thread.sleep(2000);
            return "Task 1 result";
        };

        Callable<String> taskAction2 = ()->{
            Thread.sleep(2000);
            return "Task 2 result";
        };

        Callable<String> taskAction3 = ()->{
            Thread.sleep(2000);
            return "Task 3 result";
        };

        Callable<String> taskAction4 = ()->{
            Thread.sleep(2000);
            return "Task 4 result";
        };

        //creating tatal 4 task object and 2 task per group
        Task<String> task1 = new Task<>(UUID.randomUUID(), taskGroup1,TaskType.READ, taskAction1);
        Task<String> task2 = new Task<>(UUID.randomUUID(), taskGroup1,TaskType.READ, taskAction2);
        Task<String> task3 = new Task<>(UUID.randomUUID(), taskGroup2,TaskType.WRITE, taskAction3);
        Task<String> task4 = new Task<>(UUID.randomUUID(), taskGroup2,TaskType.WRITE, taskAction4);

        // Creating task executor service object with parameter as number of threads
        TaskExecutor taskExecutor = new TaskExecutorService(2);

        //Submitting the task to executor for processing
        Future<String> f1 = taskExecutor.submitTask(task1);
        Future<String> f2 = taskExecutor.submitTask(task2);
        Future<String> f3 = taskExecutor.submitTask(task3);
        Future<String> f4 = taskExecutor.submitTask(task4);

        System.out.println("==========Main thread is not blocked============== "+ Thread.currentThread().getName());
        
        System.out.println("Task1 result: "+f1.get());
        System.out.println("Task2 result: "+f2.get());
        System.out.println("Task3 result: "+f3.get());
        System.out.println("Task4 result: "+f4.get());

        System.out.println("===========Shuting down TaskExecutor Service==============");
        taskExecutor.shutdown();
    }
}
