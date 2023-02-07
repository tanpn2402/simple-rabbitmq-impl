package dev.tanpn.amqp.listener;

import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import dev.tanpn.utils.message.ResponseMsg;

@Service
public class PubsubListener {
    private static Logger LOGGER = Logger.getLogger(PubsubListener.class.getName());

    @RabbitListener(queues = { "${mq.rabbitmq.pubsub.queue}" })
    public void receive2(ResponseMsg<Object> msg) {
        LOGGER.info("Receive new message " + msg.getClass().getName());
        LOGGER.info("  --> " + msg.getBody().toString());
    }
}
