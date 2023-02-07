package dev.tanpn.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.tanpn.entity.OrderEntity;
import dev.tanpn.repositories.OrderRepository;
import dev.tanpn.utils.message.ResponseMsg;
import dev.tanpn.utils.tags.TagName;

@Service(TagName.B_ENTERORDER)
@Lazy
public class EnterOrder implements BaseHandler<String, String> {
    private OrderRepository mvOrderRepository;
    private RabbitTemplate mvAmqpTemplate;

    @Value("${mq.rabbitmq.pubsub.exchange}")
    String pubsubExchangeName;

    @Autowired
    public EnterOrder(OrderRepository orderRepository,
            @Qualifier("rabbitJsonTemplate") AmqpTemplate amqpTemplate) {

        this.mvOrderRepository = orderRepository;
        if (amqpTemplate instanceof RabbitTemplate) {
            this.mvAmqpTemplate = (RabbitTemplate) amqpTemplate;
        }
    }

    private void notifyOrderUpdate(OrderEntity value) {
        ResponseMsg<String> lvResponseMsg = new ResponseMsg<>();
        lvResponseMsg.setSuccess(true);
        lvResponseMsg.setMessage("");
        lvResponseMsg.setType("ORDER_UPDATE");
        Map<String, String> lvOrderDetail = new HashMap<>();
        lvOrderDetail.put(TagName.ORDERID, value.getId().toString());
        lvOrderDetail.put(TagName.CLIENTID, value.getClientID());
        lvOrderDetail.put(TagName.QTY, String.valueOf(value.getQty()));
        lvOrderDetail.put(TagName.PRICE, String.valueOf(value.getPrice()));
        lvOrderDetail.put(TagName.STOCKID, value.getStockID());
        lvOrderDetail.put(TagName.MARKETID, value.getMarketID());
        lvOrderDetail.put(TagName.STATUS, value.getStatus().toString());
        lvResponseMsg.setBody(lvOrderDetail);
        this.mvAmqpTemplate.convertAndSend(pubsubExchangeName, "", lvResponseMsg);
    }

    @Override
    public Map<String, String> doExecute(Map<String, String> pMessage) {
        OrderEntity lvOrder = new OrderEntity();
        lvOrder.setMarketID(pMessage.get(TagName.MARKETID));
        lvOrder.setStockID(pMessage.get(TagName.STOCKID));
        lvOrder.setPrice(Double.parseDouble(pMessage.get(TagName.PRICE)));
        lvOrder.setQty(Integer.parseInt(pMessage.get(TagName.QTY)));
        lvOrder.setClientID(pMessage.get(TagName.CLIENTID));
        lvOrder.setStatus("PENDING");
        OrderEntity lvInsertedOrder = mvOrderRepository.save(lvOrder);

        Map<String, String> lvResultMap = new HashMap<>();
        lvResultMap.put(TagName.ORDERID, lvInsertedOrder.getId().toString());
        this.notifyOrderUpdate(lvInsertedOrder);

        return lvResultMap;
    }
}
