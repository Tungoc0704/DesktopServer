package Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import Network.ActionSignal;
import Network.FollowHandler;
import Network.HandleMessage;
import Network.PostHandler;
import Network.SuggestFollowHandler;
import Network.UserServiceImpl;
import Network.WebSocketServerHandler;

import com.sun.net.httpserver.HttpServer;

import Common.UserService;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Main {
	static DatagramSocket serverSocket;

	public static void main(String[] args) {

		// Chạy WebSocket server trong một thread riêng
		WebSocketServerHandler webSocketServer = new WebSocketServerHandler(new InetSocketAddress(8080));
		new Thread(() -> {
			webSocketServer.start();
			System.out.println("WebSocket server started on ws://localhost:8080");
		}).start();

		// Khởi động RMI Server
		try {
			System.out.println("Attempting to start RMI registry on port 1099...");
			// Khởi tạo Registry trên cổng 1099
			LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry started on port 1099");

			// Đăng ký UserServiceImpl
			UserService userService = new UserServiceImpl(); // Triển khai dịch vụ
			Naming.rebind("rmi://localhost/UserService", userService); // Đăng ký với tên "UserService"
			System.out.println("RMI service 'UserService' is ready.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to start RMI service.");
		}

		// HTTP Server
		try {
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0); // Server HTTP chạy port 8081

			httpServer.createContext("/api/posts", new PostHandler()); // Đặt handler cho endpoint /api/posts
			httpServer.createContext("/api/suggest-follows", new SuggestFollowHandler());
			httpServer.createContext("/api/follow", new FollowHandler());

			httpServer.setExecutor(null); // Sử dụng executor mặc định
			httpServer.start();
			System.out.println("HTTP server started on http://localhost:8081");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Khởi động UDP Server
		try {
			serverSocket = new DatagramSocket(7704);
			System.out.println("đã mở port 7704");
			new ActionSignal().listenFromClient(serverSocket);

		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

}
