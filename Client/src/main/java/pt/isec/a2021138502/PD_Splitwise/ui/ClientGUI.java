package pt.isec.a2021138502.PD_Splitwise.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.a2021138502.PD_Splitwise.model.ModelManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientGUI extends Application {
	@Override
	public void init() throws Exception {
		super.init();
		String[] args = getParameters().getRaw().toArray(new String[0]);

		if (args.length != 2) {
			//TODO: show this message on java error box
			System.out.println("Usage: java Client <server> <port>");
			Platform.exit();
			return;
		}

		try {
			InetAddress serverAddr = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);
			ModelManager.getInstance().connect(serverAddr, port);
		} catch ( UnknownHostException e ) { //TODO: Improve this exception handling
			System.out.println("Error: " + e.getMessage());
			Platform.exit();
		} catch ( NumberFormatException e ) { //TODO: Improve this exception handling
			System.out.println("Error: Invalid port number.");
			Platform.exit();
		} //TODO: maybe add more catch blocks
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("root-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load()); //TODO: set scene size
		stage.setOnCloseRequest(e -> ModelManager.getInstance().close());
		stage.setTitle("PD_Splitwise");
		stage.setScene(scene);
		stage.setResizable(false); //TODO: see this latter
		stage.show();
	}
}
