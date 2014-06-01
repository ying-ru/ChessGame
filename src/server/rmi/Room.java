package server.rmi;

import java.rmi.RemoteException;
import java.util.LinkedList;
import server.jdbc.DataBase;
import server.rule.ChessBoard;
import server.rule.Rule;

/**  房間結束時回傳資料 以及刪除房間問題     **/
public class Room 
{
	private int roomNum;
	private ChessBoard chessBoard;
	private String player0UserToken;
	private String player1UserToken;
	private int nowPlay = 0;
	private LinkedList<String> chatMsg = new LinkedList<String>();
	private DataBase dataBase;
	private Rule temp;
	private boolean first = true;
	private boolean isEnd = false;
	private Thread playTooLong;
	private String updatePlayer0, updatePlayer1;
	
	public Room(int roomNum,String player0UserToken,String player1UserToken, DataBase dataBase) 
	{
		this.roomNum = roomNum;
		this.temp = new Rule();
		this.chessBoard = new ChessBoard(); /**  兩個棋盤不同  **/
		this.player0UserToken = player0UserToken;
		this.player1UserToken = player1UserToken;
		this.dataBase = dataBase;
		this.updatePlayer0 = null;
		this.updatePlayer1 = null;
		update();
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	
	public String getPlayer0UserToken() {
		return player0UserToken;
	}
	
	public String getPlayer1UserToken() {
		return player1UserToken;
	}
	
	private void changePlayer() //改變現在玩家
	{
		nowPlay = ( nowPlay + 1 ) % 2;
	}
	
	public boolean moveChess(int roomNum , String UserToken,int xOfStart,int yOfStart,int xOfEnd,int yOfEnd)//回傳型態變化
	{
		boolean ActionSuccess = false;
		
		if (this.roomNum == roomNum) {//確認房間編號?
			// 實作 moveChess
			if ((nowPlay == 0 && UserToken.equals(player0UserToken))
					|| (nowPlay == 1 && UserToken.equals(player1UserToken))) {
				//Rule temp = new Rule();
				ActionSuccess = temp.moveRule(first, nowPlay, chessBoard, xOfStart, yOfStart,
						xOfEnd, yOfEnd);
				if (ActionSuccess) {
					first = false;
				}
			} else {// 玩家順序不對
				ActionSuccess = false;
			}
		}else{
			ActionSuccess = false ;
		}
		
		if (ActionSuccess) {
			changePlayer();
		}
		
		return ActionSuccess;
	}
	
	//判斷輸贏結果
	public boolean isWin(String userToken) {
		String rivalToken;
		setUpdatePlayer(userToken);
		if (userToken.equals(player0UserToken)) {
			rivalToken = player1UserToken;
		} else {
			rivalToken = player0UserToken;
		}
		if (getScore(userToken) == 16 && !isEnd) {
			isEnd = true;
			int win = dataBase.selectWin(userToken);
			int lose = dataBase.selectLose(userToken);
			win++;
			dataBase.update(userToken, win, lose);
			win = dataBase.selectWin(rivalToken);
			lose = dataBase.selectLose(rivalToken);
			lose++;
			dataBase.update(rivalToken, win, lose);
			chatMsg.add("<系統> ： " + userToken + "獲勝");
			chatMsg.add("<系統> ： " + userToken + "獲勝");
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
	
//	public boolean openChess(String UserToken,int x,int y)
//	{
//		boolean ActionSuccess = false ;
//		//實作 openChess
//		
//		return ActionSuccess;
//	}
	
	public boolean hasChess(ChessBoard chessBoard , int toX , int toY){
		boolean hasChess = false;
		
		return hasChess;
	}
	
	public String[][] updateChessBoardInfo(String UserToken) /** 同步資訊問題   **/
	{	
		//實作 updateChessBoardInfo 當非該玩家時不要讓他更新棋盤 (做等待動作)
		String[][] chessName;
		chessName = chessBoard.getChessName();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				if (chessBoard.getChessBoard()[i][j] == null) {
					chessName[i][j] = "NULL";
				} else {
					chessName[i][j] = chessBoard.getChessBoard()[i][j].getName();
					if (chessBoard.getChessBoard()[i][j].getColor() == 0) {
						chessName[i][j] = "red" + chessName[i][j];
					} else {
						chessName[i][j] = "black" + chessName[i][j];
					}
					if(chessBoard.getChessBoard()[i][j].getCover() == false) {
						chessName[i][j] = "cover";
					}
				}
				
			}
		}
		return chessName;
	}
	
	public boolean chat(String UserToken,String msg)/** 同步資訊問題   **/
	{
		boolean ActionSuccess = false ;
		//實作 chat 當某一方玩家使用此method 需通知另一方玩家更新
		chatMsg.add(msg);
		chatMsg.add(msg);
		return ActionSuccess;
	}
	
	public boolean isTurnUser(String userToken)
			throws RemoteException {
		// TODO Auto-generated method stub
		setUpdatePlayer(userToken);
		boolean turnUser = (nowPlay == 0 && userToken.equals(player0UserToken)) || (nowPlay == 1 && userToken.equals(player1UserToken));
		return turnUser;
	}
	
	public String updateChat()
			throws RemoteException {
		// TODO Auto-generated method stub
		String msg;
		msg = "";
		if (hasNewMsg()) {
			msg = chatMsg.removeFirst();
		}
		return msg;
	}
	
	public boolean hasNewMsg()
			throws RemoteException {
		// TODO Auto-generated method stub
		boolean hasNewMsg = !(chatMsg.isEmpty());
		return hasNewMsg;
	}
	
	public void exit(String userToken)
			throws RemoteException {
		// TODO Auto-generated method stub
		
		chatMsg.add("<系統> ： " + userToken + "離開");
		//判斷獲勝
		String rivalToken;
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
			int win = dataBase.selectWin(userToken);
			int lose = dataBase.selectLose(userToken);
			win++;
			dataBase.update(userToken, win, lose);
			win = dataBase.selectWin(rivalToken);
			lose = dataBase.selectLose(rivalToken);
			lose++;
			dataBase.update(rivalToken, win, lose);
			chatMsg.add("<系統> ： " + userToken + "獲勝");
			chatMsg.add("<系統> ： " + userToken + "獲勝");
		}
		
	}
	
	private void setUpdatePlayer(String userToken) {
		if (userToken.equals(player0UserToken)) {
			updatePlayer0 = userToken;
		} else {
			updatePlayer1 = userToken;
		}
	}
	
	private void update() {
		playTooLong = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count0 = 0;
				int count1 = 0;
				try {
					while (true) {
						Thread.sleep(500);
						if (updatePlayer0 == null && count0 < 130) {
							count0++;
						} else if (count0 >= 130) {
							chatMsg.add("<系統> ： " + player0UserToken + "斷線。");
							if (!isEnd) {
								isEnd = true;
								int win = dataBase.selectWin(player1UserToken);
								int lose = dataBase.selectLose(player1UserToken);
								win++;
								dataBase.update(player1UserToken, win, lose);
								win = dataBase.selectWin(player0UserToken);
								lose = dataBase.selectLose(player0UserToken);
								lose++;
								dataBase.update(player0UserToken, win, lose);
								chatMsg.add("<系統> ： " + player1UserToken + "獲勝");
								chatMsg.add("<系統> ： " + player1UserToken + "獲勝");
							}
						} else {
							updatePlayer0 = null;
							count0 = 0;
						}
						if (updatePlayer1 == null && count1 < 130) {
							count1++;
						} else if (count1 >= 130) {
							chatMsg.add("<系統> ： " + player1UserToken + "斷線。");
							if (!isEnd) {
								isEnd = true;
								int win = dataBase.selectWin(player0UserToken);
								int lose = dataBase.selectLose(player0UserToken);
								win++;
								dataBase.update(player0UserToken, win, lose);
								win = dataBase.selectWin(player1UserToken);
								lose = dataBase.selectLose(player1UserToken);
								lose++;
								dataBase.update(player1UserToken, win, lose);
								chatMsg.add("<系統> ： " + player0UserToken + "獲勝");
								chatMsg.add("<系統> ： " + player0UserToken + "獲勝");
							}
						} else {
							updatePlayer1 = null;
							count1 = 0;
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		playTooLong.start();
	}
}
