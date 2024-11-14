package pt.isec.pd.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.component.Card;
import pt.isec.pd.client.ui.component.dialog.CreateGroupDialog;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.database.DTO.Group.PreviewGroupDTO;
import pt.isec.pd.sharedLib.network.request.Group.CreateGroup;
import pt.isec.pd.sharedLib.network.request.Group.GetGroups;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.ListResponse;
import pt.isec.pd.sharedLib.network.response.Response;

import java.io.IOException;

public class GroupsController extends BaseController {
	@FXML
	private Button btnCreateGroup;
	@FXML
	private VBox vbGroups;

	public GroupsController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		btnCreateGroup.setOnAction(e -> {
			try {
				createGroupPopUp();
			} catch ( Exception ex ) {
				viewManager.showError("Failed to create group: " + ex.getMessage());
			}
		});
	}

	@Override
	protected void update() {
		fetchGroups();
	}

	private void createGroupPopUp() {
		try {
			CreateGroupDialog dialog = new CreateGroupDialog(vbGroups.getScene().getWindow());
			dialog.showAndWait().ifPresent(groupName -> {
				//TODO: loading where?

				Request request = new CreateGroup(groupName, modelManager.getEmailLoggedUser());
				Response response = modelManager.sendRequest(request);

				if (!response.isSuccess()) {
					viewManager.showError(response.getErrorDescription());
					return; //TODO: handle error
				}

				fetchGroups();

				//TODO: hide loading
			});
		} catch ( IOException e ) {
			e.printStackTrace();
			viewManager.showError("Failed to create group dialog: " + e);
		}
	}

	private void fetchGroups() {
		GetGroups request = new GetGroups(modelManager.getEmailLoggedUser());
		viewManager.sendRequestAsync(request, this::handleResponse);
	}

	@Override
	protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return; //TODO: handle error
		}

		vbGroups.getChildren().clear();
		ListResponse<PreviewGroupDTO> listResponse = (ListResponse<PreviewGroupDTO>) response; //TODO: check this warning

		if (listResponse.isEmpty()) {
			//TODO: shoe text "No groups found"
			System.out.println("No groups found");
			return; //TODO: show message "No groups found"
		}

		PreviewGroupDTO[] groups = listResponse.getList();
		try {
			for (PreviewGroupDTO group : groups)
				vbGroups.getChildren().add(
						new Card.Builder()
								.id("group-card")
								.title(group.name())
								.subtitle(group.members_number() + " members")
								.onMouseClicked(e -> {
									modelManager.setGroupInViewId(group.id());
									viewManager.showView("group_view");
								})
								.addStyleClass("group-card")
								.build()
				);
		} catch ( IOException e ) {
			viewManager.showError("Failed to show groups: " + e.getMessage());
		}
	}
}
