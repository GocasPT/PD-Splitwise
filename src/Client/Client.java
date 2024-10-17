package Client;

import Client.ui.TUI.GetRequest;
import Message.Request.Request;
import Message.Request.User.Logout;
import Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	public static final int TIMEOUT = 60;
	private static final boolean isLogged = false;
	private static boolean exit = false;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Client.Client <server> <port>");
			return;
		}

		try {
			InetAddress serverAddr = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);

			try (
					Socket serverSocket = new Socket(serverAddr, port);
					ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
					Scanner scanner = new Scanner(System.in)
			) {
				serverSocket.setSoTimeout(TIMEOUT * 1000); //TODO: I need this?

				Thread responseThread = new Thread(new ResponseHandler(serverSocket, isLogged));
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
			}
		} catch (UnknownHostException e) {
			System.out.println("[MainThread] Destino desconhecido:\n\t" + e);
		} catch (NumberFormatException e) {
			System.out.println("[MainThread] O porto do servidor deve ser um inteiro positivo");
		} catch (SocketTimeoutException e) {
			System.out.println("[MainThread] Nao foi recebida qualquer resposta:\n\t" + e);
		} catch (SocketException e) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} catch (InterruptedException e) {
			System.out.println("[MainThread] Ocorreu um erro ao esperar pelo termino do thread de resposta:\n\t" + e);
		}
	}

	static class ResponseHandler implements Runnable {
		private final Socket socket;
		private boolean isLogged = false;

		public ResponseHandler(Socket socket, boolean isLogged) {
			this.socket = socket;
			this.isLogged = isLogged;
		}

		@Override
		public void run() {
			try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
				Response response;

				while ((response = (Response) in.readObject()) != null) {
					System.out.println("Response: " + response);

					if (!isLogged)
						if (response.isSuccess())
							isLogged = true;
						else continue;

					if (response.toString().equals("exit")) {
						exit = false;
						System.out.println("Server have been close...");
						break;
					}
				}
			} catch (SocketException e) {
				System.out.println("[ResponseThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
			} catch (IOException e) {
				System.out.println("[ResponseThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
			} catch (ClassNotFoundException e) {
				System.out.println("[ResponseThread] Ocorreu um erro ao ler o objeto recebido:\n\t" + e);
			}
		}
	}
}
