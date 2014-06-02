package server.rmi;

import java.rmi.Remote;

import server.data.player.Player;

public interface ServerInterface extends Remote {
	public String check(String APIToken, String SecretToken)
			throws java.rmi.RemoteException;

	public int connect(String userToken) throws java.rmi.RemoteException;

	public int getRoomNum(String userToken) throws java.rmi.RemoteException;

	public String getRivalToken(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public Player getRivalData(String rivalToken)
			throws java.rmi.RemoteException;

	public boolean moveChess(int roomNum, String userToken, int xOfStart,
			int yOfStart, int xOfEnd, int yOfEnd)
			throws java.rmi.RemoteException;

	public String[][] updateChessBoardInfo(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public boolean chat(int roomNum, String userToken, String msg)
			throws java.rmi.RemoteException;

	public boolean isTurnUser(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public boolean hasNewMsg(int roomNum) throws java.rmi.RemoteException;

	public String updateChat(int roomNum) throws java.rmi.RemoteException;

	public int getScore(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public boolean isWin(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public int getWin(String userToken) throws java.rmi.RemoteException;

	public int getLose(String userToken) throws java.rmi.RemoteException;

	public void exit(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public boolean isGameOver(int roomNum) throws java.rmi.RemoteException;

	public void printResult(int roomNum, String userToken)
			throws java.rmi.RemoteException;

	public void removePlayer(int roomNum) throws java.rmi.RemoteException;
}
