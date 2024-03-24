module com.example.ai {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ai to javafx.fxml;
    exports com.example.ai;
}