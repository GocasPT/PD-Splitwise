package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;

public class GroupPreviewController extends Controller {
	@FXML
	private BorderPane previewPane;
	@FXML
	private Text tfGroupName;
	@FXML
	private Text tfGroupMembers;
	private Group group; //TODO: change to group id (dont save object on client-side, request data from server)

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		//TODO: when click on previewPane, change to group page[id]
	}

	public void build(Group group) {
		this.group = group;
		tfGroupName.setText(group.name());
		tfGroupMembers.setText(group.users().length + " members");
	}
}
