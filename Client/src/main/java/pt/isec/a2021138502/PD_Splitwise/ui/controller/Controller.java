package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public abstract class Controller {
	public void registerHandlers() {
		ModelManager.getInstance().addPropertyChangeListener("", evt -> update());
	}

	protected void update() {
	}

	protected void handleResponse(Response response) {
	}

	@FXML
	public void initialize() {
		registerHandlers();
		update();
	}

	//TODO: add handlers for mains buttons (home, groups, etc)
}
