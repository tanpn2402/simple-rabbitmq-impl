package dev.tanpn.amqp.listener;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.MessageProperties;

import dev.tanpn.handler.BaseHandler;
import dev.tanpn.utils.message.RequestMsg;
import dev.tanpn.utils.message.ResponseMsg;

@Service
public class RabbitMQListener {
    private static Logger LOGGER = Logger.getLogger(RabbitMQListener.class.getName());

    private RabbitTemplate mvAmqpTemplate;
    private ApplicationContext mvApplicationContext;

    @Autowired
    private Environment env;

    @Autowired
    public void context(ApplicationContext pApplicationContext) {
        this.mvApplicationContext = pApplicationContext;
    }

    private void doResponse(String senderId, String correlationId, ResponseMsg msg) {
        // ref:
        // https://dirask.com/posts/Spring-Boot-2-receive-response-message-for-the-request-message-using-RabbitMQ-RPC-DlkNW1
        this.mvAmqpTemplate.convertAndSend(senderId, msg, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }

    @Autowired
    public RabbitMQListener(@Qualifier("rabbitJsonTemplate") AmqpTemplate amqpTemplate) {
        if (amqpTemplate instanceof RabbitTemplate) {
            this.mvAmqpTemplate = (RabbitTemplate) amqpTemplate;
        }
    }

    /**
     * @param senderId      : the request message producer
     * @param correlationId : proper correlation id parameter in the response
     *                      message to let the producer know on what request message
     *                      reply is sent
     * @param msg
     */
    @RabbitListener(queues = { "${mq.rabbitmq.queue.json}" })
    public void consumeJsonMessage(
            @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
            @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId,
            RequestMsg<Object> msg) {
        LOGGER.info(String.format("Receive message %s: Sender: %s | correlationId: %s", msg.getClass().getName(),
                msg.toString(), senderId, correlationId));
        LOGGER.info(String.format("Received message -> %s, %s", msg.getType(), msg.getBody().toString()));

        final String lvBeanName = env.getProperty("app.action.to.bean." + msg.getType());
        LOGGER.info("BEAN " + lvBeanName);
        ResponseMsg lvResponseMsg = new ResponseMsg<>(true, "", msg.getType(), null);
        if (lvBeanName != null && lvBeanName != "") {
            BaseHandler lvBaseHandler = (BaseHandler) this.mvApplicationContext.getBean(lvBeanName);
            if (lvBaseHandler != null) {
                Map lvResult = lvBaseHandler.doExecute(msg.getBody());
                lvResponseMsg.setBody(lvResult);
            } else {
                lvResponseMsg.setSuccess(false);
                lvResponseMsg.setMessage("Invalid handler");
            }
        } else {
            lvResponseMsg.setSuccess(false);
            lvResponseMsg.setMessage("Invalid handler");
        }
        doResponse(senderId, correlationId, lvResponseMsg);
    }
}