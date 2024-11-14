package pt.isec.pd.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.component.Card;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.database.DTO.Expense.PreviewExpenseDTO;
import pt.isec.pd.sharedLib.database.DTO.Group.DetailGroupDTO;
import pt.isec.pd.sharedLib.database.DTO.Payment.PreviewPaymentDTO;
import pt.isec.pd.sharedLib.database.DTO.User.PreviewUserDTO;
import pt.isec.pd.sharedLib.network.request.Group.GetGroup;
import pt.isec.pd.sharedLib.network.request.Invite.InviteUser;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

import java.io.IOException;

public class GroupController extends BaseController {
	@FXML
	private BorderPane homePane;
	@FXML
	public Button btnAddExpense;
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
	private VBox vbExpenses; //TODO: change to listview/vbox for expenses
	@FXML
	private VBox vbPayments; //TODO: change to listview/vbox for expenses
	private DetailGroupDTO groupInView;

	public GroupController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		//super.registerHandlers();
		//TODO: move this to settings group page
		//*
		btnInvite.setOnAction(e -> {
			try {
				inviteUserPopUp();
			} catch ( Exception ex ) {
				viewManager.showError("Failed to invite user: " + ex.getMessage());
			}
		}); //TODO: popup to select user to invite (dropdown)
		btnEdit.setOnAction(e -> {
			try {
				viewManager.showView("edit_group_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to edit group: " + ex.getMessage());
			}
		});
		/*btnDelete.setOnAction(e -> {
			try {
				//TODO: popup to confirm delete group
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show delete popup: " + ex.getMessage());
			}
		}); //TODO: popup to confirm delete group
		btnExit.setOnAction(e -> {
			try {
				//TODO: popup to confirm exit group
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show exit popup: " + ex.getMessage());
			}
		}); //TODO: popup to confirm exit group */
		btnAddExpense.setOnAction(e -> {
			try {
				viewManager.showView("add_expense_view");
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show add expense: " + ex.getMessage());
			}
		});
		//*
		/*btnPay.setOnAction(e -> {
			try {
				//TODO: show page with balance (?)
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show (PAYMENT): " + ex.getMessage()); ///TODO: change this
			}
		});
		btnBalance.setOnAction(e -> {
			try {
				//TODO: show page with balance (?)
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show (BALANCE): " + ex.getMessage()); //TODO: change this
			}
		});
		 */
		/*
		btnTotalSpend.setOnAction(e -> {
			try {
				//TODO: show page with total spend/history
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show total spend page: " + ex.getMessage());
			}
		});
		btnExport.setOnAction(e -> {
			try {
				//TODO: show popup to export csv (name, location, etc)
			} catch ( Exception ex ) {
				viewManager.showError("Failed to show export popup: " + ex.getMessage());
			}
		});*/
	}

	@Override
	protected void handleResponse(Response response) {
	}

	//TODO: improve this (builder pattern)

	private void inviteUserPopUp() {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Invite User");

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10));

		TextField emailField = new TextField();
		emailField.setPromptText("Enter email");

		Button btnInvite = new Button("Invite");
		Button btnCancel = new Button("Cancel");

		btnInvite.setOnAction(e -> {
			String email = emailField.getText();
			if (!email.isEmpty()) {
				inviteUser(email);
				popupStage.close();
			}
		});

		btnCancel.setOnAction(e -> popupStage.close());

		HBox hbox = new HBox(10, btnInvite, btnCancel);
		hbox.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(new Label("Email:"), emailField, hbox);

		Scene scene = new Scene(vbox);
		popupStage.setScene(scene);
		popupStage.showAndWait();
	}

	@Override
	protected void update() {
		GetGroup request = new GetGroup(modelManager.getGroupInViewId());
		//TODO: viewManager.sendRequestAsync(request, this::handleResponse);
		Response response = modelManager.sendRequest(request);

		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			viewManager.showView("groups_view");
			return;
		}

		if (response instanceof ValueResponse valueResponse) {
			if (valueResponse.getValue() instanceof DetailGroupDTO group) {
				groupInView = group;
				txtGroupName.setText(group.name());
			} else {
				viewManager.showError("Failed to get group value");
				return;
			}
		} else {
			viewManager.showError("Failed to cast response to ValueResponse");
			return;
		}

		//TODO: what fetch to show on groups? fetch all and separate for each tab?
		fetchMembers();
		fetchExpenses();
		fetchPayments();
	}

	//TODO: check this later

	private void fetchMembers() {
		vbMembers.getChildren().clear();

		PreviewUserDTO[] members = groupInView.members().toArray(PreviewUserDTO[]::new);
		try {
			for (PreviewUserDTO member : members)
				vbMembers.getChildren().add(
						new Card.Builder()
								.id("member-card")
								.title(member.username())
								.description(member.email())
								.addStyleClass("member-card")
								.build()
				);
		} catch ( IOException e ) {
			viewManager.showError("Failed to fetch members: " + e.getMessage());
		}
	}

	private void fetchExpenses() {
		vbExpenses.getChildren().clear();

		PreviewExpenseDTO[] expenses = groupInView.expenses().toArray(PreviewExpenseDTO[]::new);
		try {
			for (PreviewExpenseDTO expense : expenses)
				vbExpenses.getChildren().add(
						new Card.Builder()
								.id("expense-card")
								.title(expense.amount() + "€")
								.description(expense.buyEmail())
								.addStyleClass("expense-card")
								.build()
				);
		} catch ( IOException e ) {
			viewManager.showError("Failed to fetch expenses: " + e.getMessage());
		}
	}

	private void fetchPayments() {
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
	}

	private void inviteUser(String inviteUserEmail) {
		String loggedUserEmail = modelManager.getEmailLoggedUser();
		InviteUser request = new InviteUser(groupInView.id(), inviteUserEmail, loggedUserEmail);
		//TODO: viewManager.sendRequestAsync(request, this::handleResponse);
		Response response = modelManager.sendRequest(request);

		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
		}
	}
}
