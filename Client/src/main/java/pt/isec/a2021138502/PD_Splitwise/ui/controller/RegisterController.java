package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Register;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class RegisterController extends Controller {
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

	@Override
	public void registerHandlers() {
		super.registerHandlers();
		btnRegiste.setOnAction(e -> handleRegister());
		hpSignIn.setOnAction(e -> ModelManager.getInstance().changeState(EState.LOGIN));
	}

	@Override
	protected void update() {
		registerPane.setVisible(ModelManager.getInstance().getState() == EState.REGISTER);
	}

	@Override
	protected void handleResponse(Response response) {
		if (response.isSuccess())
			ModelManager.getInstance().changeState(EState.LOGIN);
		else {
			ModelManager.getInstance().close();
			new Alert(Alert.AlertType.ERROR, response.getErrorDescription()).showAndWait();
			Platform.exit();
		}
	}

	private void handleRegister() {
		String username = tfUsername.getText();
		String phoneNumber = tfPhoneNumber.getText();
		String email = tfEmail.getText();
		String password = tfPassword.getText();

		try {
			Register registerRequest = new Register(username, phoneNumber, email, password);
			Response response = ModelManager.getInstance().sendRequest(registerRequest);
			handleResponse(response);
		} catch (Exception ex) { //TODO: Improve exception handling
			System.out.println("Error on 'handleLogin': " + ex.getMessage());
		}
	}
}
