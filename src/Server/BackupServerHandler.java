package Server;

import Data.DatabaseManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static Server.Server.getTimeTag;

public class BackupServerHandler implements Runnable {
	private final Socket backupServerSocket;
	private final DatabaseManager context;
	private final String name;

	public BackupServerHandler(Socket backupServerSocket, DatabaseManager context) {
		this.backupServerSocket = backupServerSocket;
		this.context = context;
		name = backupServerSocket.getInetAddress().getHostAddress() + ":" +
				backupServerSocket.getPort() + " - " +
				backupServerSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		try (ObjectOutputStream out = new ObjectOutputStream(backupServerSocket.getOutputStream())) {

			//while (true) {
			//TODO: block until database update
			out.writeObject("Hello from the server");
			//}

		} catch (SocketException e) {
			System.out.println("[ClientThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[ClientThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} finally {
			System.out.println(getTimeTag() + "Backup Server '" + name + "' disconnected");
		}
	}
}
