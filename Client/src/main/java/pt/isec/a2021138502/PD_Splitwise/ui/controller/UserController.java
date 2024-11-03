package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.EditUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.GetUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;
import pt.isec.a2021138502.PD_Splitwise.ui.MainGUI;

import java.io.IOException;

public class UserController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private Text txtGroupName;
	@FXML
	private Button btnLogout;
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
		btnEdit.setOnAction(e -> editUSerPopUp());
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

	private void editUSerPopUp() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("edit_user_popup.fxml"));
			DialogPane popup = fxmlLoader.load();

			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setScene(new Scene(popup));

			TextField tfName = (TextField) popup.lookup("#tfName");
			TextField tfEmail = (TextField) popup.lookup("#tfEmail");
			TextField tfPhoneNumber = (TextField) popup.lookup("#tfPhoneNumber");
			TextField tfPassword = (TextField) popup.lookup("#tfPassword");
			ObservableList<ButtonType> buttonTypes = popup.getButtonTypes();
			Button btnFinish = (Button) popup.lookupButton(buttonTypes.get(0));
			Button btnCancel = (Button) popup.lookupButton(buttonTypes.get(1));

			tfName.setText(txtUsername.getText());
			tfEmail.setText(txtEmail.getText());
			tfPhoneNumber.setText(txtPhoneNumber.getText());

			btnFinish.setOnAction(e -> {
				if (tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || tfPhoneNumber.getText().isEmpty() || tfPassword.getText().isEmpty())
					return; //TODO: handle error

				editUser(
						tfName.getText(),
						tfEmail.getText(),
						tfPhoneNumber.getText(),
						tfPassword.getText()
				);
				popupStage.close();
			});

			btnCancel.setOnAction(e -> popupStage.close());

			popupStage.showAndWait();
		} catch ( IOException e ) {
			System.out.println("Error loading edit user popup");
		}
	}

	private void editUser(String username, String email, String phoneNumber, String password) {
		Request request = new EditUser(username, email, phoneNumber, password);
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		update();
	}

	private void fetchUser() {
		Request request = new GetUser(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		ValueResponse<User> valueResponse = (ValueResponse<User>) response;
		User user = valueResponse.getValue();
		txtUsername.setText(user.getUsername());
		txtEmail.setText(user.getEmail());
		txtPhoneNumber.setText(user.getPhoneNumber());
	}
}
