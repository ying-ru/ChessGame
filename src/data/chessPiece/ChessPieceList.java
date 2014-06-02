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
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		updateChessBoard.start();
	}
}
