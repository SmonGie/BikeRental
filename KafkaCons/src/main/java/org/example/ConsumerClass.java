package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

import static java.lang.Math.random;

public class ConsumerClass {

    private final Properties consumerConfig = new Properties();
    private final KafkaConsumer<String, String> consumer;
    private final MongoRepository repo;


    public ConsumerClass(MongoRepository repository) {


        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        String ajdi = randomNumber + "";


        this.repo = repository;
        this.consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "dobra_grupa");
        this.consumerConfig.put(ConsumerConfig.CLIENT_ID_CONFIG, ajdi);
        this.consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");

        consumer = new KafkaConsumer<>(this.consumerConfig);
        consumer.subscribe(Collections.singleton("wypozyczenia"));

    }

    public void consumeMessages() {
        try {

            while (true) {
                System.out.println("Oczekuję na wiadomość.");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
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
