package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dashboard.Dash;
import dashboard.DashController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{

	private ClientThread myThread;

    public LoginController(Stage window) throws UnknownHostException, IOException {
		super();
		this.window = window;
		myThread = new ClientThread();
		
	}
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@FXML
    private  TextField username;

    @FXML
    private  TextField password;

    @FXML
    private  Button loginButton;
    
    @FXML
    private  Label text;
    

    @FXML
    void handleCancelButtonAction(ActionEvent event) {
    	myThread.setState("quit");
    	myThread.sendWriter("");
    	
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
    	//Checks if it can open the dash
    	if(myThread.isOpenDashBool()) {
    		mediaPlayerDashLogin.play();
    		openDash();
    	}
    	//otherwise process a name change
    	myThread.setDashReady(true);
    	if(username.getText().length() > 0 && password.getText().length() > 0 && myThread.isNewUser() ==false)
    	myThread.sendWriter(username.getText() + " " + password.getText());
    	else if(myThread.isNewUser() && username.getText().length() > 0 && password.getText().length() > 0) {
    		myThread.sendWriter(username.getText() + " " + password.getText());
    		myThread.setNewUser(false); 
    	}
    	else {
    		text.setText("please input valid Strings for input");
    	}
    	
    		
    	
    	
    	
    }
    @FXML
    void handleNewUserLogin(ActionEvent event) {
    	mediaPlayerNotification.play();
    	myThread.setDashReady(true);
    	myThread.setState("newUser");
    	//text.setText("New user created, press login to enter DashBoard");
    	
    	myThread.sendWriter(username.getText().trim() + " " + password.getText().trim());
    
    	
    }
    
    
    
    /*
	 * JavaFx scenes and files for music
	 */
    Stage window;
	
    Stage dashStage = new Stage();
	
    String error = "errorLogin.mp3";   
	Media errorSound = new Media(new File(error).toURI().toString());
	MediaPlayer mediaPlayerError = new MediaPlayer(errorSound);
	
	String dashLogin = "loginSuccess.mp3";   
	Media dashLoginSound = new Media(new File(dashLogin).toURI().toString());
	MediaPlayer mediaPlayerDashLogin = new MediaPlayer(dashLoginSound);
	
	String notification = "notification.mp3";   
	Media notificationSound = new Media(new File(notification).toURI().toString());
	MediaPlayer mediaPlayerNotification = new MediaPlayer(notificationSound);
	
    
    
    
    
   
	
	public void openDash() {
		
	
			
	new Dash(myThread, window);
	
	
		
	}

	
	
		
	

}
