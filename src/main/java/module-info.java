module com.pokedex.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jdk.unsupported.desktop;

    opens com.pokedex.app to javafx.fxml;
    exports com.pokedex.app;
}
