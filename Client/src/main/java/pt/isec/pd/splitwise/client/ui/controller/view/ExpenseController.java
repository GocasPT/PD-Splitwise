package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public class ExpenseController extends BaseController {
	@FXML public Text txtGroupName;
	@FXML public Text tfAmount;
	@FXML public Text tfDescription;
	@FXML public Text tfDate;
	@FXML public Text tfPayerUser;
	@FXML public Text tfAssociatedUsers;
	@FXML public HBox hbBtn;
	@FXML public Button btnEdit;
	@FXML public Button btnCancel;

	public ExpenseController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override protected void registerHandlers() {
		//TODO: register handlers
	}

	@Override protected void update() {
		//TODO: set text fields
	}

	@Override protected void handleResponse(Response response) {
		//TODO: maybe do nothing...
	}
}
