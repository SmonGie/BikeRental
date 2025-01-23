package org.example;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        MongoRepository repo = new MongoRepository();
        ConsumerClass konsument = new ConsumerClass(repo, 2);


        try (ExecutorService executor = Executors.newFixedThreadPool(konsument.getConsumerGroup().size())) {

            for (KafkaConsumer<String, String> consumer : konsument.getConsumerGroup()) {
                executor.submit(() -> konsument.consumeMessages(consumer));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}