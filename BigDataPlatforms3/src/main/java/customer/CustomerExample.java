package customer;

import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.bson.Document;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CustomerExample {

    private final static String QUEUE_NAME = "5db735b3a2b4a50ed31db10f";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            int batchSize = 10;

            String[] headers = { "station_id", "datapoint_id" ,"alarm_id" ,"event_time",
                    "value","valueThreshold","isActive","storedtime"};

            AtomicInteger cpt = new AtomicInteger(0);
            AtomicInteger batchNb = new AtomicInteger(0);

            URL res = CustomerExample.class.getClassLoader().getResource("bts-data-alarm-2017.csv");
            Stream<String> fileStream = Files.lines(Paths.get(res.toURI()));
            final List<Document> buffer = new ArrayList<>();

            for (Iterator<String> it = fileStream.skip(1).iterator(); it.hasNext(); ){

                String[] values = it.next().split(",");
                Document doc = new Document();
                IntStream.range(0, headers.length - 1).forEach(i -> doc.append(headers[i], values[i]));
                buffer.add(doc);

                if(cpt.incrementAndGet() == batchSize|| !it.hasNext()){
                    String message = new Gson().toJson(buffer);
                    System.out.println(buffer.size());
                    buffer.clear();
                    cpt.set(0);
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent message over queue "  + QUEUE_NAME);
                    Thread.sleep(5000);
                }

            }


        }


    }
}
