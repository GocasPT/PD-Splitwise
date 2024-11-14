package pt.isec.pd.client.ui.controller.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.network.request.User.Register;
import pt.isec.pd.sharedLib.network.response.Response;

public class RegisterController extends BaseController {
	@FXML
	private AnchorPane registerPane;
	@FXML
	private TextField tfUsername;
	@FXML
	private TextField tfPhoneNumber;
	@FXML
	private TextField tfEmail;
	@FXML
	private PasswordField tfPassword;
	@FXML
	private Button btnRegiste;
	@FXML
	private Hyperlink hpSignIn;

	public RegisterController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnRegiste.setOnAction(e -> {
			try {
				handleRegister();
			} catch ( Exception ex ) {
				viewManager.showError("Register failed: " + ex.getMessage());
			}
		});
		hpSignIn.setOnAction(e -> {
			try {
				viewManager.showView("login_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to go to login: " + ex.getMessage());
			}
		});
	}

	@Override
	protected void update() {
	}

	private void handleRegister() {
		String username = tfUsername.getText();
		String phoneNumber = tfPhoneNumber.getText();
		String email = tfEmail.getText();
		String password = tfPassword.getText();

		//TODO: add validator (maven dependency)
		if (username.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
			viewManager.showError("All fields are required");
			return;
		}

		Register registerRequest = new Register(username, phoneNumber, email, password);
		viewManager.sendRequestAsync(registerRequest, this::handleResponse);
	}

	@Override
	protected void handleResponse(Response response) {
		if (response.isSuccess())
			viewManager.showView("login_view");
		else {
			viewManager.showError(response.getErrorDescription());
			//TODO: check this later
			modelManager.close();
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
			Platform.exit();
		}
	}
}
