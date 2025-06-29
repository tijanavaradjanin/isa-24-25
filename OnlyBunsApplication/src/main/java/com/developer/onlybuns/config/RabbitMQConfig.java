package com.developer.onlybuns.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "objavaExchange";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME, false, false);
    }

    @Bean
    public Queue agencija1Queue() {
        return new Queue("agencija1Queue");
    }

    @Bean
    public Queue agencija2Queue() {
        return new Queue("agencija2Queue");
    }

    @Bean
    public Binding bindAgencija1(FanoutExchange fanoutExchange, Queue agencija1Queue) {
        return BindingBuilder.bind(agencija1Queue).to(fanoutExchange);
    }

    @Bean
    public Binding bindAgencija2(FanoutExchange fanoutExchange, Queue agencija2Queue) {
        return BindingBuilder.bind(agencija2Queue).to(fanoutExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

