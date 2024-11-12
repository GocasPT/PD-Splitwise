package pt.isec.pd.client.ui.manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pd.client.ClientApp;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.ui.controller.BaseController;
import pt.isec.pd.client.ui.controller.ControllerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
	private final Stage primaryStage;
	private final ModelManager modelManager;
	private final Map<String, FXMLLoader> loaderCache;

	public ViewManager(Stage primaryStage, ModelManager modelManager) {
		this.primaryStage = primaryStage;
		this.modelManager = modelManager;
		this.loaderCache = new HashMap<>();
	}

	public void showView(String viewName) {
		try {
			Parent view = loadView(viewName);
			Scene scene = primaryStage.getScene();
			if (scene == null) {
				scene = new Scene(view);
				primaryStage.setScene(scene);
			} else
				scene.setRoot(view);
			primaryStage.show();
		} catch ( Exception e ) {
			showError("Failed to show " + viewName + ": " + e);
		}
	}

	private Parent loadView(String viewName) {
		try {
			String fxmlPath = "views/" + viewName + ".fxml";
			FXMLLoader loader = new FXMLLoader(
					ClientApp.class.getResource(fxmlPath)
			);
			loader.setControllerFactory(this::createController);
			loaderCache.put(viewName, loader);
			return loader.load();
		} catch ( IOException e ) {
			e.printStackTrace();
			showError("Failed to load view '" + viewName + "': " + e);
			return new Parent() {
			}; //TODO: return null (?)
		}
	}

	public void showError(String message) {
		//TODO: Implement error dialog
		System.out.println("Error: " + message);
	}

	private BaseController createController(Class<?> type) {
		return ControllerFactory.getController(type.asSubclass(BaseController.class), this, modelManager);
	}

	public <T extends BaseController> T getController(String viewName) {
		FXMLLoader loader = loaderCache.get(viewName);
		if (loader == null) {
			throw new IllegalStateException("View " + viewName + " has not been loaded");
		}
		return loader.getController();
	}

	public void clearCache() {
		loaderCache.clear();
	}
}
