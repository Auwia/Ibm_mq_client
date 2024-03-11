import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Session;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class MQQueueSender {
    public static void main(String[] args) {
        // Parametri di connessione
        String hostName = "yourHostname"; // Hostname o IP del server MQ
        int port = 1414; // Porta TCP per il server MQ
        String queueManager = "yourQueueManager"; // Nome del Queue Manager
        String channel = "yourChannel"; // Nome del canale
        String queueName = "yourQueueName"; // Nome della coda

        try {
            // Crea una connection factory
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

            // Configura la connection factory
            cf.setHostName(hostName);
            cf.setPort(port);
            cf.setQueueManager(queueManager);
            cf.setChannel(channel);
            cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);

            // Crea una connessione JMS
            try (JMSContext context = cf.createContext()) {
                // Crea un producer e una destinazione (coda)
                JMSProducer producer = context.createProducer();
                Destination destination = context.createQueue("queue:///" + queueName);

                // Invia un messaggio
                String message = "Hello IBM MQ";
                producer.send(destination, message);
                System.out.println("Messaggio inviato: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
