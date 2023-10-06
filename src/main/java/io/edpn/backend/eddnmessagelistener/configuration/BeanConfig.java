package io.edpn.backend.eddnmessagelistener.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.eddnmessagelistener.domain.KafkaStreamProperties;
import io.edpn.backend.eddnmessagelistener.infrastructure.kafka.EventRouterStreamProcessor;
import io.edpn.backend.eddnmessagelistener.infrastructure.kafka.KafkaTopicHandler;
import io.edpn.backend.eddnmessagelistener.infrastructure.zmq.EddnMessageHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public EddnMessageHandler eddnMessageHandler(@Qualifier("eddnTaskExecutor") TaskExecutor taskExecutor,
                                                 @Qualifier("eddnRetryTemplate") RetryTemplate retryTemplate,
                                                 ObjectMapper objectMapper,
                                                 KafkaTopicHandler kafkaTopicHandler,
                                                 KafkaTemplate<String, JsonNode> jsonNodekafkaTemplate) {
        return new EddnMessageHandler(taskExecutor, retryTemplate, objectMapper, kafkaTopicHandler, jsonNodekafkaTemplate);
    }


    @Bean
    public EventRouterStreamProcessor eventRouterStreamProcessor(
            KafkaStreamProperties kafkaStreamProperties,
            KafkaTopicHandler kafkaTopicHandler,
            ObjectMapper objectMapper) {
        return new EventRouterStreamProcessor(kafkaStreamProperties, kafkaTopicHandler, objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
