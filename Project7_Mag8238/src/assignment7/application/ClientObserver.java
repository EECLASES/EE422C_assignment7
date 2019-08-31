package application;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class ClientObserver extends PrintWriter implements Observer {
	
	private String currentState = new String();
	private boolean passwordSet;
	private boolean groupChatChange;
	private TimeWatch watch;
	private String clientNumber = new String();
	private String clientName = new String();
	private String password = new String();
	private String whichGroupchat = new String();
	private Thread ClientThread = new Thread();
	private ArrayList<String> notifications;
	public ArrayList<String> getNotifications() {
		return notifications;
	}
	public void setNotifications(ArrayList<String> notifications) {
		this.notifications = notifications;
	}
	private ArrayList<Chat> listOfChats;
	public ArrayList<Chat> getListOfChats() {
		return listOfChats;
	}
	public void setListOfChats(ArrayList<Chat> listOfChats) {
		this.listOfChats = listOfChats;
	}
	private ArrayList<String> friends;
	
	public ArrayList<String> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}
	public ClientObserver(OutputStream out, String clientNumber, String groupChatName ) {
		super(out);
		this.clientNumber = clientNumber;
		this.whichGroupchat = groupChatName;
		this.passwordSet = true;
		this.listOfChats = new ArrayList<Chat>();
		this.friends = new ArrayList<String>();
		this.currentState = "normal";
		this.clientName = clientNumber;
		this.watch = TimeWatch.start();
		this.notifications = new ArrayList<String>();
		
		
		
	}
	public long getTime() {
		return this.watch.time(TimeUnit.MINUTES);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}
	public String getName() {
		return this.clientName;
	}
	public String getGroupChat() {
		return this.whichGroupchat;
	}
	public void setGroupChat(Chat chat) {
		this.addGroupchat(chat);
		this.whichGroupchat = chat.getName();
	}
	public void setName(String name) {
		this.clientName = name;
	}

	public void addGroupchat(Chat chat) {
		listOfChats.add(chat);
	}
	public boolean checkGroupChat(Chat checkingChat) {
			if(listOfChats.contains(checkingChat)){
				return true;
			}
			return false;
		
	}
	public String getClientNumber() {
		return this.clientNumber;
	}
	public void addFriend(String newFriend) {
		friends.add(newFriend);
	}
	public void setState(String state ) {
		this.currentState =   state;
	}
	public String getPersonInfo() {
		return this.clientName + " " + this.currentState + " ";
	}
	public void setPass(String pass) {
		this.password = pass;
	}
	
}
