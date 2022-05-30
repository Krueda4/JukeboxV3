module com.jukeboxv3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.jukeboxv3 to javafx.fxml;
    exports com.jukeboxv3;
}