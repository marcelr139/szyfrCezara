module aplikacja.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens aplikacja.demo to javafx.fxml;
    exports aplikacja.demo;
}