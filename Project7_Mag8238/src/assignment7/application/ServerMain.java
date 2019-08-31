package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class ServerMain  {	
	public static Map<String, Chat> chats = new HashMap<String, Chat>();
    public static Map<String, ClientObserver> people = new HashMap<String, ClientObserver>();
	
    
    
 
	private static int clientNo= 0;
	private String codes[] = new String[3];

	
	public void parse(ClientObserver person, String message) {
		String[] codes = new String[2];
		
		String action = codes[0];
		String input = codes[1];
		
		codes = message.split(" ", 2);
		action = codes[0];
		input = codes[1];
		
		
		Chat groupChat = null;
		if(action.equals("getTimeActive")) {
			person.setState("TimeActive");
			
			long a = person.getTime();
			person.println(person.getPersonInfo() + Long.toString(person.getTime()));
		}
		if(action.equals("getFriends")) {
			person.setState("Friends");
			
			String friendsString = new String();
			for(int i = 0; i < person.getFriends().size(); i++) {
				friendsString = person.getFriends().get(i) + " ";
			}
			person.println(person.getPersonInfo() + Integer.toString(person.getFriends().size()) + " " + friendsString);
		}
		else if (action.equals("getTotalChats")) {
			person.setState("Chats");
			person.println(person.getPersonInfo() + person.getListOfChats().size());
		}
		else if (action.equals("getNotifications")) {
			person.setState("Notifications");
			
			person.println(person.getPersonInfo() + person.getNotifications().size());
		}
//		else if(action.equals("getNotifications")) {
//			
//		}
//		else if (action.equals("getTotalChats")) {
//			
//		}
		else if(action.equals("newGroupChat")) {
			if(chats.get(input) == null) {
				groupChat = new Chat(input);
				groupChat.addObserver(person);
				person.setGroupChat(groupChat);
				chats.put(groupChat.getName(), groupChat);
				person.println("you have now created "+ groupChat.getName() + " and have entered this group chat ");
				person.flush();
			}
			else if (chats.get(input) != null) {
				person.println("Sorry, this group chat name already exists. Try again with different person");
				person.flush();
			}
			
			return;
		}
		else if(action.equals("joinExistingChat")) {
			if(input.equals("nothing")) {
				person.println(person.getPersonInfo() + "Which group chat would you like to join? ");
				person.flush();
			}
			else if(chats.get(input) != null) {
				person.setGroupChat(chats.get(input));
				groupChat = chats.get(input);
				if(groupChat.contains(person)) {
					person.setState("normal");
					person.println(person.getPersonInfo() + "It looks like you are already in group chat" + groupChat.getName() );
				}
				else if (!groupChat.contains(person)) {
					person.setState("normal");
					person.setGroupChat(groupChat);
					groupChat.addObserver(person);
					
					person.println(person.getPersonInfo() + "you have joined groupChat " + groupChat.getName());
				}
				
				
			}
		}
		else if(action.equals("changeChat")) {
			if(chats.get(input) != null ) {
				Chat a = chats.get(input);
				if(person.checkGroupChat(a)) {
					person.println("you have switch to groupChat " + a.getName());
				}
				else {
					person.println("That group Chat does not exist, join the group chat or make a new one");
					
				}
				person.flush();
			}
			
		}
		else if(action.equals("addFriend")) {
			if(input.equals("nothing")) {
				person.println(person.getPersonInfo() + " what Friend would you like to add? " );
				person.flush();
			}
			else if(people.get(input) != null){
				person.addFriend(input);
				person.setState("dash");
				person.println(person.getPersonInfo() + " you have added " + input + " as your friend");
				person.flush();
			}
			else if(people.get(input) == null) {
				person.setState("dash");
				person.println(person.getPersonInfo() + " " + input + " person does not exist, please try again with username registered with us.");
				person.flush();
			}
			return;
		}else if (action.equals("changeName")) {
			if(input.equals("nothing")) {
				person.println(person.getPersonInfo() + " what would like your name to be? " );
				person.flush();
			}
			else if(!input.equals("nothing")) {
				String[] usrnameAndPass = input.split(" ", 2);
				String username = usrnameAndPass[0];
				String pass = usrnameAndPass[1];
				synchronized(people) {
					people.remove(person.getName());
					person.setName(username);
					if(pass != null) person.setPass(pass);
					people.put(person.getName(), person);
					System.out.println("User: "+ person.getClientNumber() + "changed name to: " + person.getName() +" and has set their password");
					
					person.setState("dash");
					person.println(person.getPersonInfo() + "New username = " + person.getName() );
					person.flush();
					chats.get(person.getGroupChat()).send(person.getName() + " username created and is in the broadcast chat");
				}
			}
			return;
		}else if (action.equals("newUser")) {
			String[] usrnameAndPass = input.split(" ", 2);
			String username = usrnameAndPass[0];
			String pass = usrnameAndPass[1];
			synchronized(people) {
				if(people.containsKey(username)) {
					
					person.println(person.getPersonInfo() + "This person already exists. Try Again");
					person.flush();
				}else if(!people.containsKey(username)) {
					people.remove(person.getName());
					person.setName(username);
					person.setPass(pass);
					person.setState("dash");
					
					people.put(username, person);
					person.println(person.getPersonInfo() + "New username = " + person.getName() );
					person.flush();
					
				}
			}
		}
		
		else if(action.equals("groupMessage")){
			groupChat = chats.get(person.getGroupChat());
			groupChat.send(person.getName() + ": " + input);
			return;
		}
		else if (action.equals("quit"))
		{
			people.remove(person.getName());
			person.close();
			
		}
		
		
		
	}
	
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		Chat worldChat = new Chat("worldChat");
		chats.put(worldChat.getName(), worldChat);
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(4242); 
		System.out.println("MultiThreadServer started at " 
				+ new Date() + '\n'); 
		
		while (true) {
			// Listen for a new connection request 
			Socket socket = serverSocket.accept(); 

			// Increment clientNo 
			clientNo++; 
			
			
			String clientNumber = Integer.toString(clientNo);
			String clientName = new String();

			Thread newUser = makeThread(socket);
			newUser.start();
			
			
			ClientObserver newObserver = null;
			try {
				
				newObserver = new ClientObserver(socket.getOutputStream(), clientNumber, worldChat.getName());
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			newObserver.setState("changeName");
			newObserver.println(newObserver.getPersonInfo() );
			newObserver.flush();
			people.put(newObserver.getName(), newObserver);
			
			// Find the client's host name, and IP address 
			InetAddress inetAddress = socket.getInetAddress();
			System.out.println("Client " + clientNo + "'s host name is "
					+ inetAddress.getHostName() );
			System.out.println("Client " + clientNo + "'s IP Address is " 
					+ inetAddress.getHostAddress() );
			
			//Server name for each client
			System.out.println( clientNumber  + "User has visited this Server." );
			System.out.println();
			
			
			
			
			
		}

	}
	
	public Thread makeThread(Socket socket) {
		Thread newUser = null;
		try {
			newUser = new Thread() {
				private BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				

				public void run() {
					String message;
					
				
						
						try {
							while ((message = reader.readLine()) != null) {
								
								
								codes = message.split(" ", 2);
								
								String personName = codes[0];
								ClientObserver person = people.get(codes[0]);
								
								parse(person, codes[1]);
								
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
};
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	return newUser;
		
	}
	


}