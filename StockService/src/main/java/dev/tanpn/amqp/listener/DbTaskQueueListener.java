package dev.tanpn.amqp.listener;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

import dev.tanpn.utils.message.DbOperationMsg;

@RabbitListener(queues = { "${mq.rabbitmq.db.task.queue}" })
public class DbTaskQueueListener extends Thread {
	private final int MAX_RUNNING_TASKS = 10;

	private volatile Queue<DbOperationMsg> mvTaskQueue = new ConcurrentLinkedQueue<DbOperationMsg>();

	public DbTaskQueueListener() {
	}

	@RabbitHandler
	public void receive(final DbOperationMsg msg) throws InterruptedException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		System.out.println("Received " + msg.getOperation().getValue() + ": " + msg.getHeaders()[0].toString());

		if (mvTaskQueue.size() < MAX_RUNNING_TASKS) {
			mvTaskQueue.add(msg);
		} else {
			// wait for queue is available
			while(true) {
				Thread.sleep(1000);
				if (mvTaskQueue.size() < MAX_RUNNING_TASKS) {
					break;
				}
			}
		}

		stopWatch.stop();

		System.out.println("Task " + msg.getHeaders()[0] + " waited " + stopWatch.getTotalTimeSeconds() + "(s) to added to queue");
	}

	@PostConstruct
	public void init() {
		this.start();
		
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//            	System.out.println("*** shutting down since JVM is shutting down");
//                
//            }
//        });
	}

	@Override
	public void run() {
		while (true) {
			try {
				DbOperationMsg msg = mvTaskQueue.poll();
				if (msg != null) {
					System.out.println("	Starting task...." + msg.getHeaders()[0]);
					Thread.sleep(5000);
					System.out.println("	End task...." + msg.getHeaders()[0]);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
