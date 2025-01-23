package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class ConsumerClass {

    private final Properties consumerConfig = new Properties();
    private final KafkaConsumer<String, String> consumer;
    private final MongoRepository repo;
    private final List<KafkaConsumer<String, String>> consumerGroup = new ArrayList<>();

    public ConsumerClass(MongoRepository repository, int number) {

        this.repo = repository;
        this.consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "dobra_grupa");
        this.consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        this.consumerConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        consumer = new KafkaConsumer<>(this.consumerConfig);

        for (int i = 0; i < number; i++) {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig);
            consumer.subscribe(Collections.singleton("wypozyczenia"));
            consumerGroup.add(consumer);
        }

    }

    public List<KafkaConsumer<String, String>> getConsumerGroup() {
        return consumerGroup;
    }

    public void consumeMessages(KafkaConsumer<String, String> consumer) {
        try (consumer) {
            System.out.println("Consumer started");
            consumer.poll(0);
            System.out.println("Moje Id: " + consumer.groupMetadata().memberId() + " Moja grupa " + consumer.groupMetadata().groupId());

            while (true) {
//                System.out.println("Oczekuję na wiadomość.");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Moje Id:" + consumer.groupMetadata().memberId() + " Moja grupa " + consumer.groupMetadata().groupId());
                    System.out.printf("Odebrano wiadomosc: Key=%s, Value=%s, Partition=%d, Offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                    repo.saveRental(record.value());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }

    }

}
