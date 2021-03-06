package streamAnalytic;

import app.MysimbdpClient;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StreamComputingService {

    private MongoClient mongoClient;

    public StreamComputingService(MongoClient mongoClient){
        this.mongoClient = mongoClient;

        List<MysimbdpClient> clients = getClients();
        for(MysimbdpClient client: clients){
            new Thread(new CustomerStreamApp(client.streamAnalyticsName)).start();
        }
    }

    private List<MysimbdpClient> getClients(){
        List<MysimbdpClient> clients = new ArrayList<>();

        MongoCollection collection = mongoClient.getDatabase("bts").getCollection("client");

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                clients.add(new MysimbdpClient(cursor.next()));
            }
        }
        return clients;
    }
}
