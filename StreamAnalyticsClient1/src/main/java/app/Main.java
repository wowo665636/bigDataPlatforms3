package app;


import com.rabbitmq.client.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.nio.ByteBuffer;
import java.util.Scanner;

import org.apache.flink.streaming.connectors.rabbitmq.RMQSink;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String ...args) throws Exception {

        String queueNameToAnalytic = "client1QueueToPlatform";
        String queueNameFromAnalytic = "client1QueueFromPlatform";
        String host = "localhost";
        String username = "client1username";
        String password = "client1password";
        int port = 5672;

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // check pointing is required for exactly-once or at-least-once guarantees, start a checkpoint every 1000 ms
        env.enableCheckpointing(1000);

        final RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
                .setHost(host).setVirtualHost("/").setUserName("guest").setPassword("guest")
                .setPort(port).build();

        DeserializationSchema<TrendingVideo> deserializationSchema = new DeserializationSchema<TrendingVideo>() {
            @Override
            public TypeInformation<TrendingVideo> getProducedType() {
                return TypeInformation.of(TrendingVideo.class);
            }

            @Override
            public TrendingVideo deserialize(byte[] bytes) throws IOException {
                JSONObject obj = new JSONObject(new String(bytes));
                System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + obj.getDouble("likes") + " " + obj.getDouble("dislikes"));
                TrendingVideo vid = new TrendingVideo();
                vid.setLikes(obj.getDouble("likes"));
                vid.setDislikes(obj.getDouble("dislikes"));
                return vid;
            }

            @Override
            public boolean isEndOfStream(TrendingVideo trendingVideo) {
                return false;
            }
        };

        final DataStream<TrendingVideo> stream = env
                .addSource(new RMQSource<TrendingVideo>(
                        connectionConfig,            // config for the RabbitMQ connection
                        queueNameToAnalytic,         // name of the RabbitMQ queue to consume
                  //      true,        // use correlation ids; can be false if only at-least-once is required
                        deserializationSchema))   // deserialization schema to turn messages into Java objects
                .setParallelism(1);              // non-parallel source is only required for exactly-once

        DataStream<Double> calc = stream.map((MapFunction<TrendingVideo, Double>) video -> video.getLikes() / (video.getLikes() + video.getDislikes()));

        calc.addSink(new RMQSink<Double>(
                connectionConfig,
                queueNameFromAnalytic, (SerializationSchema<Double>) aDouble -> {
                    byte[] bytes = new byte[8];
                    ByteBuffer.wrap(bytes).putDouble(aDouble);
                    return bytes;
                }));

        env.execute();
    }

}
