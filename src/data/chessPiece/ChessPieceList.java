package data.chessPiece;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import rmi.GameClient;

/* 
 * add chess in ArrayList
 * initChessPiece
 * update chess board
 */
public class ChessPieceList extends Observable implements Observer {
	private ChessPiece temp;
	private ChessPieceLocation chessBoardLoc;
	private ArrayList<ChessPiece> chessPieceList;
	private GameClient server;
	private String userToken;
	public Thread updateChessBoard;

	public ChessPieceList(ChessPieceLocation chessBoardLoc, GameClient server, String userToken) {
		this.userToken = userToken;
		this.server = server;
		this.chessBoardLoc = chessBoardLoc;
		chessPieceList = new ArrayList<ChessPiece>();
		initChessPiece();
		updateChessBoard();
		// TODO Auto-generated constructor stub
	}

	public void initChessPiece() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				temp = new ChessPiece(chessBoardLoc.getChessLocation(i, j), i, j, chessBoardLoc.getGridLength(), "cover", chessBoardLoc);
				chessPieceList.add(temp);
			}
		}
		
		setChanged();
		notifyObservers(chessPieceList);
	}
	
	public void setChessPiece(String[][] chessBoard) {
		chessPieceList.removeAll(chessPieceList);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				if (!chessBoard[j][i].equals("NULL")) {
					temp = new ChessPiece(chessBoardLoc.getChessLocation(i, j), i, j, chessBoardLoc.getGridLength(), chessBoard[j][i], chessBoardLoc);
					chessPieceList.add(temp);
				}
			}
		}
		setChanged();
		notifyObservers(chessPieceList);
	}

	public ArrayList<ChessPiece> getChessList() {
		return chessPieceList;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
		// TODO Auto-generated method stub
		if (arg instanceof ChessPiece) {
			int locX = (((ChessPiece) arg).getFrameX() + ((ChessPiece) arg).getGrid() / 2);
			int locY = (((ChessPiece) arg).getFrameY() + ((ChessPiece) arg).getGrid() / 2);
			int beforeX = ((ChessPiece) arg).getBeforeX();
			int beforeY = ((ChessPiece) arg).getBeforeY();
			int afterX = ((ChessPiece) arg).getAfterX();
			int afterY = ((ChessPiece) arg).getAfterY();
			
			if (locX < 0 || locY < 0 || locX > chessBoardLoc.getBoardWidth() || locY > chessBoardLoc.getBoardHeight()) {
				((ChessPiece) arg).goBack();
			} else {
				try {
					if (server.s.moveChess(server.getRoom(), userToken, beforeX, beforeY, afterX, afterY)) {
						setChessPiece(server.s.updateChessBoardInfo(server.getRoom(), userToken));
						System.out.println("true");
					} else {
						System.out.println("false");
						((ChessPiece) arg).goBack();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addMsg(String msg) {
		setChanged();
		notifyObservers(msg);
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
							setChessPiece(chess);
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
//					updateScore.suspend();
//					updateChessBoard.suspend();
					addMsg("<系統> ： 連線中斷，請重新開啟遊戲。");
					addMsg("<系統> ： 若 60 秒內仍然無法連線，則強制斷線，並判為輸局。");
//					int room = server.getRoom();
//					while (room == -1) {
//						try {
//							Thread.sleep(1000 * 10);
//						} catch (InterruptedException e2) {
//							// TODO Auto-generated catch block
//							e2.printStackTrace();
//						}
//						try {
//							server.s.connect(userToken);
//							playRoom.appendChatArea("<系統> ： 重新連線成功。");
//							updateScore.resume();
//							updateChessBoard.resume();
//						} catch (RemoteException e1) {
//							// TODO Auto-generated catch block
//							playRoom.appendChatArea("<系統> ： 重新連線失敗。");
//							e1.printStackTrace();
//						}
//						room = server.getRoom();
//						System.out.println("room" + room);
//					}
					e.printStackTrace();
				}
			}
		});
		updateChessBoard.start();
	}
//	
//	public String[][] updateChessBoardInfo() {
//		String[][] chesses = new String[8][4];
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 4; j++) {
//				chesses[i][j] = "cover";
//			}
//		}
//		chesses[0][0] = "NULL";
//		chesses[1][3] = "redHorse";
//		return chesses;
//	}
	
	
}
