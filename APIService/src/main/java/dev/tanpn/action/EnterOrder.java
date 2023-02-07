package dev.tanpn.action;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.tanpn.utils.message.RequestMsg;
import dev.tanpn.utils.message.ResponseMsg;
import dev.tanpn.utils.tags.TagName;

@Service(TagName.B_ENTERORDER)
@Lazy
public class EnterOrder implements BaseAction<String, String> {
    private static Logger LOGGER = Logger.getLogger(EnterOrder.class.getName());

    private RabbitTemplate amqpTemplate;

    @Value("${mq.rabbitmq.exchange}")
    String exchange;

    @Value("${mq.rabbitmq.routingkey.json}")
    private String routingkey;

    @Autowired
    public EnterOrder(@Qualifier("rabbitJsonTemplate") AmqpTemplate amqpTemplate) {
        if (amqpTemplate instanceof RabbitTemplate) {
            this.amqpTemplate = (RabbitTemplate) amqpTemplate;
        }
    }

    @Override
    public Map<String, String> doExecute(Map<String, String> pMessage) {
        Map<String, String> lvResponse = new HashMap<>();
        RequestMsg<String> lvRequestMsg = new RequestMsg<>(TagName.B_ENTERORDER, pMessage);
        ResponseMsg<String> lvResult = (ResponseMsg<String>) amqpTemplate.convertSendAndReceive(exchange, routingkey, lvRequestMsg);
        lvResponse.put(TagName.SUCCESS, lvResult.isSuccess() ? "Y" : "N");
        lvResponse.put(TagName.MESSAGE, lvResult.getMessage());
        lvResponse.put(TagName.ORDERID, lvResult.getBody().getOrDefault(TagName.ORDERID, ""));

        return lvResponse;
    }
}