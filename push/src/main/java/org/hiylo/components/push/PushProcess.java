/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: PushProcess.java
 * Data: 4/17/18 11:01 AM
 * Author: hiylo
 */

package org.hiylo.components.push;

import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hiylo.components.push.annotation.EnablePushMessageProcess;
import org.hiylo.components.push.api.PushProcessor;
import org.hiylo.components.push.vo.PushMessage;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;

@Component
@Configuration
public class PushProcess implements ApplicationContextAware {
    private ApplicationContext context;
    private Gson gson = new Gson();
    @Autowired
    private AliyunPush aliyunPush;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map map = context.getBeansWithAnnotation(EnablePushMessageProcess.class);
        System.out.println(map.size());
        if (map.size() > 0) {
            System.out.println("初始化");
            Map<String, PushProcessor> clazzs = context.getBeansOfType(PushProcessor.class);
            System.out.println(clazzs.size());
            Iterator<String> keys = clazzs.keySet().iterator();
            while (keys.hasNext()) {
                System.out.println("接收");
                String key = keys.next();
                PushProcessor pushProcessor = clazzs.get(key);
                CachingConnectionFactory connectionFactory = (CachingConnectionFactory)context.getBean(ConnectionFactory.class);
                SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
                container.setQueues(new Queue("pushQueue"));
                container.setExposeListenerChannel(true);
                container.setMaxConcurrentConsumers(1);
                container.setConcurrentConsumers(1);
                container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                container.setChannelTransacted(true);
                container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
                    channel.txSelect();
                    byte[] body = message.getBody();
                    PushMessage pushMessage = gson.fromJson(new String(body), TypeToken.get(PushMessage.class).getType());
                    try {
                        if (pushMessage.getEmployerCode() != null && !"".equals(pushMessage.getEmployerCode())) {
                            // 推送给雇主
                            aliyunPush.advancedPush(AliyunPush.PushPlateform.all, AliyunPush.PushType.notification, AliyunPush.PushTarget.ACCOUNT, pushMessage.getOpenType(), pushMessage.getOpenValue(), pushMessage.getEmployerCode(), pushMessage.getTitle(), pushMessage.getContent(), pushMessage.getExt(), AliyunPush.ENV_PRODUCE);
                        } else if (pushMessage.getHelperCode() != null && !"".equals(pushMessage.getHelperCode())) {
                            // 推送给雇工
                            aliyunPush.advancedPush(AliyunPush.PushPlateform.all, AliyunPush.PushType.notification, AliyunPush.PushTarget.ACCOUNT, pushMessage.getOpenType(), pushMessage.getOpenValue(), pushMessage.getHelperCode(), pushMessage.getTitle(), pushMessage.getContent(), pushMessage.getExt(), AliyunPush.ENV_PRODUCE);
                        } else {
                            // TODO 推送给雇主端还是雇工端
                            // 推送给所有
                            aliyunPush.advancedPush(AliyunPush.PushPlateform.all, AliyunPush.PushType.notification, AliyunPush.PushTarget.ALL, pushMessage.getOpenType(), pushMessage.getOpenValue(), "", pushMessage.getTitle(), pushMessage.getContent(), pushMessage.getExt(), AliyunPush.ENV_PRODUCE);
                        }
                        // PushPlateform pushPlateform, PushType pushType, PushTarget pushTarget, OpenType openType, String openValue, String targetValue, String title, String body, Map<String, String> ext, String env
                        // 将推送消息发给处理类, 为了将推送消息保存到数据库中.
                        pushMessage.setSuccess(true);
                        pushProcessor.process(pushMessage);
//                        System.out.println(message.getMessageProperties().getDeliveryTag());
//                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
                        channel.txCommit();
                    } catch (ClientException e) {
//                        e.printStackTrace();
                        pushMessage.setSuccess(false);
                        pushProcessor.process(pushMessage);
                        channel.txCommit();
                    }
                    if(pushMessage.isSuccess()){
                        // TODO 推送失败处理
                    }
                });
                BeanDefinitionBuilder beanDefinitionBuilder =
                        BeanDefinitionBuilder.genericBeanDefinition();
                // get the BeanDefinition
                BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                ConfigurableListableBeanFactory configurableListableBeanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
                configurableListableBeanFactory.registerSingleton("simpleMessageListenerContainer", container);
            }
        }
    }
}
