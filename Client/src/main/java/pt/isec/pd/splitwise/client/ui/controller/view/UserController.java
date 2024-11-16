package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.component.dialog.EditUserDialog;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.database.DTO.User.DetailUserDTO;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.request.User.EditUser;
import pt.isec.pd.splitwise.sharedLib.network.request.User.GetUser;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.io.IOException;

public class UserController extends BaseController {
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

	public UserController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		//super.registerHandlers();
		btnEdit.setOnAction(e -> {
			try {
				editUSerPopUp();
			} catch ( Exception ex ) {
				viewManager.showError("Failed to edit user: " + ex.getMessage());
			}
		});
	}

	@Override
	protected void update() {
		fetchUser();
	}

	@Override
	protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return;
		}

		ValueResponse<DetailUserDTO> valueResponse = (ValueResponse<DetailUserDTO>) response; //TODO: check this warning
		DetailUserDTO user = valueResponse.getValue();
		txtUsername.setText(user.username());
		txtEmail.setText(user.email());
		txtPhoneNumber.setText(user.phone_number());
	}

	//TODO: improve this (builder pattern)
	private void editUSerPopUp() {
		try {
			EditUserDialog dialog = new EditUserDialog(homePane.getScene().getWindow());
			dialog.showAndWait().ifPresent(
					infoUserDTO -> {
						Request request = new EditUser(
								infoUserDTO.username(),
								infoUserDTO.email(),
								infoUserDTO.email(),
								infoUserDTO.password()
						);

						viewManager.sendRequestAsync(request, (response) -> {
							if (!response.isSuccess()) {
								viewManager.showError(response.getErrorDescription());
								return; //TODO: handle error
							}

							update();
						});
					});
		} catch ( IOException e ) {
			System.out.println("Error loading edit user popup");
		}
	}

	private void fetchUser() {
		GetUser request = new GetUser(modelManager.getEmailLoggedUser());
		viewManager.sendRequestAsync(request, this::handleResponse);
	}
}
