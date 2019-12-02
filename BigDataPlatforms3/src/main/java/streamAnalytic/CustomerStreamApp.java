package streamAnalytic;

import app.Main;

import java.io.*;
import java.nio.file.Paths;


public class CustomerStreamApp implements Runnable {

    private String pathToClientStreamAnalytic;

    CustomerStreamApp(String clientStreamAnalyticAppName){
        pathToClientStreamAnalytic = Paths.get(Main.PATH_TO_CLIENT_STREAM_ANALYTICS)
                .resolve(clientStreamAnalyticAppName).toString();
    }

    @Override
    public void run() {

        try{
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", pathToClientStreamAnalytic);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            //TODO deal with error stream
            Process p = pb.start();
            System.out.println("Client process " + pathToClientStreamAnalytic + " is running");
            p.waitFor();
        } catch(IOException | InterruptedException e){
            System.err.println("Problem creating connection/channels");
            e.printStackTrace();
        }
     }

}