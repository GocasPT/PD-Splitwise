package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import pt.isec.a2021138502.PD_Splitwise.Data.User;

public class UserPreviewController extends Controller {
	@FXML
	public Text tfUsername;
	@FXML
	public Text tfEmail;

	public void build(User user) {
		tfUsername.setText(user.getUsername());
		tfEmail.setText(user.getEmail());
	}
}
