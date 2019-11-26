package app;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class IngestManager {

    protected MongoClient mongoClient;
    protected Logger logger;
    private final static String DB_NAME = "bts";

    public IngestManager(MongoClient mongoClient) throws IOException {
        this.mongoClient = mongoClient;
        this.logger = Logger.getLogger(this.getClass().getName());
        String path = Paths.get(Main.LOG_FOLDER).resolve(this.getClass().getName() + ".txt").toString();
        FileHandler fh = new FileHandler(path);
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }

    protected List<MysimbdpClient> getClients(){
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
