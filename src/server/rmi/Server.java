package server.rmi;

import java.rmi.Naming;

//import ChineseChess.ChineseChessArithmeticRMIImpl;

public class Server {
	// Bind ArithmeticServer and Registry
	public static void main(String args[]) {
		try {
			RMIServerImpl name = new RMIServerImpl();
			System.out.println("Registering ...");
			Naming.rebind("ChessGame", name); // the name of the service
			System.out.println("Register success");
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
