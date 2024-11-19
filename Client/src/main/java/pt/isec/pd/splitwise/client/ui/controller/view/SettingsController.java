package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.component.Card;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.database.DTO.User.DetailUserDTO;
import pt.isec.pd.splitwise.sharedLib.database.DTO.User.PreviewUserDTO;
import pt.isec.pd.splitwise.sharedLib.network.request.Group.GetMembersGroup;
import pt.isec.pd.splitwise.sharedLib.network.request.Invite.InviteUser;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.ListResponse;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.io.IOException;

public class SettingsController extends BaseController {
	@FXML
	public Button btnClose;
	@FXML
	private Button btnInvite;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnExit;
	@FXML
	private VBox vbMembers;

	public SettingsController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnClose.setOnAction(e -> viewManager.showView("group_view"));
		btnInvite.setOnAction(e -> inviteUserPopup());
		btnEdit.setOnAction(e -> editNamePopup());
		btnDelete.setOnAction(e -> deleteGroupPopup());
		btnExit.setOnAction(e -> exitGroupPopup());
	}

	@Override
	protected void update() {
		fetchMembers();
	}

	private void fetchMembers() {
		Request request = new GetMembersGroup(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, this::handleResponse);
	}

	@Override
	protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return;
		}

		if (response instanceof ListResponse listResponse) {
			if (listResponse.isEmpty()) {
				viewManager.showError("No members found");
				return;
			}

			if (listResponse.getList() instanceof DetailUserDTO[] members) {
				vbMembers.getChildren().clear();

				try {
					for (DetailUserDTO member : members)
						vbMembers.getChildren().add(
								new Card.Builder()
										.id("member-card")
										.title(member.username())
										.subtitle(member.email())
										.description(member.phoneNumber())
										.addStyleClass("member-card")
										.build()
						);
				} catch ( IOException e ) {
					viewManager.showError("Failed to fetch members: " + e.getMessage());
				}
			} else
				viewManager.showError("Failed to get members list");
		} else
			viewManager.showError("Failed to cast response to ListResponse");
	}

	private void inviteUserPopup() {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Invite User");

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10));

		TextField emailField = new TextField();
		emailField.setPromptText("Enter userEmail");

		Button btnInvite = new Button("Invite");
		Button btnCancel = new Button("Cancel");

		btnInvite.setOnAction(e -> {
			String email = emailField.getText();
			if (!email.isEmpty()) {
				String loggedUserEmail = modelManager.getEmailLoggedUser();
				Request request = new InviteUser(modelManager.getGroupInViewId(), email, loggedUserEmail);
				viewManager.sendRequestAsync(request, (response -> {
					if (!response.isSuccess()) {
						viewManager.showError(response.getErrorDescription());
					}
				}));

				popupStage.close();
			}
		});

		btnCancel.setOnAction(e -> popupStage.close());

		HBox hbox = new HBox(10, btnInvite, btnCancel);
		hbox.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(new Label("Email:"), emailField, hbox);

		Scene scene = new Scene(vbox);
		popupStage.setScene(scene);
		popupStage.showAndWait();
	}

	private void editNamePopup() {

	}

	private void deleteGroupPopup() {

	}

	private void exitGroupPopup() {

	}
}
