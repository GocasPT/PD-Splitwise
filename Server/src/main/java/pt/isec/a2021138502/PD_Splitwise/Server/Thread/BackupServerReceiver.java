package pt.isec.a2021138502.PD_Splitwise.Server.Thread;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Runnable.BackupServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class BackupServerReceiver extends Thread {
	private final ServerSocket serverSocket;
	private final DataBaseManager context;
	private final boolean isRunning;

	public BackupServerReceiver(boolean isRunning, ServerSocket serverSocket, DataBaseManager context) {
		super("BackupServerReceiver");
		this.isRunning = isRunning;
		this.serverSocket = serverSocket;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			System.out.println(getTimeTag() + "Backup server receiver started");

			//TODO: need to add something where? (flag to stop loop?)
			while (isRunning) {
				Socket backupServerSocket = serverSocket.accept();
				new Thread(
						new BackupServerHandler(backupServerSocket, context),
						backupServerSocket.getInetAddress().toString()
				).start();
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
