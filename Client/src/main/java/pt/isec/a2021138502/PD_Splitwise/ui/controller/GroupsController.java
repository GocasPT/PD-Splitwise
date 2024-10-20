package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
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

	private void fetchGroups() {
		//TODO: fetch groups from server
		Request request = new GetGroups(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);
		//System.out.println(response);
		vbGroups.getChildren().clear();
		ListResponse<Group> listResponse = (ListResponse<Group>) response;
		Group[] groups = listResponse.getList();
		try {
			for (Group group : groups) {
				FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("group_preview.fxml"));
				BorderPane groupPreview = fxmlLoader.load();
				GroupPreviewController controller = fxmlLoader.getController();
				controller.build(group);
				vbGroups.getChildren().add(groupPreview);
			}
		} catch (Exception ex) {
			System.out.println("Error loading group preview");
		}
	}
}
