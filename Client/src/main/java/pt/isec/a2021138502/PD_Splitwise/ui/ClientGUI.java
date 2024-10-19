package pt.isec.a2021138502.PD_Splitwise.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientGUI extends Application {
	private static Stage primaryStage;
	private static InetAddress serverAddr;
	private static int port;

	public static InetAddress getServerAddr() {
		return serverAddr;
	}

	public static int getPort() {
		return port;
	}

	//TODO: see this method (use this approach OR use PA method?)
	public static void switchScene(String fxml) throws IOException {
		FXMLLoader loader = new FXMLLoader(ClientGUI.class.getResource(fxml));
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
	}

	@Override
	public void init() throws Exception {
		super.init();
		String[] args = getParameters().getRaw().toArray(new String[0]);

		if (args.length != 2) {
			System.err.println("Usage: java Client <server> <port>");
			Platform.exit();
			return;
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		} catch (UnknownHostException e) { //TODO: Improve this exception handling
			System.err.println("Error: " + e.getMessage());
			Platform.exit();
		} catch (NumberFormatException e) { //TODO: Improve this exception handling
			System.err.println("Error: Invalid port number.");
			Platform.exit();
		}
	}

	@Override
	public void start(Stage stage) throws IOException {
		primaryStage = stage;
		switchScene("login.fxml");
		primaryStage.show();
	}
}
