package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.Invite;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Group.InviteResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class InvitePreviewController extends Controller {
	@FXML
	private VBox previewPane;
	@FXML
	private Text tfUsername;
	@FXML
	private Text tfGroupName;
	@FXML
	private Button btnAccept;
	@FXML
	private Button btnDenid;
	private int inviteId;

	@Override
	public void initialize() {
		registerHandlers();
	}

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		btnAccept.setOnAction(e -> handleRequest(true));
		btnDenid.setOnAction(e -> handleRequest(false));
	}

	@Override
	protected void update() {
		previewPane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		btnAccept.setDisable(true);
		btnDenid.setDisable(true);
	}

	private void handleRequest(boolean accept) {
		Request request = new InviteResponse(inviteId, accept);
		Response response = ModelManager.getInstance().sendRequest(request); //TODO: need feedback?
		if (response.isSuccess())
			update();
	}

	public void build(Invite invite) {
		this.inviteId = invite.getId();
		tfUsername.setText(invite.getUserThatInvites());
		tfGroupName.setText(invite.getGroupName());
	}
}
