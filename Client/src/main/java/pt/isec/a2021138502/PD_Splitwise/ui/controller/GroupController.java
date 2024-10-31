package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.model.EState;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

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
	private Button btnDelete;
	@FXML
	private Button btnExit;
	@FXML
	private VBox vbMembers;
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
			Group group = ModelManager.getInstance().getGroupInView();
			//TODO: inject data on page
			txtGroupName.setText(group.getName());

			User[] members = group.getUserList().toArray(User[]::new);
			try {
				for (User member : members) {
					FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("user_preview.fxml"));
					HBox userPreview = fxmlLoader.load();
					UserPreviewController controller = fxmlLoader.getController();
					controller.build(member);
					vbMembers.getChildren().add(userPreview);
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		homePane.setVisible(state == EState.GROUP_PAGE);
	}
}
