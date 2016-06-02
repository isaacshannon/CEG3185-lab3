import java.io.IOException;

public class Simulation {
	
	public static void main(String[] args){
		MyServer server = new MyServer();
		try {
			server.startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
