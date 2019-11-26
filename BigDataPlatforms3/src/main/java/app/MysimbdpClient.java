package app;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class MysimbdpClient {

    private ObjectId id;
    private String inputDirectory;
    private String constraintFile;
    private String batchIngestName;
    private String streamIngestName;
    private String databaseString;
    private String streamAnalyticsName;
    private String channel;

    public MysimbdpClient(Document obj){
        this.id = new ObjectId(obj.getString("_id"));
        this.inputDirectory = obj.getString("inputDirectory");
        this.constraintFile = obj.getString("constraintFile");
        this.batchIngestName = obj.getString("batchIngestName");
        this.streamIngestName = obj.getString("streamIngestName");
        this.databaseString = obj.getString("databaseString");
        this.streamAnalyticsName = obj.getString("streamAnalyticsName");
        this.channel = obj.getString("channel");
    }

    public MysimbdpClient(ObjectId id, String inputDirectory,
                          String constraintFile, String batchIngestName,
                          String streamIngestName, String streamAnalyticsName,
                          String databaseString, String channel) {
        this.id = id;
        this.inputDirectory = inputDirectory;
        this.constraintFile = constraintFile;
        this.batchIngestName = batchIngestName;
        this.streamIngestName = streamIngestName;
        this.databaseString = databaseString;
        this.channel = channel;
        this.streamAnalyticsName = streamAnalyticsName;
    }

    public ObjectId getId() {
        return id;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getConstraintFile() {
        return constraintFile;
    }

    public String getChannel() {
        return channel;
    }

    public String getStreamAnalyticsName() {
        return streamAnalyticsName;
    }
    public String getBatchIngestName() {
        return batchIngestName;
    }

    public String getStreamIngestName() {
        return streamIngestName;
    }

    public String getDatabaseString() {
        return databaseString;
    }

    public Document toDocument(){
        return new Document()
                .append("_id", id.toString())
                .append("inputDirectory", inputDirectory)
                .append("constraintFile", constraintFile)
                .append("batchIngestName", batchIngestName)
                .append("streamIngestName", streamIngestName)
                .append("databaseString", databaseString)
                .append("channel", channel)
                .append("streamAnalyticsName", streamAnalyticsName);
    }


}
