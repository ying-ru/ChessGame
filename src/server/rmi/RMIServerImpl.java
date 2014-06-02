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
	private DataBase dataBase;
	private LinkedList<Room> roomlist = new LinkedList<Room>();
	private HashMap<String, Integer> waitingPlayer = new HashMap<String, Integer>();
	private LinkedList<Player> online = new LinkedList<Player>();// 紀錄每個玩家屬於哪個房間

	public RMIServerImpl() throws java.rmi.RemoteException {
		super();
		dataBase = new DataBase();
	}

	private String getDateTime() {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
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

	// API
	@Override
	public String check(String APIToken, String SecretToken) { // if check fail?
		String startTime = getDateTime();
		return startTime;
	}

	@Override
	public int connect(String userToken) {
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
			return roomNum - 1;
		}
	}

	@Override
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

	@Override
	public String getRivalToken(int roomNum, String userToken) {
		if (roomlist.get(getRoomIndex(roomNum)).getPlayer0UserToken()
				.equals(userToken)) {
			return roomlist.get(getRoomIndex(roomNum)).getPlayer1UserToken();
		} else {
			return roomlist.get(getRoomIndex(roomNum)).getPlayer0UserToken();
		}
	}

	@Override
	public Player getRivalData(String rivalToken) {
		Player p = null;
		for (int i = 0; i < online.size(); i++) {
			if (online.get(i).getUserToken().equals(rivalToken)) {
				p = online.get(i);
			}
		}
		return p;
	}

	@Override
	public boolean moveChess(int roomNum, String userToken, int xOfStart,
			int yOfStart, int xOfEnd, int yOfEnd) {
		boolean ActionSuccess = false;
		ActionSuccess = roomlist.get(getRoomIndex(roomNum)).moveChess(roomNum,
				userToken, xOfStart, yOfStart, xOfEnd, yOfEnd);
		return ActionSuccess;
	}

	public String[][] updateChessBoardInfo(int roomNum, String userToken) {
		return roomlist.get(getRoomIndex(roomNum)).updateChessBoardInfo(
				userToken);
	}

	@Override
	public boolean chat(int roomNum, String userToken, String msg) {
		boolean ActionSuccess = false;
		ActionSuccess = roomlist.get(getRoomIndex(roomNum))
				.chat(userToken, msg);
		return ActionSuccess;
	}

	@Override
	public boolean isTurnUser(int roomNum, String userToken)
			throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).isTurnUser(userToken);
	}

	@Override
	public String updateChat(int roomNum) throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).updateChat();
	}

	@Override
	public boolean hasNewMsg(int roomNum) throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).hasNewMsg();
	}

	@Override
	public int getScore(int roomNum, String userToken) throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).getScore(userToken);
	}

	@Override
	public boolean isWin(int roomNum, String userToken) throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).isWin(userToken);
	}

	@Override
	public int getWin(String userToken) throws RemoteException {
		dataBase.selectUser(userToken);
		return dataBase.selectWin(userToken);
	}

	@Override
	public int getLose(String userToken) throws RemoteException {
		dataBase.selectUser(userToken);
		return dataBase.selectLose(userToken);
	}

	@Override
	public void exit(int roomNum, String userToken) throws RemoteException {
		roomlist.get(getRoomIndex(roomNum)).exit(userToken);
		for (int i = 0; i < online.size(); i++) {
			if (online.get(i).getUserToken().equals(userToken)) {
				online.remove(i);
			}
		}
		// dataBase.delete(userToken);
	}

	@Override
	public boolean isGameOver(int roomNum) throws RemoteException {
		return roomlist.get(getRoomIndex(roomNum)).isGameOver();
	}

	@Override
	public void printResult(int roomNum, String userToken)
			throws RemoteException {
		roomlist.get(getRoomIndex(roomNum)).printResult(userToken);
	}

	@Override
	public void removePlayer(int roomNum) throws RemoteException {
		Room room = roomlist.get(getRoomIndex(roomNum));
		String status = room.getStatus();
		String player0 = room.getPlayer0UserToken();
		String player1 = room.getPlayer1UserToken();
		if (isGameOver(roomNum)) {
			if (status.equals("disconnect0Again")
					|| status.equals("disconnect1OK")) {
				for (int i = 0; i < online.size(); i++) {
					if (online.get(i).getUserToken().equals(player1)) {
						room.chatMsg.add("<系統> ： 遊戲結束，中斷連線。");
						System.out.println(player1 + "leave");
						online.remove(i);
					}
				}
			} else if (status.equals("disconnect0OK")
					|| status.equals("disconnect1Again")) {
				for (int i = 0; i < online.size(); i++) {
					if (online.get(i).getUserToken().equals(player0)) {
						room.chatMsg.add("<系統> ： 遊戲結束，中斷連線。");
						System.out.println(player0 + "leave");
						online.remove(i);
					}
				}
			} else if (status.equals("OK")) {
				for (int i = 0; i < online.size(); i++) {
					room.chatMsg.add("<系統> ： 遊戲結束，中斷連線。");
					room.chatMsg.add("<系統> ： 遊戲結束，中斷連線。");
					System.out.println(player0 + "leave");
					System.out.println(player1 + "leave");
					if (online.get(i).getUserToken().equals(player0)
							|| online.get(i).getUserToken().equals(player1)) {
						online.remove(i);
					}
				}
			}
		}
	}
	// API END
}
