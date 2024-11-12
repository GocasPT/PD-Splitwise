package pt.isec.pd.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.isec.pd.server.PLACE_HOLDER.Server;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

}
