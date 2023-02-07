package dev.tanpn;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigure {
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

    // Create and binding topics
    @Bean
    public Declarables topicBindings() {
        Queue simpleTopic = new Queue(queueName, false);
        Queue jsonTopic = new Queue(queueNameJson, false);

        DirectExchange topicExchange = new DirectExchange(exchange);

        return new Declarables(
        simpleTopic,
        jsonTopic,
        topicExchange,
        BindingBuilder
            .bind(simpleTopic)
            .to(topicExchange)
            .with(routingkey)
            ,
        BindingBuilder
            .bind(jsonTopic)
            .to(topicExchange)
            .with(routingkeyJson))
        ;
    }

    // ---------------------------------------
    // Serialization and Deserialization
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	
	@Bean
	public AmqpTemplate rabbitJsonTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
    // ---------------------------------------
}