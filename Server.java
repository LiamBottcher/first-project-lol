import java.net.*;
import java.io.*;
import java.util.*;

public class Server{

	public static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
	static int id = 0;
	static ServerSocket ss;
	public static void main(String[] args){


		try{
		ss = new ServerSocket(1337);

		while (true){

				Socket socket = ss.accept();
				ClientHandler client = new ClientHandler(socket, id++);
				clients.add(client);
				new Thread(client).start();
				//new Thread(new Thing(socket, id++)).start(); old way doesn't work because I have to add it to synvhronizedList
				}
		}catch(IOException e){
			//this would be the scenario when Server closes I think
			//no need for break because exception makes program jump
			//to catch statement outside of the while loop
			}
				
		
		finally{
		for (ClientHandler object : clients){
			object.writeToSelf("Server has closed, thank you for connecting", -1);
			object.closeEverything();}
		}

	}

}



class ClientHandler implements Runnable{

	Socket socket;
	int id;
	BufferedReader reader;
	BufferedWriter writer;

	ClientHandler(Socket socket, int id){

		this.socket = socket;
		this.id = id;
		try{

			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		}catch (IOException e){}

	}


	public void writeToSelf(String msg, int incomingId){

		try{

			writer.write("User #" + incomingId + ": " + msg + "\n");
			writer.flush();

		} catch (IOException e) {}

	}



	@Override
	public void run(){

		try{

			while (true){

				String message = reader.readLine();

				if (message == null){

					closeEverything();
					break;
				}
				else{

					for (ClientHandler client : Server.clients){

						client.writeToSelf(message, id);
					}
				}
			}
		} catch(IOException e){}
	}






	public void closeEverything(){
		try{
			if (socket != null) socket.close();
			if (reader != null) reader.close();
			if (writer != null) writer.close();
		}catch(IOException e){}
	}
}
