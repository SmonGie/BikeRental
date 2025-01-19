package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerClass {

    Properties consumerConfig = new Properties();
    KafkaConsumer consumer;


    public ConsumerClass() {

        this.consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "dobra_grupa");
        this.consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");

        consumer = new KafkaConsumer(this.consumerConfig);
        consumer.subscribe(Collections.singleton("wypozyczenia"));

    }

    public void consumeMessages(){

        while (true) {
            System.out.println("Oczekuję na wiadomość.");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Odebrano wiadomosc: Key=%s, Value=%s, Partition=%d, Offset=%d%n",
                        record.key(), record.value(), record.partition(), record.offset());
            }
        }


    }

}
