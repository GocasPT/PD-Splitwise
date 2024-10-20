package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.GetInvites;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class InvitesController extends Controller {
	@FXML
	private BorderPane homePane;
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
		//TODO: fetch invites from server
		Request request = new GetInvites(ModelManager.getInstance().getEmailLoggedUser());
		Response response = ModelManager.getInstance().sendRequest(request);
		//System.out.println(response);
	}
}
