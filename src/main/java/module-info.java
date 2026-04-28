module pkg.java.project.transfermarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens pkg.java.project.transfermarket to javafx.fxml;
    exports pkg.java.project.transfermarket.Backend;
    exports pkg.java.project.transfermarket.UI;
    opens pkg.java.project.transfermarket.UI to javafx.fxml;
    exports pkg.java.project.transfermarket.Util;
    opens pkg.java.project.transfermarket.Util to javafx.fxml;
}