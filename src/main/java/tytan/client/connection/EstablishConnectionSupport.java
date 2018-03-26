package tytan.client.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client.beans.Message;

public class EstablishConnectionSupport implements AbstractConnection {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket clientSocket;

	public EstablishConnectionSupport(String host, int port) throws IOException {
		connectToServer(host, port);
		getStreams();
	}

	private void connectToServer(String host, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
	}

	private void getStreams() throws IOException {

		out = new ObjectOutputStream(clientSocket.getOutputStream());
		in = new ObjectInputStream(clientSocket.getInputStream());
	}

	public void closeConnection() {
		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	@Override
	public Message readMessage() {
		try {
			return (Message) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			closeConnection();
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void writeMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			closeConnection();
			e.printStackTrace();
		}
	}
}
