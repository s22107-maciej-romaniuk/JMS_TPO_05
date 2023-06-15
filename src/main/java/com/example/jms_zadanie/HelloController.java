package com.example.jms_zadanie;

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
    public void setConsumer(MessageConsumer consumer) throws JMSException {
        this.consumer = consumer;
        this.consumer.setMessageListener(message -> {
                try {
                    // Handle the received message
                    this.messageListView.getItems().add(((TextMessage) message).getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }
    public void setSession(Session session) {
        this.session = session;
    }

    public void send() throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(messageTextField.getText());
        this.producer.send(textMessage);
    }
}