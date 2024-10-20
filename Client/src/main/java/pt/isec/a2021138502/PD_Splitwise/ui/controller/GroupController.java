package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class GroupController extends Controller {
	@FXML
	private BorderPane homePane;
	@FXML
	private Text txtGroupName;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnInvite;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;


	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnEdit.setOnAction(e -> ModelManager.getInstance().changeState(EState.EDIT_GROUP_PAGE));
		//btnInvite.setOnAction(e -> ); //TODO: popup to select user to invite (text field OR dropdown?)
		//btnExit.setOnAction(e -> ); //TODO: popup to confirm exit group
		btnGroups.setOnAction(e -> ModelManager.getInstance().changeState(EState.GROUPS_PAGE));
		btnInvites.setOnAction(e -> ModelManager.getInstance().changeState(EState.INVITES_PAGE));
		btnUser.setOnAction(e -> ModelManager.getInstance().changeState(EState.USER_PAGE));
	}

	@Override
	protected void update() {
		EState state = ModelManager.getInstance().getState();
		if (state == EState.GROUP_PAGE) {
			Group groupView = ModelManager.getInstance().getGroupView();
			//TODO: inject data on page
			txtGroupName.setText(groupView.name());
		}
		homePane.setVisible(state == EState.GROUP_PAGE);
	}
}
