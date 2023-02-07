package dev.tanpn.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RPCExchangeConfig {
    @Value("${mq.rabbitmq.queue}")
    String queueName;

    @Value("${mq.rabbitmq.queue.json}")
    String queueNameJson;

    @Value("${mq.rabbitmq.exchange}")
    String exchange;

    @Value("${mq.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${mq.rabbitmq.routingkey.json}")
    private String routingkeyJson;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public Queue jsonQueue() {
        return new Queue(queueNameJson);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(directExchange())
                .with(routingkey);
    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder
                .bind(jsonQueue())
                .to(directExchange())
                .with(routingkeyJson);
    }
}
