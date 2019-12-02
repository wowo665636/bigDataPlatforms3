package com.company;

import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private final static String QUEUE_NAME_TO_PLATFORM = "client1QueueToPlatform";
    private final static String QUEUE_NAME_FROM_PLATFORM = "client1QueueFromPlatform";

    private static final String HOST = "localhost";
    private static final String USERNAME = "client1username";
    private static final String PASSWORD = "client1password";
    private static final int PORT = 5672;
    private final static String FILES_LOCATION = "C:\\Users\\sarah\\Desktop\\data";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        //factory.setUsername(USERNAME);
        //factory.setPassword(PASSWORD);
        factory.setHost(HOST);
        factory.setPort(PORT);

        File directoryPath = new File(FILES_LOCATION);
        String[] contents = directoryPath.list();
        Connection connection = factory.newConnection();

        assert contents != null; //TODO CHANGE

        for (String fileName : contents) {

            Stream<String> fileStream = Files.lines(Paths.get(FILES_LOCATION, fileName));

            try (Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME_FROM_PLATFORM, false, false, false, null);

                channel.basicConsume(QUEUE_NAME_FROM_PLATFORM, true,  (consumerTag, delivery) -> {
                    Double message =  ByteBuffer.wrap(delivery.getBody()).getDouble();
                    System.out.println(" [x] Platform responded over queue" + QUEUE_NAME_FROM_PLATFORM
                            + " :" + message + System.lineSeparator());
                }, consumerTag -> {});

                channel.queueDeclare(QUEUE_NAME_TO_PLATFORM, true, false,false, null);

                Iterator<String> it = fileStream.iterator();
                String[] headers = it.next().split(",");

                while(it.hasNext()) {
                    String[] values = it.next().split(",");
                    Document doc = new Document();
                    IntStream.range(0, headers.length - 1).forEach(i -> doc.append(headers[i], values[i]));
                    String message = new Gson().toJson(doc);
                    channel.basicPublish("", QUEUE_NAME_TO_PLATFORM, null, message.getBytes());
                    System.out.println(" [x] Sent message to platform over queue " + QUEUE_NAME_TO_PLATFORM + " :" + message + System.lineSeparator());
                    Thread.sleep(5000);
                }

            } catch (TimeoutException | IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
