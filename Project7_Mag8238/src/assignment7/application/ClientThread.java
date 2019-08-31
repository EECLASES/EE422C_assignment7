package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ClientThread {
	private Thread readerThread;
	/*
	 * IO connections and information
	 */
	
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket sock;
	private String clientName = new String();
	private String state = new String();
	private String stateInfo = new String();
	private boolean openDashBool = false;
	private boolean newUser= false;
	private boolean dashReady= false;
	public String pendingMessage;
	private String chatNum = new String();
	private String serverMessage;
	private String time;
	
	private ArrayList<String> listOfNotifications = new ArrayList<String>();
	public ArrayList<String> getListOfNotifications() {
		return listOfNotifications;
	}

	public void setListOfNotifications(ArrayList<String> listOfNotifications) {
		this.listOfNotifications = listOfNotifications;
	}

	private ArrayList<String> messages = new ArrayList<String>();
	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	private ArrayList<String> listOfChats;
	private ArrayList<String> friends = new ArrayList<String>();
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<String> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}

	private ArrayList<String> messagesSent;
	
	
	public ClientThread() throws UnknownHostException, IOException {
		sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		readerThread = new Thread() {
			
			private String codes[] = new String[3];
			
			
			public void run() {
				String message;
				
				
				
				
				try {
					while ((message = reader.readLine()) != null) {
						synchronized(this) {
							codes = message.split(" ", 2);
							clientName = codes[0];
							stateInfo = codes[1];
							if(stateInfo.equals("quit")) {
								break;
							}
							else {
								parseLoginInfo(stateInfo);
							}
						}
						
						

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		readerThread.start();
	} 
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void parseLoginInfo(String data) {
		String serverCodes[] = new String[2];
		serverCodes = data.split(" ", 3);
		
		state = serverCodes[0];
		
		
	
		if(dashReady && state.equals("dash")) {
			openDashBool = true;
			sendWriter("");
			dashReady = false;
		}
		else if(dashReady && !state.equals("dash")) {
			sendWriter("");
			
			
		}
		else if(state.equals("Friends")) {
			int number = Integer.parseInt(serverCodes[1]);
			String[] friendsString = new String[number];
			friendsString = serverCodes[2].split(" ", number);
			for(String friend: friendsString) {
				friends.add(friend);
			}
		}
		else if(state.equals("TimeActive")) {
			time = serverCodes[2];
			
		}
		else if (state.equals("Chats")) {
			chatNum = serverCodes[2];
		}
//		else if(state.equals("getNotifications")) {
//			
//		}
		
	}
	 public boolean isOpenDashBool() {
		return openDashBool;
	}

	public void setOpenDashBool(boolean openDashBool) {
		this.openDashBool = openDashBool;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public boolean isDashReady() {
		return dashReady;
	}

	public void setDashReady(boolean dashReady) {
		this.dashReady = dashReady;
	}

	public void sendWriter(String str) {
		synchronized(this) {
			writer.println(clientName + " " + state + " " + str);
			writer.flush();
		}
	}
	public void setState(String myState) {
			state = myState;
	}

	public void getDashInfo() {
		this.state = "getTimeActive";
		sendWriter("");
		this.state = "getFriends";
		sendWriter("");
		this.state = "getMessages";
		sendWriter("");
		this.state = "getNotifications";
		sendWriter("");
		this.state = "getTotalChats";
		sendWriter("");
//		this.state = "getTotalFriends";
		
		
		
	}
	
	
	}


