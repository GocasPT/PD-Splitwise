package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Login;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.SocketManager;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

public class LoginController {
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Button loginButton;

	@FXML
	public void initialize() {
		loginButton.setOnAction(e -> handleLogin());
	}

	private void handleLogin() {
		String username = usernameField.getText();
		String password = passwordField.getText();

		try {
			SocketManager.getInstance().connect();

			Login loginRequest = new Login(username, password);
			SocketManager.getInstance().sendRequest(loginRequest);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void handleResponse(Response response) {
		if (response.isSuccess())
			switchToListScene();
		else {
			SocketManager.getInstance().close();
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
			Platform.exit();
		}
	}


	private void switchToListScene() {
		try {
			//TODO: switch scene
			ClientGUI.switchScene("home_page.fxml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
