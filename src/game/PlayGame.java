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
	String userToken;
	private Thread updateScore;
	private Thread updateChessBoard;
	
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
		updateScore();
		updateChessBoard();
	}
	
	private void updateScore() {
		updateScore = new Thread(new Runnable() {
//		boolean turnAnother = true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						Thread.sleep(1000 * 1);
						// update score start
						if (!server.s.isWin(server.getRoom(), userToken)) {
							int scoreA = playRoom.getScore(userToken);
							int scoreB = playRoom.getScore(server.getRivalToken(userToken));
							playRoom.getPlayerInfoJPanel().setPlayerAScore(scoreA + "");
							playRoom.getPlayerInfoJPanel().setPlayerBScore(scoreB + "");
							if (scoreB - scoreA > 7) {
								playRoom.appendChatArea("<系統> ： " + userToken + "OH！該加油了！");
							}
							if (scoreA > 11) {
								playRoom.appendChatArea("<系統> ： " + userToken + "加油，快贏對方了！");
							}
						}
						// update score end
						if (server.s.isTurnUser(server.getRoom(), userToken)) {
							playRoom.changePlay("輪到你了");
						} else {
							playRoom.changePlay("等待對方");
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					updateScore.suspend();
					updateChessBoard.suspend();
					playRoom.appendChatArea("<系統> ： 連線中斷，60秒後進行重新連線。");
					playRoom.appendChatArea("<系統> ： 若仍然無法練縣，則強制斷線，並判為輸局。");
					int room = -1;
					while (room == -1) {
						try {
							Thread.sleep(1000 * 60);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							server.s.connect(userToken);
							playRoom.appendChatArea("<系統> ： 重新連線成功。");
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							playRoom.appendChatArea("<系統> ： 重新連線失敗。");
							e1.printStackTrace();
						}
						room = server.getRoom();
						System.out.println("room" + room);
					}
					e.printStackTrace();
				}
			}
		});
		updateScore.start();
	}
	
	private void updateChessBoard() {
		updateChessBoard = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					boolean update;
					update = true;
					while (true) {
						Thread.sleep(1000 * 1);
						if (server.s.isTurnUser(server.getRoom(), userToken) && update) {
							// update chess board start
							System.out.println("update...");
							String[][] chess = server.s.updateChessBoardInfo(server.getRoom(), userToken);
							data.getChessPieceList().setChessPiece(chess);
							update = false;
							for (int i = 0; i < 4; i++) {
								for (int j = 0; j < 8; j++) {
									System.out.print(chess[i][j] + " ");
								}
								System.out.println();
							}
							// update chess board end
						} else if (!server.s.isTurnUser(server.getRoom(), userToken)) {
							update = true;
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					updateScore.suspend();
					updateChessBoard.suspend();
					playRoom.appendChatArea("<系統> ： 連線中斷，60秒後進行重新連線。");
					playRoom.appendChatArea("<系統> ： 若仍然無法練縣，則強制斷線，並判為輸局。");
					int room = server.getRoom();
					while (room == -1) {
						try {
							Thread.sleep(1000 * 10);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							server.s.connect(userToken);
							playRoom.appendChatArea("<系統> ： 重新連線成功。");
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							playRoom.appendChatArea("<系統> ： 重新連線失敗。");
							e1.printStackTrace();
						}
						room = server.getRoom();
						System.out.println("room" + room);
					}
					e.printStackTrace();
				}
			}
		});
		updateChessBoard.start();
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
