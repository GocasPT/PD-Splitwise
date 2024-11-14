package pt.isec.pd.client.ui.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import net.synedra.validatorfx.Validator;
import org.controlsfx.control.CheckComboBox;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.manager.ViewManager;
import pt.isec.pd.sharedLib.database.DTO.User.PreviewUserDTO;
import pt.isec.pd.sharedLib.network.request.Expense.InsertExpense;
import pt.isec.pd.sharedLib.network.request.Group.GetUsersOnGroup;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class AddExpenseController extends BaseController {
	@FXML
	public Text txtGroupName;
	@FXML
	public TextField tfAmount;
	@FXML
	public TextArea tfDescription;
	@FXML
	public DatePicker datePicker;
	@FXML
	public ComboBox<PreviewUserDTO> cbPayerUser;
	@FXML
	public CheckComboBox<PreviewUserDTO> ccbAssociatedUsers;
	@FXML
	public Button btnAdd;
	@FXML
	public Button btnCancel;

	public AddExpenseController(ViewManager viewManager, ModelManager modelManager) {
		super(viewManager, modelManager);
	}

	@Override
	protected void registerHandlers() {
		int groupId = modelManager.getGroupInViewId();
		GetUsersOnGroup request = new GetUsersOnGroup(groupId);
		Response response = modelManager.sendRequest(request);
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			viewManager.showView("group");
			return;
		}

		//TODO: see docs
		Validator validator = new Validator();
		validator.createCheck()
				.dependsOn("amount", tfAmount.textProperty())
				.withMethod(c -> {
					//TODO; check if
					// not empty
					// is a number
					String amountStr = c.get("amount");
					if (amountStr == null || amountStr.isEmpty()) {
						c.error("Amount is required");
					} else {
						try {
							Double.parseDouble(amountStr);
						} catch ( NumberFormatException e ) {
							c.error("Amount must be a number");
						}
					}
				})
				.decorates(tfAmount)
				.immediate();

		cbPayerUser.getItems().clear();
		cbPayerUser.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(PreviewUserDTO item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.username() + " <" + item.email() + ">");
				}
			}
		});

		ccbAssociatedUsers.getItems().clear();
		ccbAssociatedUsers.setConverter(new StringConverter<>() {
			@Override
			public String toString(PreviewUserDTO object) {
				return object.username() + " <" + object.email() + ">";
			}

			@Override
			public PreviewUserDTO fromString(String string) {
				return null;
			}
		});

		ValueResponse<Map<String, Object>> listResponse = (ValueResponse<Map<String, Object>>) response;

		String groupName = (String) listResponse.getValue().get("group_name");
		txtGroupName.setText(groupName);

		List<PreviewUserDTO> member = (List<PreviewUserDTO>) listResponse.getValue().get("members");
		for (PreviewUserDTO user : member) {
			cbPayerUser.getItems().add(user);
			ccbAssociatedUsers.getItems().add(user);
		}

		btnAdd.setOnAction(e -> {
			addExpense();
		});

		btnCancel.setOnAction(e -> {
			viewManager.showView("group");
		});

		viewManager.hideLoadingIndicator();
	}

	@Override
	protected void update() {

	}

	@Override
	protected void handleResponse(Response response) {
		if (!response.isSuccess()) {
			viewManager.showError(response.getErrorDescription());
			return;
		}

		viewManager.showView("group");
	}

	private void addExpense() {
		//TODO: get information from each input
		String amountStr = tfAmount.getText();
		double amount = Double.parseDouble(amountStr);
		String description = tfDescription.getText();
		long date = datePicker.getValue().atStartOfDay(
				ZoneId.systemDefault()).toEpochSecond(); //TODO: check this later (date format)
		//int payerId = cbPayerUser.getValue().id();
		String payerId = cbPayerUser.getValue().email();
		//int[] associatedUsersId = ccbAssociatedUsers.getCheckModel().getCheckedItems().stream().mapToInt(PreviewUserDTO::id).toArray();
		String[] associatedUsersId = ccbAssociatedUsers.getCheckModel().getCheckedItems().stream().map(
				PreviewUserDTO::email).toArray(String[]::new);

		InsertExpense request = new InsertExpense(
				modelManager.getGroupInViewId(),
				amount,
				description,
				date,
				payerId,
				associatedUsersId
		);
		Response response = modelManager.sendRequest(request);
		handleResponse(response);
		viewManager.hideLoadingIndicator();
	}
}
