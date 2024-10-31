package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.GetUser;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

public class UserPreviewController extends Controller {
	@FXML
	public HBox hb;
	@FXML
	public Text tfUsername;
	@FXML
	public Text tfEmail;

	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		hb.setOnMouseClicked(e -> fetchUser());
	}

	private void fetchUser() {
		Request request = new GetUser(tfEmail.getText());
		Response response = ModelManager.getInstance().sendRequest(request);

		if (!response.isSuccess()) {
			//TODO: handle response errors
			System.out.println(response.getErrorDescription());
			return;
		}

		ValueResponse<User> valueResponse = (ValueResponse<User>) response;
		User user = valueResponse.getValue();
		/*ModelManager.getInstance().setGroupInView(group);
		ModelManager.getInstance().changeState(EState.GROUP_PAGE);*/
	}

	public void build(User user) {
		tfUsername.setText(user.getUsername());
		tfEmail.setText(user.getEmail());
	}
}
