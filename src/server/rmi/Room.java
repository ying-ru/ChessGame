package server.rmi;


import java.rmi.RemoteException;
import java.util.LinkedList;

import server.jdbc.DataBase;
import server.rule.ChessBoard;
import server.rule.Rule;
/**  房間結束時回傳資料 以及刪除房間問題     **/
public class Room 
{
	int roomNum;
	private ChessBoard chessBoard;
	private String player0UserToken;
	private String player1UserToken;
	private int nowPlay = 0;
	private LinkedList<String> chatMsg = new LinkedList<String>();
	private DataBase dataBase;
	private Rule temp;
	private boolean first = true;
	private boolean isEnd = false;
	
	public Room(int roomNum,String player0UserToken,String player1UserToken, DataBase dataBase) 
	{
		this.roomNum = roomNum;
		this.temp = new Rule();
		this.chessBoard = new ChessBoard(); /**  兩個棋盤不同  **/
		this.player0UserToken = player0UserToken;
		this.player1UserToken = player1UserToken;
		this.dataBase = dataBase;
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
		if (getScore(userToken) == 16 && !isEnd) {
			int win = dataBase.selectWin(userToken);
			int lose = dataBase.selectLose(userToken);
			win++;
			isEnd = true;
			dataBase.update(userToken, win, lose);
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
	
	public boolean isTurnUser(String UserToken)
			throws RemoteException {
		// TODO Auto-generated method stub
		boolean turnUser = (nowPlay == 0 && UserToken.equals(player0UserToken)) || (nowPlay == 1 && UserToken.equals(player1UserToken));
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
	
	
}
