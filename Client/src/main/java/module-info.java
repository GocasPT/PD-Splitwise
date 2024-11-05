module pt.isec.a2021138502.PD_Splitwise {
	requires common;
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.controls;
	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires net.synedra.validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;

	opens pt.isec.a2021138502.PD_Splitwise to javafx.fxml;
	opens pt.isec.a2021138502.PD_Splitwise.ui to javafx.fxml;
	opens pt.isec.a2021138502.PD_Splitwise.ui.controller to javafx.fxml;
	opens pt.isec.a2021138502.PD_Splitwise.model to javafx.fxml;

	exports pt.isec.a2021138502.PD_Splitwise;
	exports pt.isec.a2021138502.PD_Splitwise.ui;
	exports pt.isec.a2021138502.PD_Splitwise.ui.controller;
	exports pt.isec.a2021138502.PD_Splitwise.model;
}