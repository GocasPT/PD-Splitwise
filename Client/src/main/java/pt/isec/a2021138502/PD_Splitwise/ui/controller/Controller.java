package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public abstract class Controller {
	@FXML
	public void initialize() {
		registerHandlers();
		update();
	}

	protected void registerHandlers() {
		ModelManager.getInstance().addPropertyChangeListener(evt -> update());
	}

	protected void update() {
	}

	protected void handleResponse(Response response) {
	}
}
