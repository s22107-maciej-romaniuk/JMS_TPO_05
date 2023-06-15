package com.example.jms_zadanie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

public class QueueListenerExample extends Application {

    static MessageConsumer consumer;
    static MessageProducer producer;
    static Session session;
    public static void main(String[] args) {
        String brokerUrl = "tcp://localhost:61616";
        String queueName = "myQueue";

        try {
            // Create a connection factory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

            // Create a connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the queue
            Queue queue = session.createQueue(queueName);

            // Create a message consumer
            consumer = session.createConsumer(queue);
            producer = session.createProducer(queue);
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
        controller.setConsumer(consumer);
        controller.setProducer(producer);
        controller.setSession(session);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
