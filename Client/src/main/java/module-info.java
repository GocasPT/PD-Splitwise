module pt.isec.a2021138502.PD_Splitwise {
	requires javafx.controls;
	requires javafx.fxml;

	//TODO: check this later
	/*requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires net.synedra.validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;*/
	requires common;
	requires java.desktop;

	opens pt.isec.a2021138502.PD_Splitwise to javafx.fxml;
	exports pt.isec.a2021138502.PD_Splitwise;
	exports pt.isec.a2021138502.PD_Splitwise.model;
	opens pt.isec.a2021138502.PD_Splitwise.model to javafx.fxml;
}