package pt.isec.pd.client.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.network.response.Response;

public class NavBarController extends BaseController {
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;

	public NavBarController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnGroups.setOnAction(e -> {
			try {
				viewManager.showView("groups_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show groups: " + ex.getMessage());
			}
		});
		btnInvites.setOnAction(e -> {
			try {
				viewManager.showView("invites_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show invites: " + ex.getMessage());
			}
		});
		btnUser.setOnAction(e -> {
			try {
				viewManager.showView("user_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show user: " + ex.getMessage());
			}
		});
	}

	@Override
	protected void update() {

	}

	@Override
	protected void handleResponse(Response response) {

	}}
