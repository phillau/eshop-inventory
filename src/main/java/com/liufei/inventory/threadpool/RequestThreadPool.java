package com.liufei.inventory.threadpool;

import com.liufei.inventory.request.Request;
import com.liufei.inventory.request.RequestQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestThreadPool {

    public static ExecutorService getInstance() {
        return InnerRequestThreadPool.threadPoolExecutor;
    }

    private RequestThreadPool() {
    }

    private static class InnerRequestThreadPool {
        private static ExecutorService threadPoolExecutor;

        /*private static int queuesNum;
        @Value("${eshop.inventory.queues-number}")
        private int queuesNumTemp;
        @PostConstruct
        public void initQueuesNum(){
            queuesNum = queuesNumTemp;
        }

        private static int queueSize;
        @Value("${eshop.inventory.queue-size}")
        public void setQueueSize(int queueSize) {
            InnerRequestThreadPool.queueSize = queueSize;
        }*/

        static {
            threadPoolExecutor = new ThreadPoolExecutor(
                    10,
                    10,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new DefaultThreadFactory());

            List<ArrayBlockingQueue<Request>> queues = RequestQueue.getInstance();
            for (int i = 0; i < 8; i++) {
                ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
                queues.add(queue);
                threadPoolExecutor.submit(new RequestThread(queue));
            }
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "request-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
