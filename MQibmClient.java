import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

public class MQibmClient {
    public static void main(String[] args) {
        String hostName = "yourHostname"; 
        int port = 1414; 
        String queueManager = "yourQueueManager"; 
        String channel = "yourChannel"; 
        String queueName = "yourQueueName";
        String cipherSuite = "yourCipherSuite";
        String keyStorePath = "yourKeyStorePath";
        String keyStorePassword = "yourKeyStorePassword";

        try {
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

            // Configura la connection factory
            cf.setHostName(hostName);
            cf.setPort(port);
            cf.setQueueManager(queueManager);
            cf.setChannel(channel);
            cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            cf.setSSLCipherSuite(cipherSuite);

            System.setProperty("javax.net.ssl.keyStore", keyStorePath);
            System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");

            System.setProperty("jdk.tls.client.cipherSuites", cipherSuite);
          
            try (JMSContext context = cf.createContext()) {
                // Crea un producer e una destinazione (coda)
                JMSProducer producer = context.createProducer();
                Destination destination = context.createQueue("queue:///" + queueName);
              
                String message = "Hello IBM MQ";
                producer.send(destination, message);
                System.out.println("Message sent: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
