package dev.tanpn.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.tanpn.entity.OrderEntity;
import dev.tanpn.repositories.OrderRepository;
import dev.tanpn.utils.tags.TagName;

@Service(TagName.B_ORDERENQUIRY)
@Lazy
public class OrderEnquiry implements BaseHandler<List<Map<String, String>>, String> {
    private OrderRepository mvOrderRepository;

    @Autowired
    public OrderEnquiry(OrderRepository orderRepository) {
        this.mvOrderRepository = orderRepository;
    }
    
    @Override
    public Map<String, List<Map<String, String>>> doExecute(Map<String, String> pMessage) {
        List<Map<String, String>> lvOrders = new ArrayList<>();
        List<OrderEntity> lvAllOrders = this.mvOrderRepository.findAll();
        if (lvAllOrders.size() > 0) {
            lvAllOrders.forEach(value -> {
                Map<String, String> lvOrderDetail = new HashMap<>();
                lvOrderDetail.put(TagName.ORDERID, value.getId().toString());
                lvOrderDetail.put(TagName.CLIENTID, value.getClientID());
                lvOrderDetail.put(TagName.QTY, String.valueOf(value.getQty()));
                lvOrderDetail.put(TagName.PRICE, String.valueOf(value.getPrice()));
                lvOrderDetail.put(TagName.STOCKID, value.getStockID());
                lvOrderDetail.put(TagName.MARKETID, value.getMarketID());
                lvOrderDetail.put(TagName.STATUS, value.getStatus().toString());
                lvOrders.add(lvOrderDetail);
            });
        }

        Map<String, List<Map<String, String>>> lvResultMap = new HashMap<>();
        lvResultMap.put(TagName.LIST, lvOrders);
        return lvResultMap;
    }
}
