package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Logout;
import pt.isec.a2021138502.PD_Splitwise.ui.TUI.GetRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	static volatile boolean isLogged = false;
	static volatile boolean exit = false;
	final int TIMEOUT = 60;
	private final InetAddress serverAddr;
	private final int port;

	private Client(InetAddress serverAddr, int port) {
		this.serverAddr = serverAddr;
		this.port = port;
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Client <server> <port>");
			return;
		}

		try {
			InetAddress serverAddr = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);

			new Client(serverAddr, port).start();
		} catch (UnknownHostException e) {
			System.out.println("[MainThread] Destino desconhecido:\n\t" + e);
		}
	}

	private void start() {
		try (
				Socket serverSocket = new Socket(serverAddr, port);
				ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
				Scanner scanner = new Scanner(System.in)
		) {
			serverSocket.setSoTimeout(TIMEOUT * 1000);

			Thread responseThread = new Thread(new ResponseListener(serverSocket));
			responseThread.start();

			while (!exit) {
				Request request;

				try {
					String cmd = scanner.nextLine();
					String[] cmdArgs = cmd.split(" ");

					if (cmdArgs[0].equals("exit")) {
						exit = true;
						request = new Logout();
					} else {
						request = GetRequest.getRequest(cmdArgs);
					}

					if (request == null) continue;

					out.writeObject(request);
				} catch (NoSuchElementException e) {
					break;
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
			}

			responseThread.join();
		} catch (NumberFormatException e) {
			System.out.println("[MainThread] O porto do servidor deve ser um inteiro positivo");
		} catch (SocketTimeoutException e) {
			System.out.println("[MainThread] Nao foi enviado quais quer credenciais:\n\t" + e + "\nA conexao foi fechada");
		} catch (SocketException e) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} catch (InterruptedException e) {
			System.out.println("[MainThread] Ocorreu um erro ao esperar pelo termino do thread de resposta:\n\t" + e);
		}
	}
}
