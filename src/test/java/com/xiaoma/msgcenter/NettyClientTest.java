package com.xiaoma.msgcenter;

import com.xiaoma.msgcenter.common.model.RpcClassMap;
import com.xiaoma.msgcenter.common.pojo.Calculater;
import com.xiaoma.msgcenter.common.pool.ThreadPoolService;
import com.xiaoma.msgcenter.rpc.client.RpcClientBootstrap;
import com.xiaoma.msgcenter.rpc.client.core.MessageSendExcutor;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyClientTest {

    @Autowired
    RpcClientBootstrap clientBootstrap;

    @Autowired
    MessageSendExcutor sendExcutor;

    @Autowired
    RpcClassMap rpcClassMap;

    @Autowired
    ThreadPoolService threadPoolService;
    private Semaphore sigleSemaphore = new Semaphore(10000);


    int i = 0;
    @Test
    public void test() throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(10);

        StopWatch sw = new StopWatch();
        sw.start();
        for(; i < 2000; i++){
            sigleSemaphore.acquire();
            es.execute(new Runnable() {
                @Override
                public void run() {
                    RpcClientBootstrap clientBootstrap = new RpcClientBootstrap();
                    clientBootstrap.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Calculater calculater = sendExcutor.execute(Calculater.class);
                    System.out.println(calculater.add(i,i));
                    sigleSemaphore.release();
                }
            });
        }
        while(sigleSemaphore.availablePermits() != 10000) {

        }
        sw.stop();
        String tip = String.format(" RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);
        //while (true) {}
    }

    @Test
    public void testSingle() throws InterruptedException {
        clientBootstrap.start();
        Thread.sleep(100);
        Calculater calculater = sendExcutor.execute(Calculater.class);

        StopWatch sw = new StopWatch();
        sw.start();
        for(; i < 20; i++){
            System.out.println(calculater.add(i,i));
        }

        sw.stop();
        String tip = String.format(" RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);
        //while (true) {}
    }

    @Test
    public void task() {
        int parallel = 10000;

        for (int i = 0; i < 1; i++) {
            JdkParallelTask(sendExcutor, parallel);
//            KryoParallelTask(executor, parallel);
            //           HessianParallelTask(executor, parallel);
            System.out.printf("[author tangjie] Netty RPC Server 消息协议序列化第[%d]轮并发验证结束!\n\n", i);
        }
    }

    private void JdkParallelTask(MessageSendExcutor sendExcutor, int parallel) {

    }


}
