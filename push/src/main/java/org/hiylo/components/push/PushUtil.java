/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: PushUtil.java
 * Data: 4/11/18 4:36 PM
 * Author: hiylo
 */

package org.hiylo.components.push;

import com.google.gson.Gson;
import org.hiylo.components.push.vo.PushMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@Configuration
public class PushUtil implements RabbitTemplate.ConfirmCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private Gson gson = new Gson();

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Bean
    public Queue pushQueue() {
        return new Queue("pushQueue");
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("push");
    }

    public void sendToPush(PushMessage pushMessage) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend("pushQueue", (Object) gson.toJson(pushMessage), correlationId);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费:"+cause);
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }
}
