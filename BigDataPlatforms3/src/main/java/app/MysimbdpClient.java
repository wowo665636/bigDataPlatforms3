package app;

import org.bson.Document;
import org.bson.types.ObjectId;

public class MysimbdpClient {

    public ObjectId id;
    public String inputDirectory, constraintFile, batchIngestName, streamIngestName, databaseString;
    public String streamAnalyticsName, queueFromClient, queueToClient, host, username, password;
    public int port;

    public MysimbdpClient(Document obj){
        this.id = new ObjectId(obj.getString("_id"));
        this.inputDirectory = obj.getString("inputDirectory");
        this.constraintFile = obj.getString("constraintFile");
        this.batchIngestName = obj.getString("batchIngestName");
        this.streamIngestName = obj.getString("streamIngestName");
        this.databaseString = obj.getString("databaseString");

        this.streamAnalyticsName = obj.getString("streamAnalyticsName");
        this.queueFromClient = obj.getString("queueFromClient");
        this.queueToClient = obj.getString("queueToClient");
        this.host = obj.getString("host");
        this.port = obj.getInteger("port");
        this.username = obj.getString("username");
        this.password = obj.getString("password");

    }

    public MysimbdpClient(ObjectId id, String inputDirectory,
                          String constraintFile, String batchIngestName,
                          String streamIngestName, String databaseString,
                          String streamAnalyticsName, String queueFromClient,
                          String queueToClient, String host,
                          String username, String password, int port) {
        this.id = id;
        this.inputDirectory = inputDirectory;
        this.constraintFile = constraintFile;
        this.batchIngestName = batchIngestName;
        this.streamIngestName = streamIngestName;
        this.databaseString = databaseString;

        this.streamAnalyticsName = streamAnalyticsName;
        this.queueFromClient = queueFromClient;
        this.queueToClient = queueToClient;
        this.port = port;
        this.username = username;
        this.password = password;
        this.host = host;
    }

    public Document toDocument(){
        return new Document()
                .append("_id", id.toString())
                .append("inputDirectory", inputDirectory)
                .append("constraintFile", constraintFile)
                .append("batchIngestName", batchIngestName)
                .append("streamIngestName", streamIngestName)
                .append("databaseString", databaseString)

                .append("port", port)
                .append("streamAnalyticsName", streamAnalyticsName)
                .append("queueFromClient", queueFromClient)
                .append("queueToClient", queueToClient)
                .append("host", host)
                .append("username", username)
                .append("password", password);
    }

}
