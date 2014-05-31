package game;

import java.rmi.RemoteException;

import rmi.GameClient;
import control.Controller;
import data.MainData;
import ui.playRoom.PlayRoom;

public class PlayGame {

//	private String[][] chesses;
	PlayRoom playRoom;
	GameClient server;
	int chessBoardWidth, chessBoardHeight;
	MainData data;
	Controller controler;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playRoom = new PlayRoom(server, userToken);
		chessBoardWidth = playRoom.getChessBoard().getChessBoardWidth();
		chessBoardHeight = playRoom.getChessBoard().getChessBoardHeight();
		data = new MainData(chessBoardWidth, chessBoardHeight, server, userToken);
		controler = new Controller(chessBoardWidth, chessBoardHeight);
		
		// set observer observable
		data.getChessPieceList().addObserver(playRoom.getChessBoard());
		data.getChessPieceList().addObserver(controler.getChessPieceCoordinate());
		playRoom.getChessBoard().getChessGameObservable().addObserver(controler.getTransferFrameXY());
		playRoom.getChessBoard().getChessGameObservable().addObserver(data.getChessPieceList());
		// set observer observable end
		data.getChessPieceList().initChessPiece();
	}
	
//	public void test() {
//		chesses = new String[8][4];
		
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 4; j++) {
//				chesses[i][j] = "cover";
//			}
//		}
//		chesses[0][0] = "NULL";
//		data.getChessPieceList().setChessPiece(0, chesses);
//		chesses[1][3] = "redHorse";
//		data.getChessPieceList().setChessPiece(0, chesses);
//		chesses[2][3] = "redHorse";
//		data.getChessPieceList().setChessPiece(0, chesses);
//		System.out.println("x: " + playRoom.getChessBoard().getBefortX() + " to " + playRoom.getChessBoard().getAfterX());
//		chesses = controler.getTransferFrameXY().getChessName();
//		data.getChessPieceList().setChessPiece(0, chesses);
//	}
	
	public static void main(String[] args) {
		String userToken, secretToken;
		userToken = args[0];
		secretToken = args[1];
		PlayGame pg = new PlayGame(userToken, secretToken);
//		pg.test();
	}
}
