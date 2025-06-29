package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

public class ReklamnaAgencijaApp {

    private static final String EXCHANGE_NAME = "objavaExchange";

    public static void main(String[] args) throws Exception {

        // 1. Kreiramo konekciju ka RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Deklarisemo fanout exchange — ne mora biti durable (false, false)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, false);

        //Kreiramo anonimni (privremeni) queue
        String queueName = channel.queueDeclare().getQueue();

        //Bindujemo queue na exchange
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Čekam poruke. Za izlaz pritisni CTRL+C");

        ObjectMapper objectMapper = new ObjectMapper();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Primljena poruka:");
            System.out.println(message);
        };

        //Startujemo slusanje
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
