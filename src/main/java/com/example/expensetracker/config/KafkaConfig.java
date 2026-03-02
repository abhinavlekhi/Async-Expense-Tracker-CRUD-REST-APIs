package com.example.expensetracker.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka // This annotation is required to enable listener methods annotated with @KafkaListener
public class KafkaConfig {
    //Default Error Handler would come into play only if the listener method throws exception, the DLQ i.e
    // "expense-deleted-events-dlt" would be used only if all retry attempts are exhausted. Before message
    // is sent to DLQ, 3 retries would be attempted with interval of 2 secs between each retry.
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, exception) ->
                        new TopicPartition("expense-deleted-events-dlt", record.partition()));
        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(2000, 3)); // 3 retries with 2 secs interval
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        return errorHandler;
    }

    @Bean("dlqKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> dlqKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setCommonErrorHandler(new DefaultErrorHandler()); // No Retries for DLQ
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
