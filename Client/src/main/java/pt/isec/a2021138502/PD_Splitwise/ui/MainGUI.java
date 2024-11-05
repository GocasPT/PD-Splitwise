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

public class MainGUI extends Application {
	private final ModelManager modelManager = ModelManager.getInstance();

	@Override
	public void init() throws Exception {
		super.init();
		String[] args = getParameters().getRaw().toArray(String[]::new);

		if (args.length != 2) {
			//TODO: show this message on java error box
			System.out.println("Usage: java ClientApp <server> <port>");
			Platform.exit();
			return;
		}

		try {
			InetAddress serverAdder = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);
			modelManager.connect(serverAdder, port);
			//TODO: Improve catch blocks
			// show error message on MainGUI/Popup
		} catch ( UnknownHostException e ) {
			System.out.println("UnknownHostException on 'init': " + e.getMessage());
			Platform.exit();
		} catch ( NumberFormatException e ) {
			System.out.println("NumberFormatException on 'init': " + e.getMessage());
			Platform.exit();
		} catch ( RuntimeException e ) {
			System.out.println("RuntimeException on 'init': " + e.getMessage());
			Platform.exit();
		}
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("root-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load()); //TODO: set scene size
		stage.setOnCloseRequest(e -> modelManager.close());
		stage.setTitle("PD_Splitwise");
		stage.setScene(scene);
		stage.setResizable(false); //TODO: see this latter
		stage.show();
	}
}
