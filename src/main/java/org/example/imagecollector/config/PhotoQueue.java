package org.example.imagecollector.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotoQueue {
    private final static String QUEUE_PHOTOS = "photos";
    @Bean
    public Queue photos() {
        return new Queue(QUEUE_PHOTOS, true);
    }
}
