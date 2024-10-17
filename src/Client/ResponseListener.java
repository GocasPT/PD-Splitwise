package Client;

import Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

class ResponseListener implements Runnable {
	private final Socket socket;

	public ResponseListener(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
			Response response;

			while ((response = (Response) in.readObject()) != null) {
				System.out.println("Response: " + response);

				if (!Client.isLogged)
					if (response.isSuccess()) {
						Client.isLogged = true;
						socket.setSoTimeout(0);
						System.out.println("Login successful!");
					} else { // Fail login == Exit
						Client.exit = true;
						//TODO: break input
						System.out.println("Login failed, exiting...");
						break;
					}

				//TODO: make response to exit (Server gonna shutdown)
				if (response.toString().equals("exit")) {
					Client.exit = true;
					System.out.println("Server have been close...");
					break;
				}

				//TODO: response handler (
				// when is a feedback unlock UI
				// when is notification trigger notification system
				// )
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
