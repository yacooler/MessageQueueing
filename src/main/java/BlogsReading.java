import com.rabbitmq.client.*;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BlogsReading {
    public static final String EXCHANGE_NAME = "BlogsExchange";
    public static final String[] TOPICS = {"Java", "Kotlin", "Erlang", "PHP", "JS"};
    public static void main(String[] argv) throws Exception {

        System.out.println("Type " + Arrays.toString(TOPICS));
        Scanner scanner = new Scanner(System.in);
        String topic = "";

        boolean topicSelect = false;
        while(!topicSelect){
            topic = scanner.nextLine();
            for(String s:TOPICS) {
                if (s.equals(topic)) {
                    topicSelect = true;
                    break;
                }
            }
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("BlogQueueName: " + queueName);

        channel.queueBind(queueName, EXCHANGE_NAME, topic);

        System.out.println("[*] Waiting posts");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] Received blog:" + message);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
