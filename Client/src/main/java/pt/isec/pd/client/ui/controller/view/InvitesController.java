package pt.isec.pd.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.component.Card;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.database.DTO.Invite.PreviewInviteDTO;
import pt.isec.pd.sharedLib.network.request.Invite.GetInvites;
import pt.isec.pd.sharedLib.network.response.ListResponse;
import pt.isec.pd.sharedLib.network.response.Response;

import java.io.IOException;

public class InvitesController extends BaseController {
	@FXML
	private BorderPane homePane;
	@FXML
	private VBox vbInvites;

	public InvitesController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		//super.registerHandlers();
	}

	@Override
	protected void update() {
		fetchInvites();
	}

	@Override
	protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return; //TODO: handle error
		}

		vbInvites.getChildren().clear();
		ListResponse<PreviewInviteDTO> listResponse = (ListResponse<PreviewInviteDTO>) response; //TODO: check this warning
		PreviewInviteDTO[] invites = listResponse.getList();
		try {
			for (PreviewInviteDTO invite : invites) {
				Button btnAccept = new Button("Accept");
				btnAccept.setOnAction(e -> {
					//TODO: handle accept invite + "disable" card
				});
				Button btnDeny = new Button("Deny");
				btnDeny.setOnAction(e -> {
					//TODO: handle deny invite + "disable" card
				});

				vbInvites.getChildren().add(
						new Card.Builder()
								.id("invite-card")
								.title(invite.groupName())
								.subtitle(invite.inviterUsername() + " <" + invite.inviterEmail() + ">")
								.addButton(btnAccept)
								.addButton(btnDeny)
								.addStyleClass("invite-card")
								.build()
				);
			}
		} catch ( IOException e ) {
			viewManager.showError("Failed to build invites: " + e.getMessage());
		}
	}

	private void fetchInvites() {
		GetInvites request = new GetInvites(modelManager.getEmailLoggedUser());
		Response response = modelManager.sendRequest(request);
		handleResponse(response);
	}
}
