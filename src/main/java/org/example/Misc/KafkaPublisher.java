package org.example.Misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.example.Model.Rental;
import org.example.Model.RentalKafka;

import java.util.Properties;

public class KafkaPublisher {

    KafkaProducer producer;

    public KafkaPublisher() {

        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "RentalPublisher");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        this.producer = new KafkaProducer(producerConfig);

    }

    public KafkaProducer getProducer() {
        return producer;
    }

    public void sendRental(Rental rental) {

        String rentalShopName = "Bike Rental Poland";

        RentalKafka kafkaRental = new RentalKafka(rental.getEntityId().getUuid(), rental.getClient(), rental.getBike(), rental.getStartTime());
        System.out.println(rentalShopName);
        kafkaRental.setRentalShopName(rentalShopName);


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String rentalJson = "";
        try {
            rentalJson = objectMapper.writeValueAsString(kafkaRental);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(rentalJson);

        ProducerRecord<String, String> record = new ProducerRecord<>("wypozyczenia", rentalShopName, rentalJson);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Wiadomość została wysłana do tematu: " + metadata.topic());
            }

        });

    }

}
