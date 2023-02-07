package dev.tanpn.amqp.listener;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

import dev.tanpn.utils.message.DbOperationMsg;

@RabbitListener(queues = { "${mq.rabbitmq.db.task.queue}" })
public class DbTaskQueueListener {
    private final AtomicInteger COUNTER = new AtomicInteger(0);

    public DbTaskQueueListener() {
    }

    @RabbitHandler
    public void receive(final DbOperationMsg msg) throws InterruptedException {
        System.out.println("Received " + msg.getOperation().getValue());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if (COUNTER.incrementAndGet() > 10) {
            waiting();
            COUNTER.set(0);
        }

        stopWatch.stop();

        System.out.println("Consumer Done in " + stopWatch.getTotalTimeSeconds() + "s");
    }

    private void waiting() throws InterruptedException {
        Thread.sleep(10000);
    }

    // @PostConstruct
    // public void init() {
    // this.start();
    // }

    // @Override
    // public void run() {
    // while (true) {
    // try {
    // Thread.sleep(10000);
    // COUNTER.set(0);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }

}
