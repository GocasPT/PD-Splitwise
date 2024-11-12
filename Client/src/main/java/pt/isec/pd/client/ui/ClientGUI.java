package pt.isec.pd.client.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.manager.ViewManager;

import java.net.InetAddress;

public class ClientGUI extends Application {
	//private ViewManager viewManager;
	private ModelManager modelManager;

	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(String[]::new);

		if (args.length != 2) {
			//TODO: show this message on java error box if possible
			System.out.println("Usage: java ClientApp <server> <port>");
			Platform.exit();
			return;
		}

		try {
			InetAddress serverAdder = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);

			modelManager = new ModelManager();
			modelManager.connect(serverAdder, port);
			//TODO: Improve catch blocks
			// show error message on MainGUI/Popup
		/*} catch ( UnknownHostException e ) {
			System.out.println("UnknownHostException on 'init': " + e.getMessage());
			Platform.exit();
		} catch ( NumberFormatException e ) {
			System.out.println("NumberFormatException on 'init': " + e.getMessage());
			Platform.exit();
		} catch ( RuntimeException e ) {
			System.out.println("RuntimeException on 'init': " + e.getMessage());
			Platform.exit();*/
		} catch ( Exception e ) {
			throw new RuntimeException("Failed to initialize application", e); //TODO: can I show in a popup?
		}
	}

	@Override
	public void start(Stage primaryStage) {
		ViewManager viewManager = new ViewManager(primaryStage, modelManager);
		primaryStage.setOnCloseRequest(e -> modelManager.close());
		primaryStage.setTitle("PD_Splitwise");
		primaryStage.setResizable(false);

		try {
			viewManager.showView("login_view");
		} catch ( Exception e ) {
			viewManager.showError("Failed to start application: " + e.getMessage());
		}
	}
}
