package dev.tanpn.amqp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.tanpn.amqp.listener.DbTaskQueueListener;

@Configuration
public class TaskQueueExchange {

    @Value("${mq.rabbitmq.db.task.queue}")
    String dbTaskQueueName;

    @Bean
    public Queue dbTaskQueue() {
        return new Queue(dbTaskQueueName);
    }

    @Bean
	public DbTaskQueueListener dbTaskQueueListener() {
		return new DbTaskQueueListener();
	}
}
