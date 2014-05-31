package server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import server.jdbc.DataBase;
import server.data.player.Player;

public class RMIServerImpl extends UnicastRemoteObject implements
		ServerInterface {
	private int roomNum = 0;
	private int count = 0;
	private LinkedList<Room> roomlist = new LinkedList<Room>();
	private DataBase dataBase;
	// private LinkedList<String> waitingPlayer = new LinkedList<String>();/**
	// 形態要改過? **/

	private HashMap<String, Integer> waitingPlayer = new HashMap<String, Integer>();
	private LinkedList<Player> online = new LinkedList<Player>();// 紀錄每個玩家屬於哪個房間

	// This implementation must have a public constructor.
	// The constructor throws a RemoteException.
	public RMIServerImpl() throws java.rmi.RemoteException {
		super(); // Use constructor of parent class
		dataBase = new DataBase();
	}
	
	// Implementation of the service defended in the interface
	public String check(String APIToken, String SecretToken) { //if check fail?
		String startTime = getDateTime();
		return startTime;
	}

	// 以下為寫完的method
	private String getDateTime() {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
	}

	public int connect(String userToken)// 隨機配對
	{
		Player user;
		Player rival;
		String rivalToken = "";
		System.out.println(waitingPlayer.isEmpty());
		if (waitingPlayer.isEmpty() && getRoomNum(userToken) == -1) {
			waitingPlayer.put(userToken, -1);
			return -1;
		} else if (getRoomNum(userToken) != -1) {
			return getRoomNum(userToken);
		} else {
			for (String key : waitingPlayer.keySet()) {
				if (waitingPlayer.get(key) == -1) {
					if (key.equals(userToken)) {
						return -1;
					}
					rivalToken = key;
					break;
				}
			}
			// 隨機找尋等待玩家清單中的人
			Room room;
			
			room = new Room(roomNum, userToken, rivalToken, dataBase);
			user = new Player(userToken, roomNum);
			rival = new Player(rivalToken, roomNum);
			roomlist.add(room);
			online.add(user);
			online.add(rival);
			count = (count + 1) % 2;
			waitingPlayer.remove(rivalToken);
			roomNum++;
			return roomNum-1;
		}
	}

	public int getRoomNum(String userToken) {
		int roomNum;
		roomNum = -1;
		for (Player p : online) {
			if (p.getUserToken().equals(userToken)) {
				roomNum = p.getRoom();
			}
		}
		return roomNum;
	}

	// 配對成功後 對手1取得對手的Token
	public String getRivalToken(int roomNum, String userToken) {
		if (roomlist.get(getRoomIndex(roomNum)).getPlayer0UserToken().equals(userToken)) {
			return roomlist.get(getRoomIndex(roomNum)).getPlayer1UserToken();
		} else {
			return roomlist.get(getRoomIndex(roomNum)).getPlayer0UserToken();
		}
	}
	
	
	public Player getRivalData(String rivalToken) {
		Player p = null;
		for (int i = 0; i < online.size(); i++) {
			if (online.get(i).getUserToken().equals(rivalToken)) {
				p = online.get(i);
			}
		}
		return p;
	}

	public int connect(String userToken, String rivalToken)// 選擇玩家
	{
		Room room = new Room(roomNum, userToken, rivalToken, dataBase);
		roomNum++;
		roomlist.add(room);
		waitingPlayer.put(rivalToken, roomNum);
		
		// 給一個RoomNumber
		return room.getRoomNum();
	}

	private int getRoomIndex(int roomNum)// 找到實際的位址
	{
		int roomIndex = -1;
		for (int i = 0; i < roomlist.size(); i++) {
			if (roomlist.get(i).getRoomNum() == roomNum) {
				roomIndex = roomlist.get(i).getRoomNum();
			}
		}
		return roomIndex;
	}

	public boolean moveChess(int roomNum, String userToken, int xOfStart,
			int yOfStart, int xOfEnd, int yOfEnd) {
		boolean ActionSuccess = false;
		ActionSuccess = roomlist.get(getRoomIndex(roomNum)).moveChess(roomNum,
				userToken, xOfStart, yOfStart, xOfEnd, yOfEnd);
		return ActionSuccess;
	}

	// public boolean openChess(int roomNum,String userToken,int x,int y)
	// {
	// boolean ActionSuccess = false ;
	// ActionSuccess =
	// roomlist.get(getRoomIndex(roomNum)).openChess(userToken,x, y);
	// return ActionSuccess;
	// }
	public String[][] updateChessBoardInfo(int roomNum, String userToken) {
		return roomlist.get(getRoomIndex(roomNum)).updateChessBoardInfo(
				userToken);
	}

	public boolean chat(int roomNum, String userToken, String msg) {
		boolean ActionSuccess = false;
		ActionSuccess = roomlist.get(getRoomIndex(roomNum))
				.chat(userToken, msg);
		return ActionSuccess;
	}

	@Override
	public boolean isTurnUser(int roomNum, String userToken)
			throws RemoteException {
		// TODO Auto-generated method stub
		return roomlist.get(getRoomIndex(roomNum)).isTurnUser(userToken);
	}

	@Override
	public String updateChat(int roomNum)
			throws RemoteException {
		// TODO Auto-generated method stub
		return roomlist.get(getRoomIndex(roomNum)).updateChat();
	}

	@Override
	public boolean hasNewMsg(int roomNum)
			throws RemoteException {
		// TODO Auto-generated method stub
		return roomlist.get(getRoomIndex(roomNum)).hasNewMsg();
	}

	@Override
	public int getScore(int roomNum, String userToken) throws RemoteException {
		// TODO Auto-generated method stub
		return roomlist.get(getRoomIndex(roomNum)).getScore(userToken);
	}

	@Override
	public boolean isWin(int roomNum, String userToken) throws RemoteException {
		// TODO Auto-generated method stub
		return roomlist.get(getRoomIndex(roomNum)).isWin(userToken);
	}
}
