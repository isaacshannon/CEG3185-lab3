import java.awt.*;
import java.applet.*;
import java.net.*;
import javax.swing.*;
import java.io.*;

public class MyServer {
	ServerSocket serverSocket = null;
	Socket[] client_sockets = new Socket[10];
	String inputLine = null;
	String outputLine = null;
	PrintWriter[] s_out = new PrintWriter[10];
	BufferedReader[] s_in = new BufferedReader[10];

	int nPort = 4444; // default 

	boolean bListening = true;

	String[] sMessages = new String[10];
	int nMsg = 0;
	boolean bAnyMsg = false;
	boolean bAlive = false;

	// initialize some var's for array handling
	int s_count = 0;

	public MyServer() {
	}

	public void startServer() throws IOException{
		// create server socket
		try {
			serverSocket = new ServerSocket(nPort);

		} catch (IOException e) {
			System.err.println("Could not listen on port");
			System.exit(-1);
		}

		//
		// this variable defines how many clients are connected
		//
		int nClient = 0;

		//
		// set timeout on the socket so the program does not
		// hang up
		//
		serverSocket.setSoTimeout(1000);

		//
		// main server loop
		//
		System.out.println("Server - Starting up...");
		System.out.println("Server - Looking for new clients");
		while (bListening){

			bAlive = false;

			try {
				//System.out.println("looking for new clients");
				//
				// trying to listen to the socket to accept
				// clients
				// if there is nobody to connect, exception will be
				// thrown - set by setSoTimeout()
				//
				client_sockets[s_count]=serverSocket.accept();

				//
				// connection got accepted
				// 
				if (client_sockets[s_count]!=null){
					System.out.println("Server - Connection from " +
							client_sockets[s_count].getInetAddress() + " accepted.");
					client_sockets[s_count].setSoTimeout(1000);
					System.out.println("Server - accepted client");
					s_out[s_count] = new PrintWriter(client_sockets[s_count].getOutputStream(),true);
					s_in[s_count] = new BufferedReader(new InputStreamReader(client_sockets[s_count].getInputStream()));

					//
					// set server message about new client connection
					//

					bAnyMsg = true;
					sMessages[nMsg]=" " + s_in[s_count].readLine() + " joined";
					nMsg ++;

					// 
					// increment count of clients
					//
					s_count++;
				}
			}
			catch (InterruptedIOException e) {}

			if (s_count >0){
				//read from sockets
				for (int i=0;i<s_count;i++){
					//s_out[i].println("POL");
					inputLine = null;

					try {
						// read respose from the client
						inputLine = s_in[i].readLine();

					} catch (InterruptedIOException e) {
						inputLine = null;}
					catch (java.net.SocketException e) {}

					if(inputLine != null){
						System.out.println("Server - Got message: " + inputLine);
					}
				}
			} 

			//warning, no clients
			if (s_count < 1){
				System.out.println("Server - No clients are connected");
			}

		}// end of while loop

		// close all sockets
		for (int i=0;i<s_count;i++){
			client_sockets[i].close();
		}

		serverSocket.close();

	}// end main 
}// end of class MyServer
