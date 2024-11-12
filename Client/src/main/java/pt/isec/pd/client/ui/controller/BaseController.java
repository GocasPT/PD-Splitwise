package pt.isec.pd.client.ui.controller;

import javafx.fxml.FXML;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.network.response.Response;

public abstract class BaseController {
	protected ViewManager viewManager;
	protected ModelManager modelManager;

	public BaseController(ViewManager viewManager, ModelManager modelManager) {
		this.viewManager = viewManager;
		this.modelManager = modelManager;
	}

	@FXML
	public void initialize() {
		registerHandlers();
		update();
	}

	protected abstract void registerHandlers();

	protected abstract void update();

	protected abstract void handleResponse(Response response);
}
