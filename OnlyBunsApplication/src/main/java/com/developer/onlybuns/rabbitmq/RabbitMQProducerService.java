package com.developer.onlybuns.rabbitmq;

import com.developer.onlybuns.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void posaljiPorukuReklamnimAgencijama(String poruka) {
        System.out.println("Saljem poruku: " + poruka);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "", poruka);
    }
}
