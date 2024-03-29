import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Destination;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class MQQueueSender2 {

    public static void main(String[] args) {
        try {
            // Parametri di connessione e configurazione SSL
            String hostName = "yourHostname";
            int port = 1414; // Porta TCP per il server MQ
            String queueManager = "yourQueueManager";
            String channel = "yourChannel";
            String queueName = "massimo_queue"; // Nome della coda esistente
            String cipherSuite = "TLS_RSA_WITH_AES_256_CBC_SHA256"; // Sostituisci con la tua Cipher Suite
            String keystoreAndTruststorePath = "path/to/your/keystore.p12";
            String password = "yourKeystorePassword";

            // Carica il keystore/truststore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keystoreAndTruststorePath), password.toCharArray());

            // Inizializza KeyManagerFactory e TrustManagerFactory con lo stesso keystore
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, password.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Crea e inizializza SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Crea una connection factory e imposta le proprietà SSL
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
            cf.setHostName(hostName);
            cf.setPort(port);
            cf.setQueueManager(queueManager);
            cf.setChannel(channel);
            cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            cf.setSSLCipherSuite(cipherSuite);
            cf.setSSLContext(sslContext); // Imposta il contesto SSL personalizzato

            // Crea una connessione JMS e invia un messaggio
            try (JMSContext context = cf.createContext()) {
                JMSProducer producer = context.createProducer();
                Destination destination = context.createQueue("queue:///" + queueName);
                String message = "Hello IBM MQ with SSL";
                producer.send(destination, message);
                System.out.println("Messaggio inviato: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
