package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
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
		ModelManager.getInstance().setGroupInView(group);
		ModelManager.getInstance().changeState(EState.GROUP_PAGE);
	}

	//TODO: improve this method (Group → each data maybe)
	public void build(Group group) {
		this.groupId = group.getId();
		tfGroupName.setText(group.getName());
		tfGroupMembers.setText(group.getNumUsers() + " members");
	}
}
