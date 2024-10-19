package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.EditUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.SocketManager;


public class HomeController {
	@FXML
	private Button btn;

	@FXML
	public void initialize() {
		btn.setOnAction(e -> handlerClick());
	}

	private void handlerClick() {
		try {
			EditUser editUser = new EditUser("batata triste", "9177,tira tira, mete mte", "email.batata", "password");
			SocketManager.getInstance().sendRequest(editUser);
			SocketManager.getInstance().waitForResponse(this::handleResponse);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void handleResponse(Response response) {
		if (response.isSuccess())
			System.out.println("Success");
		else {
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
		}
	}
}
