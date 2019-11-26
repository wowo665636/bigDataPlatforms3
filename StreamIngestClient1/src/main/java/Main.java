import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.bson.Document;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String ...args) throws IOException {

        String host = args[0];
        String channelName = args[1];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        Connection connection;

        try{
            connection = factory.newConnection();
        }catch(TimeoutException e){
            System.err.println("The factory couldn't create a new connection");
            e.printStackTrace();
            return;
        }

        Channel channel = connection.createChannel();
        channel.queueDeclare(channelName, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(channelName, true, deliverCallback, consumerTag -> {});
    }

}
