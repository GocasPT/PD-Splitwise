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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.CreateGroup;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.GetGroups;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

public class GroupsController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private Button btnCreateGroup;
	@FXML
	private VBox vbGroups;
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnCreateGroup.setOnAction(e -> createGroupPopUp());
		btnGroups.setOnAction(e -> update());
		btnInvites.setOnAction(e -> ModelManager.getInstance().changeState(EState.INVITES_PAGE));
		btnUser.setOnAction(e -> ModelManager.getInstance().changeState(EState.USER_PAGE));
	}

	@Override
	protected void update() {
		EState currentState = ModelManager.getInstance().getState();
		if (currentState == EState.GROUPS_PAGE) {
			fetchGroups();
		}
		homePane.setVisible(currentState == EState.GROUPS_PAGE);
	}

	private void createGroupPopUp() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("create_group_popup.fxml"));
			DialogPane popup = fxmlLoader.load();

			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setScene(new Scene(popup));

			TextField tfGroupName = (TextField) popup.lookup("#tfGroupName");
			ObservableList<ButtonType> buttonTypes = popup.getButtonTypes();
			Button btnCreateGroup = (Button) popup.lookupButton(buttonTypes.get(0));
			Button btnCancelGroup = (Button) popup.lookupButton(buttonTypes.get(1));

			btnCreateGroup.setOnAction(e -> {
				createGroup(tfGroupName.getText());
				popupStage.close();
			});

			btnCancelGroup.setOnAction(e -> popupStage.close());

			popupStage.showAndWait();
		} catch ( Exception e ) {
			System.out.println("Error loading create group popup");
		}
	}

	private void createGroup(String groupName) {
		Request request = new CreateGroup(groupName, ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		fetchGroups();
	}

	private void fetchGroups() {
		Request request = new GetGroups(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		vbGroups.getChildren().clear();
		ListResponse<Group> listResponse = (ListResponse<Group>) response;

		if (listResponse.isEmpty()) {
			System.out.println("No groups found");
			return; //TODO: show message "No groups found"
		}

		Group[] groups = listResponse.getList();
		try {
			for (Group group : groups) {
				FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("group_preview.fxml"));
				Pane groupPreview = fxmlLoader.load();
				GroupPreviewController controller = fxmlLoader.getController();
				controller.build(group);
				vbGroups.getChildren().add(groupPreview);
			}
		} catch ( Exception e ) {
			System.out.println("Error loading group preview");
		}
	}
}
