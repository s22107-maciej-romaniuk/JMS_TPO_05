package com.example.jms_zadanie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class HelloController {
    MessageConsumer consumer;

    MessageProducer producer;

    Session session;

    @FXML
    ListView<String> messageListView;
    @FXML
    TextField messageTextField;
    @FXML
    Button sendButton;
    private String name;

    private void setConsumer(MessageConsumer consumer) throws JMSException {
        this.consumer = consumer;
        this.consumer.setMessageListener(message -> {
                try {
                    // Handle the received message
                    Platform.runLater(() -> {
                        try {
                            messageListView.getItems().add(((TextMessage) message).getText());
                        } catch (JMSException e) {
                            throw new RuntimeException(e);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
    private void setProducer(MessageProducer producer) {
        this.producer = producer;
    }
    public void setSession(Session session, Topic topic, String name) throws JMSException {
        this.session = session;
        this.name = name;
        setConsumer(session.createDurableSubscriber(topic,name));
        setProducer(session.createProducer(topic));
    }

    public void send() throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(this.name + ": " + messageTextField.getText());
        this.producer.send(textMessage);


    }
}