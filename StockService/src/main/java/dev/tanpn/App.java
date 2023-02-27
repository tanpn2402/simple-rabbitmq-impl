package dev.tanpn;

import java.util.logging.Logger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import dev.tanpn.utils.enums.DbOperationType;
import dev.tanpn.utils.message.DbOperationMsg;

@SpringBootApplication
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            logger.info("Initiated total " + beanNames.length + " beans!");
            // java.util.Arrays.sort(beanNames);
            // for (String beanName : beanNames) {
            // System.out.println(beanName);
            // }
            
            AmqpTemplate amqpTemplate = (AmqpTemplate) ctx.getBean("rabbitJsonTemplate");
            if (amqpTemplate instanceof RabbitTemplate) {
                RabbitTemplate mvAmqpTemplate = (RabbitTemplate) amqpTemplate;
                
                for (int i = 0; i< 100; i++) {
                    mvAmqpTemplate.convertAndSend("db.task.queue", new DbOperationMsg(DbOperationType.Q, new String[] {String.valueOf(i)}));
                }
            }
        };
    }
}
