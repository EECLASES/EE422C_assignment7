package dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.ClientThread;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashSettingsController implements Initializable{

	   @FXML
	    private TextField userInput;

	    @FXML
	    private Button createChat;
		
		@FXML
	    private FontAwesomeIconView minimizeIcon;

	    @FXML
	    private FontAwesomeIconView exitIcon;
	    
		@FXML
	    private TextField searchBar;

	    @FXML
	    private HBox panelDash;

	    @FXML
	    private HBox panelNotifications;

	    @FXML
	    private HBox panelFriends;

	    @FXML
	    private HBox panelBroadcast;

	    @FXML
	    private HBox panelChats;

	    @FXML
	    private HBox panelSettings;

	    @FXML
	    private Label friend1;

	    @FXML
	    private Label friend2;

	    @FXML
	    private Label friend3;

	    @FXML
	    private Button addFriend;

	    @FXML
	    private Label dashNotIcon;

	    @FXML
	    private FontAwesomeIconView dashNotificationBell;

	    @FXML
	    private FontAwesomeIconView dashSettingsIcon;

	    @FXML
	    private Label timeActive;

	    @FXML
	    private Label timeActive1;

	    @FXML
	    private Label timeActive2;

	    @FXML
	    private ProgressIndicator MostPopularChat;

	    @FXML
	    private ProgressBar privateChats;

	    @FXML
	    private ProgressIndicator totalFriends;
	    
	    

	    @FXML
	    void buttonAddFriend(ActionEvent event) {

	    }

	    @FXML
	    void putIntoSearchbar(ActionEvent event) {

	    }

	    @FXML
	    void switchToBroadcasts(MouseEvent event) {

	    }

	    @FXML
	    void switchToChats(MouseEvent event) throws IOException {
	    	dashChats();
	    }

	    @FXML
	    void switchToDash(MouseEvent event) {

	    }

	    @FXML
	    void switchToFriends(MouseEvent event) {

	    }

	    @FXML
	    void switchToNotifications(MouseEvent event) {

	    }

	    @FXML
	    void switchToSettings(MouseEvent event) {

	    }
	    @FXML
	    void exitProgram(MouseEvent event) {
	    	System.exit(1);
	    }

	    @FXML
	    void lowerScreen(MouseEvent event) {

	    }
		
	    @FXML
	    void createNewChat(ActionEvent event) {
	    	myThread.setState("newGroupChat");
	    	myThread.sendWriter(userInput.getText().trim());
	    	
	    }
		
		private Stage stage;
		private Stage switchStaged = new Stage();
		
		
		
		
		
		private ClientThread myThread;
		
		
		
		

	    @FXML
	    private VBox ChatList;

	    @FXML
	    private VBox ChatList1;

		
		
		public DashSettingsController() {
			super();
			
//			myThread.getDashInfo();
		}

		 @Override
			public void initialize(URL arg0, ResourceBundle arg1) {
				
				
				
				
			}
		 
		 
		 public void dash() throws IOException {
			 
			 timeActive.setText(myThread.getTime());
				timeActive1.setText(Integer.toString(myThread.getMessages().size()));
				timeActive2.setText(Integer.toString(myThread.getFriends().size()));
				this.dashNotIcon.setText(Integer.toString(myThread.getListOfNotifications().size()));
				if(myThread.getFriends().size()>2) {
					friend1.setText(myThread.getFriends().get(1));
					friend2.setText(myThread.getFriends().get(2));
					friend3.setText(myThread.getFriends().get(3));
				}
			 
			 
			 switchStaged.setScene(dashScene);
			 switchStaged.show();
			 
			 stage.close();
			 
		 }
		 public void dashChats() throws IOException {
			 
			 
			 
			

			
			 switchStaged.setScene(chatScene);
			 switchStaged.show();
			 
			 stage.close();
			 
		 }
		 
		 
		 
		 
		 @FXML
		 private VBox notificationsList;
		 
		 private FXMLLoader dashLoader;
		 
		private Scene dashScene; 
		private Scene chatScene;
		private Scene notificationScene;
		private Scene friendScene;
		private Scene settingScene;
		 
//		 public void dashNotifications() throws IOException{
//			 for(String input: myThread.getListOfNotifications() ) {
//				 Label newLabel = new Label();
//				 newLabel.setText(input);
//				 notificationsList.getChildren().add(newLabel);
//			 }
//			 
//			 
//			 switchStaged.setScene(notificationScene);
//			 switchStaged.show();
//			 
//			 
//		 }
//		 //todo
//		 public void dashFriends() throws IOException{
////			 for(String input: myThread.getFriends()) {
////				 Label newLabel = new Label();
////				 newLabel.setText(input);
////				 ChatList.getChildren().add(newLabel);
////			 }
//			 
//			 
//			 switchStaged.setScene(friendScene);
//			 switchStaged.show();
//			 
//			 
//		 }
//		 //todo
//		 public void dashSettings() throws IOException{
////			 for(String input: friends) {
////				 Label newLabel = new Label();
////				 newLabel.setText(input);
////				 ChatList.getChildren().add(newLabel);
////			 }
//			 
//			 FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("Dashboard-Chats.fxml"));
//			 
//			 switchStaged.setScene(settingScene);
//			 switchStaged.show();
//			 
//			 
//		 }

		public void dashChatsInit(Stage stage, ClientThread myThread, Scene chatRoot, Scene notification, Scene friend, Scene setting) {
			this.stage = stage;
			this.myThread = myThread;
			this.chatScene = chatRoot;
			this.notificationScene = notification;
			this.friendScene = friend;
			this.settingScene = setting;
			
		}
		 

	}
