package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.component.Card;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Expense.DetailExpenseDTO;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Group.PreviewGroupDTO;
import pt.isec.pd.splitwise.sharedLib.network.request.Expense.ExportCSV;
import pt.isec.pd.splitwise.sharedLib.network.request.Expense.GetHistory;
import pt.isec.pd.splitwise.sharedLib.network.request.Expense.GetTotalExpenses;
import pt.isec.pd.splitwise.sharedLib.network.request.Group.GetGroup;
import pt.isec.pd.splitwise.sharedLib.network.request.Payment.ViewBalance;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.ListResponse;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.io.IOException;

public class GroupController extends BaseController {
	@FXML
	public Button btnSettings;
	@FXML
	private Button btnExpenses;
	@FXML
	public Button btnAddExpense;
	@FXML
	private Text txtGroupName;
	@FXML
	private Button btnPay;
	@FXML
	private Button btnBalance;
	@FXML
	private Button btnTotalSpend;
	@FXML
	private Button btnExport;
	@FXML
	private VBox vbInfo;

	private enum EGroupView { EXPENSES, BALANCE, TOTAL_SPEND }
	private final ObjectProperty<EGroupView> groupViewState;

	public GroupController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
		groupViewState = new SimpleObjectProperty<>(EGroupView.EXPENSES);
	}

	@Override
	protected void registerHandlers() {
		btnSettings.setOnAction(e -> {
			try {
				viewManager.showView("settings_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show settings: " + ex.getMessage());
			}
		});

		btnExpenses.setOnAction(e -> groupViewState.set(EGroupView.EXPENSES));

		btnAddExpense.setOnAction(e -> {
			try {
				viewManager.showView("add_expense_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show add expense: " + ex.getMessage());
			}
		});

		btnPay.setOnAction(e -> {
			try {
				viewManager.showView("payment_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show add expense: " + ex.getMessage());
			}
		});

		btnBalance.setOnAction(e -> groupViewState.set(EGroupView.BALANCE));
		btnTotalSpend.setOnAction(e -> groupViewState.set(EGroupView.TOTAL_SPEND));
		btnExport.setOnAction(e -> exportPopup());

		groupViewState.addListener((observable, oldValue, newValue) -> {
			if (oldValue == newValue) return;

			//TODO: style buttons
			switch (newValue) {
				case EXPENSES -> fetchExpenses();
				case BALANCE -> fetchBalance();
				case TOTAL_SPEND -> fetchTotalSpend();
			}
		});
	}

	@Override
	protected void update() {
		Request request = new GetGroup(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				viewManager.showView("groups_view");
				return;
			}

			if (response instanceof ValueResponse valueResponse) {
				if (valueResponse.getValue() instanceof PreviewGroupDTO group) {
					txtGroupName.setText(group.name());
				} else {
					viewManager.showError("Failed to get group value");
				}
			} else {
				viewManager.showError("Failed to cast response to ValueResponse");
			}

			fetchExpenses();
		}));
	}

	@Override
	protected void handleResponse(Response response) {
	}

	private void fetchExpenses() {
		Request request = new GetHistory(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				viewManager.showView("groups_view");
				return;
			}

			if (response instanceof ListResponse listResponse) {
				if (listResponse.isEmpty()) {
					System.out.println("No expenses found");
					return; //TODO: show message "No expenses found"
				}

				if (listResponse.getList() instanceof DetailExpenseDTO[] expenses) {


					vbInfo.getChildren().clear();
					try {
						for (DetailExpenseDTO expense : expenses)
							//TODO: add month separator (new month → new separator)
							vbInfo.getChildren().add(
									new Card.Builder()
											.id("expense-card")
											//.avatar() //TODO: category icon using ikonli icons
											.title(expense.amount() + "€")
											.subtitle(expense.date().toString())
											.description(expense.user())
											.onMouseClicked(e -> {
												//TODO: show expense details
												//expense.id()
											})
											.addStyleClass("expense-card")
											.build()
							);
					} catch ( IOException e ) {
						viewManager.showError("Failed to fetch expenses: " + e.getMessage());
					}
				} else {
					viewManager.showError("Failed to get expenses list");
				}
			} else {
				viewManager.showError("Failed to cast response to ListResponse");
			}
		}));
	}

	private void fetchBalance() {
		Request request = new GetTotalExpenses(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				return;
			}

			//TODO: show balance
		}));
	}

	private void fetchTotalSpend() {
		Request request = new ViewBalance(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				return;
			}

			//TODO: show view balance with graph + details
		}));
	}

	private void exportPopup() {
		Request request = new ExportCSV(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				return;
			}

			//TODO: get CSV file
		}));
	}

	/*private void fetchPayments() {
		vbPayments.getChildren().clear();

		PreviewPaymentDTO[] payments = groupInView.payments().toArray(PreviewPaymentDTO[]::new);
		try {
			for (PreviewPaymentDTO payment : payments)
				vbPayments.getChildren().add(
						new Card.Builder()
								.id("payment-card")
								.title(payment.amount() + "€")
								.description("Payment")
								.addStyleClass("payment-card")
								.build()
				);
		} catch ( IOException e ) {
			viewManager.showError("Failed to fetch payments: " + e.getMessage());
		}
	}*/


}
