package dev.tanpn.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.tanpn.entity.OrderEntity;
import dev.tanpn.repositories.OrderRepository;
import dev.tanpn.utils.tags.TagName;

@Service(TagName.B_ENTERORDER)
@Lazy
public class EnterOrder implements BaseHandler<String, String> {
    private OrderRepository mvOrderRepository;

    @Autowired
    public EnterOrder(OrderRepository orderRepository) {
        this.mvOrderRepository = orderRepository;
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
        return lvResultMap;
    }
}
