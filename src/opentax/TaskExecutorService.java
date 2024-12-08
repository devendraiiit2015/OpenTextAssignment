package opentax;

import java.util.concurrent.*;

public class TaskExecutorService implements TaskExecutor{
    private final BlockingQueue<FutureTask> taskQueue;
    private volatile boolean isShutdown = false;

    public TaskExecutorService(int numThreads) {
        this.taskQueue = new LinkedBlockingQueue<>();
        for(int i=0;i<numThreads;i++){
            new Thread(()->{
               while ((!isShutdown || !taskQueue.isEmpty())) {
                   try {
                       Runnable runnable= taskQueue.poll(1, TimeUnit.SECONDS);
                       if(runnable!=null) {
                           FutureTaskExtended ft = ((FutureTaskExtended<?>)runnable);
                           TaskGroup taskGroup = ft.taskGroup();
                           synchronized (taskGroup){
                               System.out.printf("Task started,  UUID: +%s  %n", ft.uuid);
                               runnable.run();
                           }
                           System.out.printf("Task Finished,  UUID:+%s %n", ft.uuid);
                       }
                   } catch (InterruptedException e) {
                       Thread.currentThread().interrupt();
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
               }
           }).start();
        }
    }

    @Override
    public <T> Future<T> submitTask(Task<T> task) throws InterruptedException {
        FutureTaskExtended<T> futureTask = new FutureTaskExtended<T>(task);
        taskQueue.offer(futureTask);
        return futureTask;
    }


    public void shutdown() {
        isShutdown = true;
    }
}






