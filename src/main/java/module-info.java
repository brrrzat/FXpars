module org.example.fxparse {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires unirest.java;
    requires json;

    opens org.example.fxparse to javafx.fxml;
    exports org.example.fxparse;
}