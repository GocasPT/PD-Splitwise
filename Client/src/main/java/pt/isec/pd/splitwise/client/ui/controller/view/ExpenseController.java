package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Expense.DetailExpenseDTO;
import pt.isec.pd.splitwise.sharedLib.network.request.Expense.GetExpense;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

public class ExpenseController extends BaseController {
	@FXML public Text txtGroupName;
	@FXML public Text tfAmount;
	@FXML public Text tfDescription;
	@FXML public Text tfDate;
	@FXML public Text tfPayerUser;
	@FXML public Text tfAssociatedUsers;
	@FXML public HBox hbBtn;
	@FXML public Button btnEdit;
	@FXML public Button btnBack;

	public ExpenseController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override protected void registerHandlers() {
		btnEdit.setOnAction(e -> viewManager.showView("edit_expense_view"));
		btnBack.setOnAction(e -> viewManager.showView("group_view"));
	}

	@Override protected void update() {
		viewManager.sendRequestAsync(new GetExpense(modelManager.getExpenseInViewId()), this::handleResponse);
	}

	@Override protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return;
		}

		if (response instanceof ValueResponse<?> valueResponse)
			if (valueResponse.getValue() instanceof DetailExpenseDTO expense) {
				tfAmount.setText(String.valueOf(expense.getAmount()));
				tfDescription.setText(expense.getTitle());
				tfDate.setText(expense.getDate().toString());
				tfPayerUser.setText(expense.getPayerUser());
				tfAssociatedUsers.setText(expense.getAssociatedUsersList().toString()); //TODO: maybe improve this
			} else
				viewManager.showError("Failed to get expense: Invalid response value");
		else
			viewManager.showError("Failed to get expense: Invalid response type");
	}
}
