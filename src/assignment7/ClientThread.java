package assignment7;

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
	private static Thread readerThread;
	/*
	 * IO connections and information
	 */
	private boolean readytoSend = false;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket sock;
	private String clientName = new String();
	private String state = new String();
	private String stateInfo = new String();
	private boolean openDashBool = false;
	private boolean newUser= false;
	private boolean dashReady= false;
	public String pendingMessage = new String();
	private String chatNum = new String();
	private String time = new String();
	private int timeSeconds = 0;
	private String serverMessage = new String();
	
	public String getServerMessage() {
		return serverMessage;
	}

	public int getTimeSeconds() {
		return timeSeconds;
	}

	public void setTimeSeconds(int timeSeconds) {
		this.timeSeconds = timeSeconds;
	}

	public String[] allChats;
	
	private ArrayList<String> listOfNotifications = new ArrayList<String>();
	private ArrayList<String> listOfChats = new ArrayList<String>();
	
	

	
	private HashMap<String, Object> controllers;
	 public void controllers(HashMap<String, Object> controllers2) {
		 this.controllers = controllers2;
	 }
	
	
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
						
							System.out.println("processing " + message + " ");
							codes = message.split(" ", 2);
							clientName = codes[0];
							stateInfo = codes[1];
							if(stateInfo.equals("quit")) {
								System.exit(1);
								break;
								
							}
							else {
								parseLoginInfo(stateInfo);
							}
						
						
						

					}
					System.out.println("The thread has ended");
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

	public void parseLoginInfo(String data) throws IOException {
		String serverCodes[] = new String[2];
		serverCodes = data.split(" ", 3);
		
		state = serverCodes[0];
		
		
	
		if(dashReady && state.equals("dash")) {
			openDashBool = true;
			dashReady = false;
			sendWriter("");
			
		}
//		else if(dashReady && !state.equals("dash")) {
//			sendWriter("");
//			
//			
//		}
		else if (state.equals("login")) {
			serverMessage = serverCodes[1];
		}
		else if(state.equals("Friends")) {
			int number = Integer.parseInt(serverCodes[1]);
			if(number>0) {
				String[] friendsString = new String[number];
				friendsString = serverCodes[2].split("++", number);
				friends.clear();
				for(String friend: friendsString) {
					friends.add(friend);
				}
			}
		}
		else if(state.equals("TimeActive")) {
			this.setTimeSeconds(Integer.parseInt(serverCodes[1]));
			this.setTime(serverCodes[1]);
			
		}
		else if (state.equals("Chats")) {
			
			chatNum = serverCodes[1];
			if(Integer.parseInt(chatNum)>0) {
				allChats = new String[Integer.parseInt(chatNum)];
				allChats = serverCodes[2].split(" ", Integer.parseInt(chatNum));
				listOfChats.clear();
				for (String chat: allChats) {
					this.listOfChats.add(chat);
				}
//				DashChatsController DC = (DashChatsController) controllers.get("chat");
//		    	DC.dashChats();
			}
			
		}
		else if(state.equals("Notifications")) {
			String notificationNum = serverCodes[1];
			if(Integer.parseInt(notificationNum) > 0) {
				String[] allNotifications = new String[Integer.parseInt(notificationNum)];
				allNotifications = serverCodes[2].split(" \\ ", Integer.parseInt(notificationNum));
				listOfNotifications.clear();
				for (String notification: allNotifications) {
					this.listOfNotifications.add(notification);
				}
			}
			
		}
		else if (state.equals("FriendAdded")) {
			DashController a = (DashController) controllers.get("dash");
			a.dash();
		}
		else if(listOfChats.contains(state)) {
			String notification =  state + "-> " +serverCodes[2];
//			this.messages.add(notification);
//			this.listOfNotifications.add(notification);
//			DashChatsController DC = (DashChatsController) controllers.get("chat");
//	    	DC.dashChats();		
		}
		readytoSend = true;
		System.out.println("processed");
		
	}
	 public boolean isReadytoSend() {
		return readytoSend;
	}

	public void setReadytoSend(boolean readytoSend) {
		this.readytoSend = readytoSend;
	}

	public String getState() {
		return state;
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
		
			writer.println(clientName + " " + state + " " + str);
			writer.flush();
		
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
		this.state = "getChats";
		sendWriter("");

		
		
	}
	public void getChatInfo() {
		this.state = "getChats";
		sendWriter("");
		this.state = "getMessages";
		sendWriter("");
		this.state = "getNotifications";
		sendWriter("");
		
	
	}
	
	public void getFriendsInfo() {
		this.state = "getFriends";
		sendWriter("");
		
	}
	public void getNotificationsInfo() {
		this.state = "getNotifications";
		sendWriter("");
		
	}
	public void getSettingsInfo() {
		
	}
	public void getBroadcastInfo() {
		
	}
	
	
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

	
	public ArrayList<String> getListOfChats() {
		return listOfChats;
	}

	public void setListOfChats(ArrayList<String> listOfChats) {
		this.listOfChats = listOfChats;
	}

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
	
	}


