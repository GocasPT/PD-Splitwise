package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Invite;

public class InvitePreviewController extends Controller {
	@FXML
	private BorderPane previewPane;
	@FXML
	private Text tfUsername;
	@FXML
	private Text tfGroupName;
	@FXML
	private Button btnAccept;
	@FXML
	private Button btnDenid;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnAccept.setOnAction(e -> handleAccept());
		btnDenid.setOnAction(e -> handleDenid());
	}

	public void build(Invite invite) {
		tfUsername.setText(invite.inviterUserEmail());
		tfGroupName.setText(invite.groupName());
	}

	private void handleAccept() {
		//TODO: send accept invite request
	}

	private void handleDenid() {
		//TODO: send denid invite request
	}
}
