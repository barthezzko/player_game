package com.barthezzko.playergame.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.apache.log4j.Logger;

public class ClientSocketAPI implements SocketAPI {

	private static final String LOCALHOST = "127.0.0.1";
	private static final int TIMEOUT_MS = 10_000;
	private static final String THREAD_NAME = "client-socket-listener";
	private Socket socket;
	private PrintStream outStream;
	private BufferedReader reader;
	private Logger logger = Logger.getLogger(ClientSocketAPI.class);

	public ClientSocketAPI(int socketPort, Function<String, Boolean> lineConsumer) {
		try {
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(LOCALHOST), socketPort);
			socket = new Socket();
			socket.connect(socketAddress, TIMEOUT_MS);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new PrintStream(socket.getOutputStream(), true);
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.submit(() -> {
				Thread.currentThread().setName(THREAD_NAME);
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						if (logger.isDebugEnabled()){
							logger.debug("CLIENT:IN:" + line);
						}
						if (lineConsumer.apply(line)){
							return;
						}
						
					}
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			});
			executorService.shutdown();
		} catch (IOException e) {
			logger.warn(e, e);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	@Override
	public void send(String line) {
		outStream.println(line);
		outStream.flush();
	}

}
