package app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;
import org.bson.types.ObjectId;
import streamAnalytic.StreamComputingService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static String
            LOG_FOLDER,
            DB_NAME,
            DB_CONNECTION_STRING_DEV,
            CLIENT_UPLOADS_RELATIVE_PATH,
            PATH_TO_CLIENT_BATCH_INGESTS,
            PATH_TO_CLIENT_STREAM_INGESTS,
            PATH_TO_CLIENT_STREAM_ANALYTICS;

    public static void main(String... args){

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            prop.load(input);
            DB_CONNECTION_STRING_DEV = prop.getProperty("db.connection_string");
            DB_NAME =  prop.getProperty("db.name");
            LOG_FOLDER = prop.getProperty("log_folder");
            CLIENT_UPLOADS_RELATIVE_PATH = prop.getProperty("client_uploads_relative_path");
            PATH_TO_CLIENT_BATCH_INGESTS = prop.getProperty("path_to_client_batch_ingests");
            PATH_TO_CLIENT_STREAM_INGESTS = prop.getProperty("path_to_client_stream_ingests");
            PATH_TO_CLIENT_STREAM_ANALYTICS = prop.getProperty("path_to_client_stream_analytics");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        MongoClient dbClient = MongoClients.create(DB_CONNECTION_STRING_DEV);
        MongoDatabase database = dbClient.getDatabase(DB_NAME);
        createClients(database.getCollection("client"));
        StreamComputingService manager = new StreamComputingService(dbClient);
    }

    private static void createClients(MongoCollection<Document> clientCollection){
        clientCollection.drop();
        logger.log(Level.INFO, "Emptied client table");

        List<MysimbdpClient> clients = Arrays.asList(
                new MysimbdpClient(new ObjectId("5db735b3a2b4a50ed31db10f"),
                        "inputDirectory", "constraintFile",
                        "BatchIngestClient1.jar", "StreamIngestClient1.jar", "Client1db",

                        "StreamAnalyticsClient1-1.0-SNAPSHOT.jar", "client1QueueToPlatform", "client1QueueFromPlatform" ,
                        "localhost", "client1username", "client1password", 5672),
                new MysimbdpClient(new ObjectId("5db735b3a2b4a50ed31db110"),
                        "inputDirectory", "constraintFile",
                        "BatchIngestClient2.jar", "StreamIngestClient2.jar", "Client2db",

                        "StreamAnalyticsClient2.jar", "client2QueueToPlatform", "client2QueueFromPlatform" ,
                        "localhost", "client2username", "client2password", 5672),

                new MysimbdpClient(new ObjectId("5db735b3a2b4a50ed31db111"),
                        "inputDirectory", "constraintFile",
                        "BatchIngestClient3.jar", "StreamIngestClient3.jar", "Client3db",

                        "StreamAnalyticsClient3.jar", "client3QueueToPlatform", "client3QueueFromPlatform" ,
                        "localhost", "client3username", "client3password", 5672),

                new MysimbdpClient(new ObjectId("5db735b3a2b4a50ed31db112"),
                        "inputDirectory", "constraintFile",
                        "BatchIngestClient4.jar", "StreamIngestClient4.jar", "Client4db",

                        "StreamAnalyticsClient4.jar", "client4QueueToPlatform", "client4QueueFromPlatform" ,
                        "localhost", "client4username", "client4password", 5672)
        );

        List<InsertOneModel<Document>> all = clients.stream()
                .map(client ->  new InsertOneModel<>(client.toDocument()))
                .collect(Collectors.toList());

        logger.log(Level.INFO, "Creating test clients");
        clientCollection.bulkWrite(all);
        logger.log(Level.INFO, "Created test clients");

    }
}
