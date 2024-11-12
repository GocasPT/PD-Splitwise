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
    requires static lombok;
    requires Splitwsie.sharedLib.main;

    opens pt.isec.pd.client to javafx.fxml;
    opens pt.isec.pd.client.model to javafx.fxml;
    opens pt.isec.pd.client.ui to javafx.graphics;
	opens pt.isec.pd.client.ui.component to javafx.fxml;
    opens pt.isec.pd.client.ui.controller to javafx.fxml;
    opens pt.isec.pd.client.ui.controller.view to javafx.fxml;

    exports pt.isec.pd.client;
}