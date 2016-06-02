import java.awt.*;
import java.applet.*;
import java.net.*;
import javax.swing.*;
import java.io.*;

public class MyClientWin extends Applet implements Runnable

{
	static boolean bConnected = false;
	static Socket mySocket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static String sMyId = null;
	static String sIP = null;

	static String fromServerStr = null;
	static String fromUserStr = null;
	static String savedMsg = null;

	static TextField textField;
	static TextArea textArea;

	static String sConnection = "Not Connected to the chat server!";

	Thread thread;

	//states of the client
	final static int INITIAL = 0;
	final static int REQUEST_SENT = 1;
	final static int WAIT_RESPONSE_RECEIVED = 2;
	final static int READY_RESPONSE_RECEIVED = 3;
	final static int MSG_SENT = 4;


	public void init()
	{
		textField = new TextField("", 50);
		textArea = new TextArea("No Messages",15, 50);
		Button button = new Button("Connect");
		Button closebutton = new Button("Close");
		Button msgbutton = new Button("Send Message");
		//Button chkmsgbutton = new Button("Check Messages");


		add(textField);
		add(button);
		add(closebutton);
		add(msgbutton);
		//add(chkmsgbutton);
		add(textArea);
	}

	public void paint(Graphics g)
	{
		Font font = new Font("Arial", Font.PLAIN, 12);
		Font fontb = new Font("Arial", Font.BOLD, 14);

		g.setFont(fontb);

		g.drawString(sConnection, 60, 330);

	}
	//***********************************************
	// trapping button actions
	//
	//***********************************************
	public boolean action(Event evt, Object arg) {
		String sTemp = null;

		//******************************************
		// close button pressed
		//******************************************
		if (arg == "Close") {
			try {
				if (bConnected)
					mySocket.close();
			} catch (IOException e) {}

			System.exit(0);
		}

		//********************************************
		// connect button pressed
		//******************************************
		if (arg == "Connect" && !bConnected) {

			try {
				int nPort = 4444; 
				mySocket = new Socket(sIP, nPort);				bConnected = true;

				// define input and output streams for reading and
				// writing to the socket				out = new PrintWriter(mySocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				out.println(sMyId);
				// set screen messages
				sConnection = "Connected to the chat server!";			
				//start thread				thread = new Thread(this);
				thread.start();	
			} catch (UnknownHostException e) {
				bConnected = false;
				sConnection = "Not Connected to the chat server!";				JOptionPane.showMessageDialog(null,"Don't know about host: " + sIP);
			} catch (IOException e) {				bConnected = false;				sConnection = "Not Connected to the chat server!";
				JOptionPane.showMessageDialog(null,"Server is not running!"); }		
		}// end of connect button

		//*****************************************************	
		// Send Message button pressed
		//*****************************************************	
		if (arg == "Send Message") {
			if (textField.getText() != null){
				fromUserStr = textField.getText();
				out.println("ACK"+ sMyId + " says: " + fromUserStr);
				textField.setText("");
			}
		}           
		repaint();

		return true;
	}


	//************************************************
	// main
	//
	// main application method for the class
	// it will initialize whole environment
	//
	//************************************************
	public static void main(String args[]){
		String sTemp = null;
		//
		// define window and call standard methods
		//
		MyClientWin app = new MyClientWin();
		Frame frame = new Frame ("Isaac Shannon - 6709038");
		app.init();
		app.start();

		frame.add("Center",app);
		frame.resize(400,400);
		frame.show();

	}// end of main

	//***********************************
	// stop
	//***********************************
	public void stop() {		thread.stop();	}// end of stop

	//***********************************
	// run - thread method	//***********************************
	public void run() {		boolean bLoopForever = true;


		//current state
		int currentState = INITIAL; 

		while (bLoopForever){
			//read from server			currentState = checkServer(currentState);
			try {				thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}// end of run
	//***********************************
	// checkServer - this is a main client algorithm
	//***********************************
	public static int checkServer(int currentState){

		String sTemp = null;
		boolean bLoop = true;
		String sFrameType = null;
		int newState = currentState;

		try {
			// read from the server socket
			if ((fromServerStr = in.readLine()) != null){

				//read message from server
				sTemp = textArea.getText();	
				textArea.setText(sTemp + "\n" + fromServerStr);
			}

		}catch (InterruptedIOException e) { }	
		catch (IOException e) { }

		return newState;	

	}// end of checkserver

	private void requestToSend(){

	}

}// end of class MyClientWin
