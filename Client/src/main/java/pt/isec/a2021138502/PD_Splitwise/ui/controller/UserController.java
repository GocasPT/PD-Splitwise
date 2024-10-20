package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.GetUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class UserController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private Text txtEmail;
	@FXML
	private Text txtPhoneNumber;
	@FXML
	private Text txtUsername;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		//btnEdit.setOnAction(e -> ModelManager.getInstance().changeState(EState.EDIT_USER_PAGE));
		btnGroups.setOnAction(e -> ModelManager.getInstance().changeState(EState.GROUPS_PAGE));
		btnInvites.setOnAction(e -> ModelManager.getInstance().changeState(EState.INVITES_PAGE));
		btnUser.setOnAction(e -> update());
	}

	@Override
	protected void update() {
		EState currentState = ModelManager.getInstance().getState();
		if (currentState == EState.USER_PAGE)
			fetchUser();
		homePane.setVisible(currentState == EState.USER_PAGE);
	}

	private void fetchUser() {
		Request request = new GetUser(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		ValueResponse<User> valueResponse = (ValueResponse<User>) response;
		User user = valueResponse.getValue();
		txtUsername.setText(user.username());
		txtEmail.setText(user.email());
		txtPhoneNumber.setText(user.phoneNumber());
	}
}
