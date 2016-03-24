package chat_Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	//Constructor
	
	public Client(String host)
	{
		super("Ashirogi Client");
		serverIP  = host;
		userText =  new JTextField();
		userText.setEditable(false);
		userText.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	//connect to server
	public void startRunning()
	{
		try
		{
			connectToServer();
			setupStreams();
			whileChatting();
		}
		catch(EOFException e)
		{
			showMessage("\nClient terminated the connection");
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
		finally
		{
			closeCrap();
		}
	}
	
	private void connectToServer() throws IOException
	{
		showMessage("Attempting connection..\n");
		connection = new Socket(InetAddress.getByName(serverIP),6789);
		showMessage("Connected to " + connection.getInetAddress().getHostName());
	}
	
	//setup streams
	
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Your streams are good to go!!\n");
	}
	
	private void whileChatting() throws IOException
	{
		ableToType(true);
		do
		{
			try
			{
				message = (String) input.readObject();
				showMessage("\n "+ message);
			}
			catch(ClassNotFoundException c)
			{
				showMessage("\n Don't know that object type");
			}
		}
		while(!message.equals("SERVER - END"))
	}

}


