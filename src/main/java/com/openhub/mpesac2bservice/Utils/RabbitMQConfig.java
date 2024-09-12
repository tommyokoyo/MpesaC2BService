package com.openhub.mpesac2bservice.Utils;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String TRANSACTION_QUEUE = "mpesaTransactions";
    public static final String CALLBACK_QUEUE = "mpesaCallbacks";
    public static final String EXCHANGE = "mpesaExchange";

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE, false);
    }

    @Bean
    public Queue callbackQueue() {
        return new Queue(CALLBACK_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
         return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange exchange) {
        return BindingBuilder.bind(transactionQueue).to(exchange).with(TRANSACTION_QUEUE);
    }

    @Bean
    public Binding CallbackBinding(Queue callbackQueue, TopicExchange exchange) {
        return BindingBuilder.bind(callbackQueue).to(exchange).with(CALLBACK_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
