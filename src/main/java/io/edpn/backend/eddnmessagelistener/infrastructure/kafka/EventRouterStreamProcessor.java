package io.edpn.backend.eddnmessagelistener.infrastructure.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.eddnmessagelistener.domain.KafkaStreamProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class EventRouterStreamProcessor {

    public static final String INPUT_TOPIC = "https___eddn.edcd.io_schemas_journal_1";
    private final KafkaStreamProperties kafkaStreamProperties;
    private final KafkaTopicHandler kafkaTopicHandler;
    private final ObjectMapper objectMapper;
    private KafkaStreams streams;

    @PostConstruct
    public void init() {
        log.info("starting 'EventRouterStreamProcessor'");
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> journalStream = builder.stream(INPUT_TOPIC);
        journalStream.to((key, value, recordContext) -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(value);
                        String targetTopic = Optional.ofNullable(rootNode.get("message").get("event"))
                                .map(JsonNode::asText)
                                .map(String::toLowerCase)
                                .map(eventValue -> INPUT_TOPIC + "_" + eventValue)
                                .orElse(INPUT_TOPIC + "_unknown-journal-event");

                        //create topics as needed
                        kafkaTopicHandler.createTopicIfNotExists(targetTopic)
                                .whenComplete((topicName, kafkaTopicCreateException) -> {
                                    if (Objects.nonNull(kafkaTopicCreateException)) {
                                        //TODO handle kafka exception
                                        log.error("could not create topic on Kafka", kafkaTopicCreateException);
                                        throw new RuntimeException(kafkaTopicCreateException.getMessage());
                                    }
                                });
                        return targetTopic;
                    } catch (Exception e) {
                        log.error("could not resolve event value when stream processing the journal events", e);
                        throw new RuntimeException(e);
                    }
                },
                Produced.with(Serdes.String(), Serdes.String()));

        streams = new KafkaStreams(builder.build(), kafkaStreamProperties.createStreamProperties());
        streams.start();
        log.info("'EventRouterStreamProcessor' started");
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down 'EventRouterStreamProcessor'");

        if (streams != null) {
            streams.close();
            log.info("'EventRouterStreamProcessor' closed");
        }
    }
}
