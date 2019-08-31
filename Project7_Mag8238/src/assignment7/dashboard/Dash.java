package dashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import application.ClientThread;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Dash extends Application {

	private static ClientThread myThread;
	private Scene notificationsScene;
	private Scene friendScene;
	private Scene settingsScene;
	
	public Dash(ClientThread myThread2,Stage stage) {
		myThread = myThread2;
		start(stage);
	}


	@Override
	public void start(Stage primaryStage) {

		try {

			FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
			DashController dashController = new DashController();
			dashLoader.setController(dashController);
			AnchorPane dashboardRoot = (AnchorPane) dashLoader.load();
			Scene dashboard = new Scene(dashboardRoot);
			dashboard.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Sets the Chats scene
			FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("Dashboard-Chats.fxml"));
			DashChatsController dashChatController = new DashChatsController();
			chatLoader.setController(dashChatController);
			AnchorPane dashboardChatRoot = (AnchorPane) chatLoader.load();
			Scene chatScene = new Scene(dashboardChatRoot);
			chatScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// sets the notifications scene
			FXMLLoader notificationsLoader = new FXMLLoader(getClass().getResource("Dashboard-Notifications.fxml"));
			DashNotificationsController dashNotificationsController = new DashNotificationsController();
			notificationsLoader.setController(dashNotificationsController);
			
			AnchorPane dashboardNotificationsRoot = (AnchorPane) notificationsLoader.load();
			Scene notificationsScene = new Scene(dashboardNotificationsRoot);
			notificationsScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// sets the Friends scene
			FXMLLoader friendsLoader = new FXMLLoader(getClass().getResource("Dashboard-Chats.fxml"));
			DashFriendsController dashFriendsController = new DashFriendsController();
			friendsLoader.setController(dashFriendsController);
			
			AnchorPane dashboardFriendsRoot = (AnchorPane) friendsLoader.load();
			Scene friendScene = new Scene(dashboardFriendsRoot);
			friendScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// sets the settingLoader
			FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("Dashboard-Chats.fxml"));
			
			DashSettingsController dashSettingsController1 = new DashSettingsController();
			settingsLoader.setController(dashSettingsController1);
			AnchorPane dashboardSettingsRoot = (AnchorPane) settingsLoader.load();
			Scene settingsScene = new Scene(dashboardSettingsRoot);
			settingsScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			dashController.dashInit(primaryStage, myThread, chatScene, notificationsScene, friendScene, settingsScene);


			// Sets the screen to the dashboard
			primaryStage.setScene(dashboard);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	
}
