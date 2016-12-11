package net.fe.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.network.Message;

//TODO Rename this because it's going to get hella confusing.
public class ServerClient {

	
	
	private Socket socket;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private int id;
	
	
	public ServerClient(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TODO check documentation to see if I did this right (s/o to the lack of an internet connection)
	public ArrayList<Message> readMessages(){
		ArrayList<Message> messages = new ArrayList<Message>();
		try {
			while(input.available() != 0){
				Object object = input.readObject();
				if(object instanceof Message)
					messages.add((Message)input.readObject());
				else
					System.out.printf("Recieved an unexpected object from client : %s, %s%n", object.getClass(), object);
			}
		} catch (IOException e) {
			//Silently ignores the error because I'm trash
		} catch (ClassNotFoundException e) {
			//This shouldn't ever happen
		}
		messages.forEach(message -> message.setServer(id));
		return messages;
	}
	
	public void sendMessage(Message message){
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			System.out.printf("Failed to send %s message, %s%n", message.getClass(), e.getMessage());
		}
	}
	
	public void close(){
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}