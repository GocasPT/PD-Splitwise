package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
	private Button btnInvite;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnPay;
	@FXML
	private Button btnBalance;
	@FXML
	private Button btnTotalSpend;
	@FXML
	private Button btnExport;
	@FXML
	private VBox vbMembers; //TODO: change to listview/vbox for expenses
	@FXML
	private Button btnGroups;
	@FXML
	private Button btnInvites;
	@FXML
	private Button btnUser;


	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		//TODO: move this to settings group page
		//*
		//btnInvite.setOnAction(e -> ); //TODO: popup to select user to invite (dropdown)
		btnEdit.setOnAction(e -> ModelManager.getInstance().changeState(EState.EDIT_GROUP_PAGE));
		/*btnDelete.setOnAction(e -> ); //TODO: popup to confirm delete group
		btnExit.setOnAction(e -> ); //TODO: popup to confirm exit group */
		//*
		/*btnPay.setOnAction(e -> ); //TODO: select user to pay
		btnBalance.setOnAction(e -> ); //TODO: show page with balance (?)
		btnTotalSpend.setOnAction(e -> ); //TODO: show page with total spend/history
		btnExport.setOnAction(e -> ); //TODO: show popup to export csv (name, location, etc) */
		btnGroups.setOnAction(e -> ModelManager.getInstance().changeState(EState.GROUPS_PAGE));
		btnInvites.setOnAction(e -> ModelManager.getInstance().changeState(EState.INVITES_PAGE));
		btnUser.setOnAction(e -> ModelManager.getInstance().changeState(EState.USER_PAGE));
	}

	@Override
	protected void update() {
		EState state = ModelManager.getInstance().getState();
		if (state == EState.GROUP_PAGE) {
			vbMembers.getChildren().clear();

			Group group = ModelManager.getInstance().getGroupInView();
			txtGroupName.setText(group.getName());
			User[] members = group.getUserList().toArray(User[]::new);
			try {
				for (User member : members) {
					FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("user_preview.fxml"));
					Pane userPreview = fxmlLoader.load();
					UserPreviewController controller = fxmlLoader.getController();
					controller.build(member);
					vbMembers.getChildren().add(userPreview);
				}
			} catch ( Exception e ) {
				System.out.println("Error loading user preview: " + e.getMessage());
			}
		}
		homePane.setVisible(state == EState.GROUP_PAGE);
	}
}
