package com.xjxxxc.rabbitmqdemo;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @author: Jason Xu
 * @Date: 2019年3月30日
 * @description:  测试RabbitMQ发送消息的Controller
 */
@RestController
public class SendController implements RabbitTemplate.ConfirmCallback {
    private RabbitTemplate rabbitTemplate;

    /**
     * @Title: SendController
     * @Description:  配置发送消息的rabbitTemplate，因为是构造方法，所以不用注解Spring也会自动注入。
     * @param rabbitTemplate
     */
    public SendController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //设置消费回调
        this.rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * @Title: send1
     * @Description: 向消息队列1中发送消息
     * @param msg
     * @return String
     */
    @RequestMapping("send1")
    public String send1(String msg) {
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTINGKEY1, msg, correlationId);
        return null;
    }

    /**
     * @Title: send2
     * @Description: 向消息队列2中发送消息
     * @param msg
     * @return String
     */
    @RequestMapping("send2")
    public String send2(String msg) {
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTINGKEY2, msg, correlationId);
        return null;
    }

    /**
     * @Title: confirm
     * @Description: 消息的回调，主要是实现RabbitTemplate.ConfirmCallback接口
     * 注意，消息回调只能代表消息发送成功，不能代表消息被成功处理
     * @param correlationData
     * @param ack
     * @param cause
     */
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause + "\n重新发送");

        }
    }
}
