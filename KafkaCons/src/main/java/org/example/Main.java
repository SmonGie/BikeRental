package org.example;

public class Main {
    public static void main(String[] args) {

            MongoRepository repo = new MongoRepository();
            ConsumerClass konsument = new ConsumerClass(repo);

            konsument.consumeMessages();


    }
}