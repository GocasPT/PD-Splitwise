package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isec.a2021138502.PD_Splitwise.Data.Invite;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.GetInvites;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

public class InvitesController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private VBox vbInvites;
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnGroups.setOnAction(e -> ModelManager.getInstance().changeState(EState.GROUPS_PAGE));
		btnInvites.setOnAction(e -> update());
		btnUser.setOnAction(e -> ModelManager.getInstance().changeState(EState.USER_PAGE));
	}

	@Override
	protected void update() {
		EState currentState = ModelManager.getInstance().getState();
		if (currentState == EState.INVITES_PAGE) {
			fetchInvites();
		}
		homePane.setVisible(currentState == EState.INVITES_PAGE);
	}

	private void fetchInvites() {
		Request request = new GetInvites(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess())
			return; //TODO: handle error

		vbInvites.getChildren().clear();
		ListResponse<Invite> listResponse = (ListResponse<Invite>) response;
		Invite[] invites = listResponse.getList();
		try {
			for (Invite invite : invites) {
				FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("invite_preview.fxml"));
				Pane invitePreview = fxmlLoader.load();
				InvitePreviewController controller = fxmlLoader.getController();
				controller.build(invite);
				vbInvites.getChildren().add(invitePreview);
			}
		} catch ( Exception e ) {
			System.out.println("Error loading invite preview");
		}
	}
}
