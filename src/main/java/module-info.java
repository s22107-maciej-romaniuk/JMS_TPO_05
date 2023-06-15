module com.example.jms_zadanie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires activemq.all;


    opens com.example.jms_zadanie to javafx.fxml;
    exports com.example.jms_zadanie;
}