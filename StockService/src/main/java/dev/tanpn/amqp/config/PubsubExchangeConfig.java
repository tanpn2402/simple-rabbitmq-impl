package dev.tanpn.amqp.config;

    
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubsubExchangeConfig {
    // pub/sub
    @Value("${mq.rabbitmq.pubsub.exchange}")
    String pubsubExchangeName;

    @Value("${mq.rabbitmq.pubsub.queue}")
    String pubsubQueueName;

    @Bean
    public FanoutExchange pubsubExchange() {
        return new FanoutExchange(pubsubExchangeName);
    }

    @Bean
    public Queue pubsubQueue() {
        return new Queue(pubsubQueueName);
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder
                .bind(pubsubQueue())
                .to(pubsubExchange());
    }
}
