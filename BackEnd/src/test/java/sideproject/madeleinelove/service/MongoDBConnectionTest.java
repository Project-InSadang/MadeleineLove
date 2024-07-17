package sideproject.madeleinelove.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;

public class MongoDBConnectionTest {

    @Test
    public void test() {
        try {
            String connectionString = "mongodb://madeleineAdmin:990001@ec2-3-36-136-110.ap-northeast-2.compute.amazonaws.com:27017";
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("test");
            System.out.println("Connected to MongoDB!");
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
