package org.example.config;

import org.springframework.amqp.core.*;
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
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue(); // privremeni queue
    }

    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanoutExchange);
    }
}

