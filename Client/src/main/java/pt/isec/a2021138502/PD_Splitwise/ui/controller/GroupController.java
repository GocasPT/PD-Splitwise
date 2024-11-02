package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.InviteUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;
import pt.isec.a2021138502.PD_Splitwise.ui.GUI;

public class GroupController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private Text txtGroupName;
	@FXML
	private Button btnInvite;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnPay;
	@FXML
	private Button btnBalance;
	@FXML
	private Button btnTotalSpend;
	@FXML
	private Button btnExport;
	@FXML
	private VBox vbMembers; //TODO: change to listview/vbox for expenses
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;
	private Group groupInView;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		//TODO: move this to settings group page
		//*
		btnInvite.setOnAction(e -> inviteUserPopUp()); //TODO: popup to select user to invite (dropdown)
		btnEdit.setOnAction(e -> ModelManager.getInstance().changeState(EState.EDIT_GROUP_PAGE));
		/*btnDelete.setOnAction(e -> ); //TODO: popup to confirm delete group
		btnExit.setOnAction(e -> ); //TODO: popup to confirm exit group */
		//*
		/*btnPay.setOnAction(e -> ); //TODO: select user to pay
		btnBalance.setOnAction(e -> ); //TODO: show page with balance (?)
		btnTotalSpend.setOnAction(e -> ); //TODO: show page with total spend/history
		btnExport.setOnAction(e -> ); //TODO: show popup to export csv (name, location, etc) */
		btnGroups.setOnAction(e -> ModelManager.getInstance().changeState(EState.GROUPS_PAGE));
		btnInvites.setOnAction(e -> ModelManager.getInstance().changeState(EState.INVITES_PAGE));
		btnUser.setOnAction(e -> ModelManager.getInstance().changeState(EState.USER_PAGE));
	}

	@Override
	protected void update() {
		EState state = ModelManager.getInstance().getState();
		if (state == EState.GROUP_PAGE) {
			vbMembers.getChildren().clear();

			groupInView = ModelManager.getInstance().getGroupInView();
			txtGroupName.setText(groupInView.getName());
			User[] members = groupInView.getUserList().toArray(User[]::new);
			try {
				for (User member : members) {
					FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("user_preview.fxml"));
					Pane userPreview = fxmlLoader.load();
					UserPreviewController controller = fxmlLoader.getController();
					controller.build(member);
					vbMembers.getChildren().add(userPreview);
				}
			} catch ( Exception e ) {
				System.out.println("Error loading user preview: " + e.getMessage());
			}
		}
		homePane.setVisible(state == EState.GROUP_PAGE);
	}

	private void inviteUserPopUp() {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Invite User");

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10));

		TextField emailField = new TextField();
		emailField.setPromptText("Enter email");

		Button btnInvite = new Button("Invite");
		Button btnCancel = new Button("Cancel");

		btnInvite.setOnAction(e -> {
			String email = emailField.getText();
			if (!email.isEmpty()) {
				inviteUser(email);
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

	private void inviteUser(String inviteUserEmail) {
		String loggedUserEmail = ModelManager.getInstance().getEmailLoggedUser();
		InviteUser request = new InviteUser(groupInView.getId(), inviteUserEmail, loggedUserEmail);
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess()) {
			System.out.println("Failed to invite user: " + response.getErrorDescription());
		}
	}
}
