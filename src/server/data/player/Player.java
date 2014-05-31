package server.data.player;


import java.awt.Image;

import server.jdbc.DataBase;

public class Player
{
	private String userToken;
	private Image picture;
	private int win,lose;
	private int roomNum;
	private DataBase dataBase = new DataBase();
	
	public Player (String userToken, int roomNum) {
		this.userToken = userToken;
		this.roomNum = roomNum;
	}
	
	public String getUserToken() {
		return userToken;
	}
	
	public Image getPicture() {
		return picture;
	}
	
	public int getWin() {
		win = dataBase.selectWin(userToken);
		return win;
	}
	
	public int getLose() {
		lose = dataBase.selectLose(userToken);
		return lose;
	}
	
	public int getRoom() {
		return roomNum;
	}
}