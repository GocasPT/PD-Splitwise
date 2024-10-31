package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Login;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class LoginController extends Controller {
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

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnLogin.setOnAction(e -> handleLogin());
		hpSignUp.setOnAction(e -> ModelManager.getInstance().changeState(EState.REGISTER));
	}

	@Override
	protected void update() {
		loginPane.setVisible(ModelManager.getInstance().getState() == EState.LOGIN);
	}

	@Override
	protected void handleResponse(Response response) {
		if (response.isSuccess()) {
			ModelManager.getInstance().setEmailLoggedUser(tfEmail.getText());
			ModelManager.getInstance().changeState(EState.GROUPS_PAGE);
		} else {
			ModelManager.getInstance().close();
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
			Platform.exit();
		}
	}

	private void handleLogin() {
		String username = tfEmail.getText();
		String password = tfPassword.getText();

		Login loginRequest = new Login(username, password);
		Response response = ModelManager.getInstance().sendRequest(loginRequest);
		handleResponse(response);
	}
}
