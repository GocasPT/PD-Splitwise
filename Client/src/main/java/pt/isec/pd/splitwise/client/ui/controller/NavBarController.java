package pt.isec.pd.splitwise.client.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public class NavBarController extends BaseController {
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;
	private static NavButton currentButton = NavButton.GROUPS;

	public NavBarController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnGroups.setOnAction(e -> {
			try {
				viewManager.showView("groups_view");
				currentButton = NavButton.GROUPS;
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show groups: " + ex.getMessage());
			} finally {
				update();
			}
		});
		btnInvites.setOnAction(e -> {
			try {
				viewManager.showView("invites_view");
				currentButton = NavButton.INVITES;
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show invites: " + ex.getMessage());
			} finally {
				update();
			}
		});
		btnUser.setOnAction(e -> {
			try {
				viewManager.showView("user_view");
				currentButton = NavButton.USER;
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show user: " + ex.getMessage());
			} finally {
				update();
			}
		});
	}

	//TODO: hot fix (delay update)
	@Override
	protected void update() {
		//TODO: if currentButton doesn't change, don't update (idk...performance?)

		//TODO: remove style button
		btnGroups.setStyle("-fx-background-color: #2196F3");
		btnInvites.setStyle("-fx-background-color: #2196F3");
		btnUser.setStyle("-fx-background-color: #2196F3");

		//TODO: add style button
		switch (currentButton) {
			case GROUPS -> btnGroups.setStyle("-fx-background-color: #4CAF50");
			case INVITES -> btnInvites.setStyle("-fx-background-color: #4CAF50");
			case USER -> btnUser.setStyle("-fx-background-color: #4CAF50");
		}
	}

	@Override
	protected void handleResponse(Response response) {

	}

	private enum NavButton {
		GROUPS, INVITES, USER
	}
}
