package com.example.jms_zadanie;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.InetAddress;
import java.util.Hashtable;

public class QueueListenerExample extends Application {

    static Session session;

    static Connection connection;
    static Topic topic;
    static String user;
    static String topicName;
    public static void main(String[] args) {
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:61616");

        topicName = args[0];
        user = args[1];

        try {
            Context ctx = new InitialContext(env);
            // Create a connection factory
            ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");


            // Create a connection
            connection = connectionFactory.createConnection();
            connection.setClientID(user);
            connection.start();

            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the topic
            topic = session.createTopic(topicName);

            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Uruchamianie GUI");
        FXMLLoader fxmlLoader = new FXMLLoader(QueueListenerExample.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        HelloController controller = fxmlLoader.getController();
        controller.setSession(session, topic, user);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // executed when the application shuts down
        try {
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
