package org.example.Misc;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TopicCreator {

    public void createTopic() {
        String bootstrapServers = "kafka1:9192";

        String topicName = "wypozyczenia";
        int numPartitions = 3;
        short replicationFactor = 3;

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);


        try (AdminClient adminClient = AdminClient.create(props)) {

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            System.out.printf("Utworzono temat:", topicName);

        } catch (TopicExistsException | InterruptedException | ExecutionException e) {
            System.out.println("Temat: " + topicName + " prawdopodobnie już istnieje.");
        }
    }
}