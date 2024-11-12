package pt.isec.pd.client.ui.controller.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.network.request.User.Login;
import pt.isec.pd.sharedLib.network.response.Response;

public class LoginController extends BaseController {
	@FXML
	private AnchorPane loginPane;
	@FXML
	private TextField tfEmail;
	@FXML
	private TextField tfPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Hyperlink hpSignUp;

	public LoginController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnLogin.setOnAction(e -> {
			try {
				handleLogin();
			} catch ( Exception ex ) {
				viewManager.showError("Login failed: " + ex.getMessage());
			}
		});
		hpSignUp.setOnAction(e -> {
			try {
				viewManager.showView("signup_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show sign up view: " + ex.getMessage());
			}
		});
	}

	@Override
	protected void update() {
	}

	private void handleLogin() {
		String username = tfEmail.getText();
		String password = tfPassword.getText();

		//TODO: add validator (maven dependency)
		if (username.isEmpty() || password.isEmpty()) {
			viewManager.showError("Username and password are required");
			return;
		}

		Login loginRequest = new Login(username, password);
		Response response = modelManager.sendRequest(loginRequest);
		handleResponse(response);
	}

	@Override
	protected void handleResponse(Response response) {
		if (response.isSuccess()) {
			modelManager.setEmailLoggedUser(tfEmail.getText());
			viewManager.showView("groups_view");
		} else {
			viewManager.showError(response.getErrorDescription());
			//TODO: check this later
			modelManager.close();
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
			Platform.exit();
		}
	}
}
