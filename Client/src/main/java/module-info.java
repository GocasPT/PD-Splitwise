module pt.isec.pd.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jdk.httpserver;
    requires static lombok;
    requires Splitwsie.sharedLib.main;

    opens pt.isec.pd.client to javafx.fxml;

    exports pt.isec.pd.client;
}