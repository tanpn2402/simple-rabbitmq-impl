package dev.tanpn.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.tanpn.utils.message.RequestMsg;
import dev.tanpn.utils.message.ResponseMsg;
import dev.tanpn.utils.tags.TagName;

@Service(TagName.B_ORDERENQUIRY)
public class OrderEnquiry implements BaseAction<Object, String> {

    private RabbitTemplate amqpTemplate;

    @Value("${mq.rabbitmq.exchange}")
    String exchange;

    @Value("${mq.rabbitmq.routingkey.json}")
    private String routingkey;

    @Autowired
    public OrderEnquiry(@Qualifier("rabbitJsonTemplate") AmqpTemplate amqpTemplate) {
        if (amqpTemplate instanceof RabbitTemplate) {
            this.amqpTemplate = (RabbitTemplate) amqpTemplate;
        }
    }

    @Override
    public Map<String, Object> doExecute(Map<String, String> pMessage) {
        Map<String, Object> lvResponse = new HashMap<>();
        RequestMsg<String> lvRequestMsg = new RequestMsg<>(TagName.B_ORDERENQUIRY, pMessage);
        ResponseMsg<List<Map<String, String>>> lvResult = (ResponseMsg<List<Map<String, String>>>) amqpTemplate.convertSendAndReceive(exchange, routingkey, lvRequestMsg);
        lvResponse.put(TagName.SUCCESS, lvResult.isSuccess() ? "Y" : "N");
        lvResponse.put(TagName.MESSAGE, lvResult.getMessage());
        lvResponse.put(TagName.LIST, lvResult.getBody());

        return lvResponse;
    }
}
