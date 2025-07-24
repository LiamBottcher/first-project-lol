import java.util.*;
import java.net.*;
import java.io.*;

public class Client{

	public static void main(String[] args){
		try{
			Socket socket = new Socket("127.0.0.1", 1337);
			Connection connection = new Connection(socket);
			new Thread(connection).start();
			Scanner scanner = new Scanner(System.in);
			while (true){
				String message = scanner.nextLine();
				connection.write(message);}
		}catch(IOException e){}
	}


}

class Connection implements Runnable{
	Socket socket;
	BufferedReader reader;
	BufferedWriter writer;
	@Override
	public void run(){
		while (true){
			try{
				String message = reader.readLine();
				if (message == null){
					break;}
				System.out.println(message);
			}catch(IOException e){}
		
		}
	}
	

	public Connection(Socket socket){
		try{
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}catch(IOException e){}
	}
	public void write(String message){
		try{
			writer.write(message + "\n");
			writer.flush();
		}catch(IOException e){}
	}
}
