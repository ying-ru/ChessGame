package server.rmi;

import java.rmi.RemoteException;
import java.util.LinkedList;
import server.jdbc.DataBase;
import server.rule.ChessBoard;
import server.rule.Rule;

/** 房間結束時回傳資料 以及刪除房間問題 **/
public class Room {
	private int roomNum;
	private ChessBoard chessBoard;
	private String player0UserToken = "";
	private String player1UserToken = "";
	private int nowPlay = 0;
	private DataBase dataBase;
	private Rule temp;
	private boolean first = true;
	private boolean isEnd = false;
	private Thread playTooLong;
	private String updatePlayer0, updatePlayer1;
	private boolean isGameOver = false;
	private String status;

	public LinkedList<String> chatMsg = new LinkedList<String>();

	public Room(int roomNum, String player0UserToken, String player1UserToken,
			DataBase dataBase) {
		this.roomNum = roomNum;
		this.temp = new Rule();
		this.chessBoard = new ChessBoard();
		this.player0UserToken = player0UserToken;
		this.player1UserToken = player1UserToken;
		this.dataBase = dataBase;
		this.updatePlayer0 = null;
		this.updatePlayer1 = null;
		update();
	}
	
	private void changePlayer() { // 改變現在玩家
		nowPlay = (nowPlay + 1) % 2;
	}
	
	private void update() {
		playTooLong = new Thread(new Runnable() {
			@Override
			public void run() {
				int count0 = 0;
				int count1 = 0;
				try {
					while (true) {
						Thread.sleep(500);
						if (updatePlayer0 == null && count0 < 20) {
							count0++;
						} else if (count0 >= 20) {
							if (!isEnd) {
								isEnd = true;
								isGameOver = true;
								status = "disconnect0";

								int win = dataBase.selectWin(player1UserToken);
								int lose = dataBase
										.selectLose(player1UserToken);
								win++;
								dataBase.update(player1UserToken, win, lose);
								win = dataBase.selectWin(player0UserToken);
								lose = dataBase.selectLose(player0UserToken);
								lose++;
								dataBase.update(player0UserToken, win, lose);
							}
						} else {
							updatePlayer0 = null;
							count0 = 0;
						}
						if (updatePlayer1 == null && count1 < 20) {
							count1++;
						} else if (count1 >= 20) {

							if (!isEnd) {
								isEnd = true;
								isGameOver = true;
								status = "disconnect1";
								int win = dataBase.selectWin(player0UserToken);
								int lose = dataBase
										.selectLose(player0UserToken);
								win++;
								dataBase.update(player0UserToken, win, lose);
								win = dataBase.selectWin(player1UserToken);
								lose = dataBase.selectLose(player1UserToken);
								lose++;
								dataBase.update(player1UserToken, win, lose);
							}
						} else {
							updatePlayer1 = null;
							count1 = 0;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		playTooLong.start();
	}
	
	// API
	public int getRoomNum() {
		return roomNum;
	}

	public String getStatus() {
		return status;
	}

	public String getPlayer0UserToken() {
		return player0UserToken;
	}

	public String getPlayer1UserToken() {
		return player1UserToken;
	}

	public boolean moveChess(int roomNum, String UserToken, int xOfStart,
			int yOfStart, int xOfEnd, int yOfEnd) {
		boolean ActionSuccess = false;
		if (this.roomNum == roomNum) {
			if ((nowPlay == 0 && UserToken.equals(player0UserToken))
					|| (nowPlay == 1 && UserToken.equals(player1UserToken))) {
				ActionSuccess = temp.moveRule(first, nowPlay, chessBoard,
						xOfStart, yOfStart, xOfEnd, yOfEnd);
				if (ActionSuccess) {
					first = false;
				}
			} else { // wrong order
				ActionSuccess = false;
			}
		} else {
			ActionSuccess = false;
		}

		if (ActionSuccess) {
			changePlayer();
		}
		return ActionSuccess;
	}

	public boolean isWin(String userToken) {
		String rivalToken;
		if (userToken.equals(player0UserToken)) {
			updatePlayer0 = userToken;
		} else {
			updatePlayer1 = userToken;
		}
		if (userToken.equals(player0UserToken)) {
			rivalToken = player1UserToken;
		} else {
			rivalToken = player0UserToken;
		}
		if (getScore(userToken) == 16 && !isEnd) {
			isEnd = true;
			isGameOver = true;
			if (userToken.equals(player0UserToken)) {
				status = "outcome0";
			} else {
				status = "outcome1";
			}
			int win = dataBase.selectWin(userToken);
			int lose = dataBase.selectLose(userToken);
			win++;
			dataBase.update(userToken, win, lose);
			win = dataBase.selectWin(rivalToken);
			lose = dataBase.selectLose(rivalToken);
			lose++;
			dataBase.update(rivalToken, win, lose);
			return true;
		} else {
			return false;
		}
	}

	public int getScore(String userToken) {
		int player;
		if (userToken.equals(player0UserToken)) {
			player = 0;
		} else {
			player = 1;
		}
		return temp.score(player);
	}

	public boolean hasChess(ChessBoard chessBoard, int toX, int toY) {
		boolean hasChess = false;

		return hasChess;
	}

	public String[][] updateChessBoardInfo(String UserToken) { /** 同步資訊問題 **/
		String[][] chessName;
		chessName = chessBoard.getChessName();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessBoard.getChessBoard()[i][j] == null) {
					chessName[i][j] = "NULL";
				} else {
					chessName[i][j] = chessBoard.getChessBoard()[i][j]
							.getName();
					if (chessBoard.getChessBoard()[i][j].getColor() == 0) {
						chessName[i][j] = "red" + chessName[i][j];
					} else {
						chessName[i][j] = "black" + chessName[i][j];
					}
					if (chessBoard.getChessBoard()[i][j].getCover() == false) {
						chessName[i][j] = "cover";
					}
				}

			}
		}
		return chessName;
	}

	public boolean chat(String UserToken, String msg) {
		boolean ActionSuccess = false;
		chatMsg.add(msg);
		chatMsg.add(msg);
		return ActionSuccess;
	}

	public boolean isTurnUser(String userToken) throws RemoteException {
		if (userToken.equals(player0UserToken)) {
			updatePlayer0 = userToken;
		} else {
			updatePlayer1 = userToken;
		}
		boolean turnUser = (nowPlay == 0 && userToken.equals(player0UserToken))
				|| (nowPlay == 1 && userToken.equals(player1UserToken));
		return turnUser;
	}

	public String updateChat() throws RemoteException {
		String msg;
		msg = "";
		if (hasNewMsg()) {
			msg = chatMsg.removeFirst();
		}
		return msg;
	}

	public boolean hasNewMsg() throws RemoteException {
		boolean hasNewMsg = !(chatMsg.isEmpty());
		return hasNewMsg;
	}

	public void exit(String userToken) throws RemoteException {
		String rivalToken;
		boolean isPlayer0 = userToken.equals(player0UserToken);
		if (userToken.equals(player0UserToken)) {
			rivalToken = player1UserToken;
		} else {
			rivalToken = player0UserToken;
		}
		if (userToken.equals(player0UserToken)) {
			userToken = player1UserToken;
		} else {
			userToken = player0UserToken;
		}
		if (!isEnd) {
			isEnd = true;
			isGameOver = true;
			if (isPlayer0) {
				status = "exit0";
			} else {
				status = "exit1";
			}
			int win = dataBase.selectWin(userToken);
			int lose = dataBase.selectLose(userToken);
			win++;
			dataBase.update(userToken, win, lose);
			win = dataBase.selectWin(rivalToken);
			lose = dataBase.selectLose(rivalToken);
			lose++;
			dataBase.update(rivalToken, win, lose);
		}
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void printResult(String userToken) {
		if (status.equals("disconnect0") || status.equals("disconnect0Again")) {
			if (status.equals("disconnect0")
					&& userToken.equals(player1UserToken)) {
				status = "disconnect0Again";
				chatMsg.add("<系統> ： 對方斷線");
				chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
			} else if (userToken.equals(player0UserToken)) {
				status = "disconnect0OK";
				chatMsg.add("<系統> ： 上局斷線逾時");
				chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
			}
		} else if (status.equals("disconnect1")
				|| status.equals("disconnect1Again")) {
			if (status.equals("disconnect1")
					&& userToken.equals(player0UserToken)) {
				status = "disconnect1Again";
				chatMsg.add("<系統> ： 對方斷線");
				chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
			} else if (userToken.equals(player1UserToken)) {
				status = "disconnect1OK";
				chatMsg.add("<系統> ： 上局斷線逾時");
				chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
			}
		} else if (status.equals("outcome0")) {
			status = "OK";
			chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
			chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
		} else if (status.equals("outcome1")) {
			status = "OK";
			chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
			chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
		} else if (status.equals("exit0")) {
			status = "OK";
			chatMsg.add("<系統> ： " + player0UserToken + " 離開此局");
			chatMsg.add("<系統> ： " + player0UserToken + " 離開此局");
			chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
			chatMsg.add("<系統> ： 玩家<" + player1UserToken + ">獲勝");
		} else if (status.equals("exit1")) {
			status = "OK";
			chatMsg.add("<系統> ： " + player1UserToken + " 離開此局");
			chatMsg.add("<系統> ： " + player1UserToken + " 離開此局");
			chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
			chatMsg.add("<系統> ： 玩家<" + player0UserToken + ">獲勝");
		}
	}
	// API END
}
