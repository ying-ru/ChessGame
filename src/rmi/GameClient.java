package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import server.rmi.ServerInterface;

public class GameClient {
	public ServerInterface s;
	private int room;
	
	public GameClient() {
		try {
			s = (ServerInterface) Naming.lookup("rmi://127.0.0.1/ChessGame");
			room = -1;
		}catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getRoom() {
		return room;
	}
	
	public void setRoom(int room) {
		this.room = room;
	}
	
	public String getRivalToken(String userToken) {
		String token;
		token = "";
		try {
			token = s.getRivalToken(getRoom(), userToken);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public int getWin(String userToken) {
		int win = 0;
		try {
			win = s.getWin(userToken);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return win;
	}
	
	public int getLose(String userToken) {
		int lose = 0;
		try {
			lose = s.getLose(userToken);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return lose;
	}
}

