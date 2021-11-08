package base;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class BasicMQJUnit {

    protected static final String MQ_URI = "amqp://admin:admin@192.168.0.216:5672";

    protected static ConnectionFactory connectionFactory;
    protected static Connection        connection;
    protected static Channel           channel;

    public static Channel newChannel(Connection connection) {
        try {
            return connection.createChannel();
        } catch(IOException e) {
            log.error("Get channel error", e);
            throw new IllegalStateException(e);
        }
    }


    public static void doSend(String exchange, String routingKey, String message, Map<String, Object> headers) {
        try {
            channel.basicPublish(exchange, routingKey,
                                 new AMQP.BasicProperties.Builder().contentType("text/plain").headers(headers)
                                         .deliveryMode(1).expiration("30000").build(),
                                 message.getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.error("Send Message: " + message + " by routingKey: " + routingKey + " error", e);
            throw new IllegalStateException(e);
        }
    }
}
