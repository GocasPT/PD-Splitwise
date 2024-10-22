package pt.isec.a2021138502.PD_Splitwise.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

import java.io.IOException;

public class RootViewController {
	@FXML
	private StackPane stackPane;

	@FXML
	public void initialize() {
		createViews();
	}

	private void createViews() {
		loadView("login.fxml");
		loadView("register.fxml");
		loadView("groups_page.fxml");
		loadView("group_page.fxml");
		loadView("invites_page.fxml");
		loadView("user_page.fxml");
	}

	private void loadView(String fxmlName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource(fxmlName));
			Parent child = fxmlLoader.load();
			stackPane.getChildren().add(child);
		} catch ( IOException e ) {
			System.out.println("Error loading view: " + fxmlName);
		}
	}
}
