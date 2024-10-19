package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Login;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.SocketManager;

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
			SocketManager.getInstance().connect(this::handleResponse);

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
			System.out.println(response.getErrorDescription());
			SocketManager.getInstance().close();
			//TODO: exit app
		}
	}


	private void switchToListScene() {
		try {
			//TODO: switch scene
			//ClientGUI.switchScene("list.fxml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
