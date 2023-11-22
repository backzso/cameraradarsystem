package org.example.kafka;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.EnvironmentVariables;
import org.example.models.Tower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducer {
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private static KafkaProducer instance;
    private static org.apache.kafka.clients.producer.KafkaProducer<Object, String> producer;
    private static final Gson gson = new Gson();
    private static final String TOWER_POS = KafkaConstants.TOWER_POS;

    private KafkaProducer() {
    }

    public static KafkaProducer getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (KafkaProducer.class) {
                if (instance == null) {
                    // if instance is null, initialize
                    instance = new KafkaProducer();
                    producer = createProducer();
                }
            }
        }
        return instance;
    }

    private static org.apache.kafka.clients.producer.KafkaProducer<Object, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, EnvironmentVariables.KAFKA_HOST.getValue());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    public synchronized void produce(String topic, String message) {
        ProducerRecord<Object, String> producerRecord = new ProducerRecord<>(topic, message);
        try {
            producer.send(producerRecord, (metadata, e) -> {
                if (e != null) {
                    LOGGER.error("Kafka producer failed!");
                    throw new KafkaException(e);
                }
            });
        } catch (Exception e) {
            producer.close();
        }
    }

    public synchronized void produceTowerPositionData(Tower t) {
        String message = gson.toJson(t);
        produce(TOWER_POS, message);
        LOGGER.info("Tower positions were produced to kafka");
    }
}