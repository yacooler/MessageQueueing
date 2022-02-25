import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class BlogsPosting {
    public static final String EXCHANGE_NAME = "BlogsExchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");

        try (Connection connection = factory.newConnection();

            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            for (int i = 0; i < 100; i++) {

                String topic = getRandomTopic();
                String content = (i + 1) + ") some message " + new Random().nextInt(100);

                channel.basicPublish(EXCHANGE_NAME, topic, null, content.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + topic + "':'" + content + "'");
            }

        }
        System.out.println("===DONE===");
    }

    public static String getRandomTopic(){
        return switch (new Random().nextInt(5)) {
            case 0 -> "Java";
            case 1 -> "Kotlin";
            case 2 -> "Erlang";
            case 3 -> "PHP";
            default -> "JS";
        };

    }

}
