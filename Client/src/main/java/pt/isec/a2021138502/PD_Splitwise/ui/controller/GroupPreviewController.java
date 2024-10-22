package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.GroupPreview;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.GetGroup;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class GroupPreviewController extends Controller {
	@FXML
	private BorderPane previewPane;
	@FXML
	private Text tfGroupName;
	@FXML
	private Text tfGroupMembers;
	private int groupId;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		previewPane.setOnMouseClicked(e -> fetchGroup());
	}

	//TODO: improve this method (Group → each data maybe)
	public void build(GroupPreview group) {
		this.groupId = group.id();
		tfGroupName.setText(group.name());
		tfGroupMembers.setText(group.memberCount() + " members");
	}

	private void fetchGroup() {
		Request request = new GetGroup(groupId);
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess()) {
			//TODO: handle response errors
			System.out.println(response.getErrorDescription());
			return;
		}

		ValueResponse<Group> valueResponse = (ValueResponse<Group>) response;
		Group group = valueResponse.getValue();
		ModelManager.getInstance().setGroupView(group);
		ModelManager.getInstance().changeState(EState.GROUP_PAGE);
	}
}
