package game;

import java.rmi.RemoteException;
import rmi.GameClient;
import data.MainData;
import ui.playRoom.PlayRoom;

public class PlayGame {
	PlayRoom playRoom;
	GameClient server;
	int chessBoardWidth, chessBoardHeight;
	MainData data;
	String userToken;
	
	public PlayGame(String userToken, String secretToken) {
		server = new GameClient();
		try {
			int room;
			server.s.connect(userToken);
			room = server.s.getRoomNum(userToken);
			while (room == -1) {
				Thread.sleep(1000 * 5);
				server.s.connect(userToken);
				room = server.s.getRoomNum(userToken);
				System.out.println("room" + room);
			}
			server.setRoom(room);
			
			System.out.println("room" + room);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		playRoom = new PlayRoom(server, userToken);
		chessBoardWidth = playRoom.getChessBoard().getChessBoardWidth();
		chessBoardHeight = playRoom.getChessBoard().getChessBoardHeight();
		data = new MainData(chessBoardWidth, chessBoardHeight, server, userToken);
		
		// set observer observable
		data.getChessPieceList().addObserver(playRoom.getChessBoard());
		data.getChessPieceList().addObserver(playRoom.getChatPanel());
		playRoom.getChessBoard().getChessGameObservable().addObserver(data.getChessPieceList());
		// set observer observable end
		data.getChessPieceList().initChessPiece();
	}
	
	public static void main(String[] args) {
		String userToken, secretToken;
		userToken = args[0];
		secretToken = args[1];
		PlayGame game = new PlayGame(userToken, secretToken);
	}
}
