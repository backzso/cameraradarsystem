package org.example.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private Consumer<String, String> consumer;

    private Consumer<String, String> createConsumer() {
        String broker = EnvironmentVariables.KAFKA_HOST.getValue();
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "world-cameralossstatus");

        return new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        try {
            // Create the consumer, if removed previously or not initialized
            if (consumer == null) {
                synchronized (KafkaConsumer.class) {
                    if (consumer == null) {
                        consumer = createConsumer();
                    }
                }
            }
            LOGGER.info("Kafka consumer started for topic cameralos");
            consumer.subscribe(Collections.singleton(KafkaConstants.CAMERA_LOSS_STATUS));
            Duration timeout = Duration.ofSeconds(1543559242, 999_999_999);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info(record.value());
                    String data = record.value();
                    
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception => {}", e.getMessage());
        } finally {
            assert consumer != null;
            consumer.close();
        }
    }
}
