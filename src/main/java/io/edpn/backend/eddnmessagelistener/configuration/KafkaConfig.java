package io.edpn.backend.eddnmessagelistener.configuration;

import io.edpn.backend.eddnmessagelistener.domain.KafkaStreamProperties;
import io.edpn.backend.eddnmessagelistener.infrastructure.kafka.KafkaTopicHandler;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public AdminClient kafkaAdminClient(@Value(value = "${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return AdminClient.create(configs);
    }

    @Bean(name = "eventRouterKafkaStreamProperties")
    public KafkaStreamProperties kafkaStreamProperties(@Value(value = "${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new KafkaStreamProperties(bootstrapServers, "journal-event-router-app");
    }

    @Bean
    public KafkaTopicHandler kafkaTopicCreator(AdminClient adminClient,
                                               @Value(value = "${spring.kafka.topic.partitions:1}") final int topicPartitions,
                                               @Value(value = "${spring.kafka.topic.replicationfactor:1}") final short topicReplicationFactor) {
        return new KafkaTopicHandler(adminClient, topicPartitions, topicReplicationFactor);
    }
}
