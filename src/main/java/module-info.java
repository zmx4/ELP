module org.tick.elp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires ormlite.jdbc;
    requires java.sql;

    opens org.tick.elp to javafx.fxml;
    opens org.tick.elp.Entity to ormlite.jdbc;
    exports org.tick.elp;
}