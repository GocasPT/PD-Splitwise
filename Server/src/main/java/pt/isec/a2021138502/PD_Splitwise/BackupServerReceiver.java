package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Server.getTimeTag;

public class BackupServerReceiver implements Runnable{
	private final ServerSocket serverSocket;
	private final DataBaseManager context;

	public BackupServerReceiver(ServerSocket serverSocket, DataBaseManager context) {
		this.serverSocket = serverSocket;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			System.out.println(getTimeTag() + "Backup server receiver started");

			//TODO: need to add something where? (flag to stop loop?)
			while (true) {
				Socket backupServerSocket = serverSocket.accept();
				new Thread(new BackupServerHandler(backupServerSocket, context)).start();
			}
		} catch ( NumberFormatException e ) {
			System.out.println("[HeartbeatThread] O porto de escuta deve ser um inteiro positivo");
		} catch ( SocketException e ) {
			System.out.println("[HeartbeatThread] Ocorreu um erro ao nível do backupServerSocket TCP:\n\t" + e);
		} catch ( IOException e ) {
			System.out.println("[HeartbeatThread] Ocorreu um erro no acesso ao backupServerSocket:\n\t" + e);
		} finally {
			try {
				serverSocket.close();
			} catch ( IOException e ) {
				System.out.println("[HeartbeatThread] Ocorreu um erro ao fechar o socket:\n\t" + e);
			}
		}
	}
}
