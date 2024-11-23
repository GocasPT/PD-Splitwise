package pt.isec.pd.splitwise.client.ui.controller.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.StringUtils;
import pt.isec.pd.splitwise.client.model.ModelManager;
import pt.isec.pd.splitwise.client.ui.component.Card;
import pt.isec.pd.splitwise.client.ui.controller.BaseController;
import pt.isec.pd.splitwise.client.ui.manager.ViewManager;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Balance.DetailBalanceDTO;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Balance.PreviewBalanceDTO;
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

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupController extends BaseController {
	private final ObjectProperty<EGroupView> groupViewState;
	@FXML
	public Button btnSettings;
	@FXML
	public Button btnAddExpense;
	@FXML
	private Button btnExpenses;
	@FXML
	private Text txtGroupName;
	@FXML
	private Button btnPay;
	@FXML
	private Button btnBalance; //TODO: show balance (graph, details, etc)
	@FXML
	private Button btnTotalSpend; //TODO: show total spend (total, debs, debs fro each user, etc)
	@FXML
	private Button btnExport;
	@FXML
	private VBox vbInfo;

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
					txtGroupName.setText(group.getName());
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

	private void exportPopup() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		Request request = new ExportCSV(modelManager.getGroupInViewId());
		viewManager.sendRequestAsync(request, (response -> {
			if (!response.isSuccess()) {
				viewManager.showError(response.getErrorDescription());
				return;
			}

			//TODO: get CSV file
			//TODO: download by chunks + progress bar
			if (response instanceof ValueResponse valueResponse) {
				if (valueResponse.getValue() instanceof File file) {
					File outputFolder = directoryChooser.showDialog(vbInfo.getScene().getWindow());
					if (outputFolder != null) {
						File outputFile = new File(outputFolder, file.getName());
						//TODO: write file in folder
					}
				}
			}
		}));
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
											.title(expense.getTitle())
											.subtitle(expense.getAmount() + "€")
											.description(expense.getDate().toString() + " - " + expense.getPayerUser())
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
			vbInfo.getChildren().clear();

			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			if (response instanceof ValueResponse valueResponse)
				if (valueResponse.getValue() instanceof PreviewBalanceDTO balance) {
					for (AbstractMap.SimpleEntry<String, Double> entry : balance.getUsersBalance()) {
						PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
						pieChartData.add(data); //TODO: improve this (onHover → percentage?)
					}

					//TODO: add style to this label
					Label lblTotalExpenses = new Label("Total expenses: " + balance.getTotalBalance() + "€");

					//TODO: why chart is small?
					vbInfo.getChildren().addAll(
							lblTotalExpenses,
							new PieChart(pieChartData)
					);
				} else
					viewManager.showError("Failed to get balance value");
			else
				viewManager.showError("Failed to cast response to ValueResponse");
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
			//TODO: ValueResponse → ListResponse → Map<String, Object>
			if (response instanceof ValueResponse valueResponse) {
				Map<String, DetailBalanceDTO> balance = (Map<String, DetailBalanceDTO>) valueResponse.getValue();

				vbInfo.getChildren().clear();
				for (Map.Entry<String, DetailBalanceDTO> entry : balance.entrySet()) {
					String userEmail = entry.getKey();
					DetailBalanceDTO userBalance = entry.getValue();
					List<Map<String, Double>> debts = new ArrayList<>();
					List<Map<String, Double>> receive = new ArrayList<>();

					//TODO: created 2 card: total debts and total receive
					//TODO list card of users with value (as negative → debt OR as positive → receive)
					userBalance.getDebtList().forEach((k, v) -> debts.add(Map.of(k, v)));
					userBalance.getReceiveList().forEach((k, v) -> receive.add(Map.of(k, v)));

					try {
						Card userCard = new Card.Builder()
								.id("user-expense-card")
								.title(userEmail)
								.subtitle("Total expense: " + userBalance.getTotalExpended() + "€")
								.addContent(
										//TODO: style this (red)
										new VBox(
												new Label("Debts"),
												new Label("Total debts: " + userBalance.getTotalDebt() + "€"),
												new Label(
														(debts.isEmpty() ? "No debts :)"
																: StringUtils //TODO: add bullets points
																.join(
																		debts.stream()
																				.map(Map::entrySet)
																				.map(set -> set.stream()
																						.map(e -> "•" + e.getKey() + ": " + e.getValue() + "€")
																						.findFirst()
																						.orElse("")
																				)
																				.toList(),
																		"\n"
																))
												)
										)
								)
								.addContent(
										//TODO: style this (green)
										new VBox(
												new Label("Receive"),
												new Label("Total receive: " + userBalance.getTotalReceive() + "€"),
												new Label(
														(receive.isEmpty() ? "No receives :("
																: StringUtils //TODO: add bullets points
																.join(
																		receive.stream()
																				.map(Map::entrySet)
																				.map(set -> set.stream()
																						.map(e -> "•" + e.getKey() + ": " + e.getValue() + "€")
																						.findFirst()
																						.orElse("")
																				)
																				.toList(),
																		"\n"
																))
												)
										)
								)
								/*.description( //TODO: improve this (better format)
								              "Debts\nTotal debts: " + userBalance.getTotalDebt() + "€\n" +
								              (debts.isEmpty() ? "No debts :)"
										              : StringUtils //TODO: add bullets points
										              .join(
												              debts.stream()
														              .map(Map::entrySet)
														              .map(set -> set.stream()
																              .map(e -> e.getKey() + ": " + e.getValue() + "€")
																              .findFirst()
																              .orElse("")
														              )
														              .toList(),
												              "\n"
										              )) + "\n\n" +
								              "Receive\nTotal receive: " + userBalance.getTotalReceive() + "€\n" +
								              (receive.isEmpty() ? "No receives :("
										              : StringUtils //TODO: add bullets points
										              .join(
												              receive.stream()
														              .map(Map::entrySet)
														              .map(set -> set.stream()
																              .map(e -> e.getKey() + ": " + e.getValue() + "€")
																              .findFirst()
																              .orElse("")
														              )
														              .toList(),
												              "\n"
										              ))
								)*/
								.build();

						vbInfo.getChildren().add(userCard);
					} catch ( IOException e ) {
						viewManager.showError("Fail to create user card: " + e.getMessage());
					}
				}

				/*
				//TODO: created 2 card: total debts and total receive
				//TODO list card of users with value (as negative → debt OR as positive → receive)
				vbInfo.getChildren().clear();

				vbInfo.getChildren().addAll(
						new Label("Total expense: " + balance.get("totalExpense") + "€"),
						new Label("Amount expense: " + balance.get("amountToReceive") + "€"),
						new Label("--------------------------------------")
				);

				for (Map.Entry<String, Double> member : ((Map<String, Double>) balance.get("debtPerUser")).entrySet()) {
					vbInfo.getChildren().add(new Label(member.getKey() + ": " + member.getValue() + "€"));
				}

				vbInfo.getChildren().add(
						new Label("--------------------------------------")
				);

				for (Map.Entry<String, Double> member : ((Map<String, Double>) balance.get(
						"amountToReceivePerUser")).entrySet()) {
					vbInfo.getChildren().add(new Label(member.getKey() + ": " + member.getValue() + "€"));
				}*/
			}
		}));
	}

	private enum EGroupView {EXPENSES, BALANCE, TOTAL_SPEND}

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
