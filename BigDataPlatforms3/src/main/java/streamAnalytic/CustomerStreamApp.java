package streamAnalytic;

import app.Main;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class CustomerStreamApp implements Runnable {
    
    private String host, pathToClientStreamAnalytic;
    private List<String> channels;
    
    public CustomerStreamApp(String host, String channel, String clientStreamAnalyticAppName){
        this.host = host;
        this.channels = channels;
        pathToClientStreamAnalytic = Paths.get(Main.PATH_TO_CLIENT_STREAM_ANALYTICS)
                .resolve(clientStreamAnalyticAppName).toString();
    }

    @Override
    public void run() {

        //long start = System.currentTimeMillis();

        try {
            System.out.println(new Gson().toJson(channels));
            ProcessBuilder pb = new ProcessBuilder("java", "-jar",
                    pathToClientStreamAnalytic, host, new Gson().toJson(channels));
            System.out.println(pb.command());
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process p = pb.start();
            System.out.println("Client process " + pathToClientStreamAnalytic + " is running");

            /*p.waitFor();
            System.out.println("DONE");
            long time = System.currentTimeMillis() - start;

            FileHandler fh = new FileHandler(logPath, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.log(Level.INFO, "Client Ingest {0} has finished running. Duration: {1} ms",
                    new Object[]{ clientStreamIngest, String.valueOf(time)});

            for(Handler handler: logger.getHandlers())
                handler.close();*/

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
    
}
