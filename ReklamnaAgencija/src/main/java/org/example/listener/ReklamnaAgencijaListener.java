package org.example.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReklamnaAgencijaListener {

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void handleMessage(String message) {
        System.out.println("Primljena poruka: " + message);
    }
}
