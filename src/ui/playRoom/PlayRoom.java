package ui.playRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import rmi.GameClient;
import ui.ChatPanel;
import ui.MainFrame;

public class PlayRoom extends MainFrame {
	private JLabel background, forWhoToPlay;
	private PlayerInfoJPanel playerInfo;
	private ChessBoard chessBoard;
	private ChatPanel chatArea;
	private JButton leaveBtn;
	private ImageIcon backgroundPhoto;
	private GameClient server;
	private String userToken;
	private Thread updateScore, playTooLong;
	private boolean isRemind = false;
//	private Observable obs;

	public PlayRoom(GameClient server, String userToken) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.userToken = userToken;
		initChessBoard();
		initJPanel();
		initJButton();
		initJLabel();
		initBackground();
		initBound();
		initLocation();
		setComponentFont();
//		setGameObservable();
		revalidate();
		repaint();
		testDrive();
		updateScore();
		time();
	}
	
	private void testDrive() {
//		appendChatArea("Rose > Hello !");

//		setPlayerAPhoto(new ImageIcon("C:/sqa/wallpaper/Desert.jpg"));
		setPlayerAInfoName(userToken);
		setPlayerAInfoWin(1);
		setPlayerAInfoLose(2);
		
//		setPlayerBPhoto(new ImageIcon("C:/sqa/wallpaper/Jellyfish.jpg"));
		setPlayerBInfoName(server.getRivalToken(userToken));
		setPlayerBInfoWin(3);
		setPlayerBInfoLose(4);
	}
	
	// init Component //
	
	private void setComponentFont() {
		leaveBtn.setFont(new Font(Font.DIALOG, Font.BOLD, getHeight()/40));
		forWhoToPlay.setFont(new Font(Font.DIALOG, Font.BOLD, getHeight()/20));
		//readyBtn.setFont(new Font(Font.DIALOG, Font.BOLD, getHeight()/40));
	}
	
//	private void setGameObservable() {
//		obs = getChessBoard().getChessGameObservable();
//	}
	
	private void initJButton() {
		leaveBtn = new JButton("離開");
		leaveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					server.s.exit(server.getRoom(), userToken);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		add(leaveBtn);
//		readyBtn = new JButton("Ready");
//		add(readyBtn);//add到PlayRoom
	}
	
	private void initJLabel() {
		forWhoToPlay = new JLabel("輪到你了");
		forWhoToPlay.setForeground(Color.red);
		add(forWhoToPlay);
	}
	
	private void initBackground() { //加入背景圖片
		backgroundPhoto = new ImageIcon(getClass().getResource("/Image/04.png"));
		background = new JLabel();
		backgroundPhoto.setImage(backgroundPhoto.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT));//設定圖片的顯示
		background.setIcon(backgroundPhoto);
		add(background);
	}
	//設定Panel
	private void initJPanel() {
		playerInfo = new PlayerInfoJPanel(getWidth() /30, getHeight() /90 *62, (getWidth() - getWidth() /10) * 2 / 3, getHeight() /9 *2, server, userToken);
		add(playerInfo);
		chatArea = new ChatPanel(getWidth() - (getWidth() - getWidth() /50) / 3, getHeight() /9 *3, (getWidth() - getWidth() /60 *7) / 3, getHeight() /90 *52, server, userToken);
		add(chatArea);
	}

	private void initChessBoard() {
		chessBoard = new ChessBoard(getWidth() /30, getHeight() /90 *14, (getWidth() - getWidth() /10) * 2 / 3, getHeight() /2);
		add(chessBoard);
	}

	private void initBound() {
		background.setBounds(0, 0, this.getWidth(), this.getHeight());
//		leaveBtn.setBounds(getWidth() - (getWidth() - getWidth() / 50) / 3, getHeight() /90 *14, getWidth() /10, getHeight() /90 *8);
//		readyBtn.setBounds(getWidth() - getWidth() /15 *2, getHeight() /90 *14, getWidth() /10, getHeight() /90 *8);
		forWhoToPlay.setBounds(getWidth() - getWidth() /20 *6, getHeight() /90 *14, getWidth() / 4, getHeight() /90 *8);
		leaveBtn.setBounds(getWidth() - getWidth() /15 *2, getHeight() /90 *14, getWidth() /10, getHeight() /90 *8);
	}
	
	private void initLocation() {
//		chessBoard.setLocation(50, 185);
	}
	
	private int getScore(String userToken) {
		int score;
		score = -1;
		try {
			System.out.println(userToken);
			score = server.s.getScore(server.getRoom(), userToken);
			return score;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
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
							int scoreA = getScore(userToken);
							int scoreB = getScore(server.getRivalToken(userToken));
							playerInfo.setPlayerAScore(scoreA + "");
							playerInfo.setPlayerBScore(scoreB + "");
							if (scoreB - scoreA > 7) {
								appendChatArea("<系統> ： " + userToken + "OH！該加油了！");
							}
							if (scoreA > 11) {
								appendChatArea("<系統> ： " + userToken + "加油，快贏對方了！");
							}
						}
						// update score end
						if (server.s.isTurnUser(server.getRoom(), userToken)) {
							changePlay("輪到你了");
						} else {
							changePlay("等待對方");
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					updateScore.suspend();
					
					int room = server.getRoom();
					while (room == -1) {
						try {
							Thread.sleep(1000 * 5);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							server.s.connect(userToken);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
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
	
	private void time() {
		playTooLong = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String formet = new SimpleDateFormat("a", Locale.US).format(new Date());
					Date d = new Date();
					while (true) {
						d = new Date();
						
						System.out.println(d.getHours()+" : "+d.getMinutes()+" : "+d.getSeconds() + " " + formet);
						if (!isRemind && ((d.getHours() > 10 && formet.equals("PM")) || (d.getHours() < 4 && formet.equals("AM")))) {
							appendChatArea("<系統> ： " + d.getHours() + "點了，很晚囉。");
							isRemind = true;
						}
						Thread.sleep(1000 * 5);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		playTooLong.start();
	}
	// init Component end //

	
	// API //
	
	public void changePlay(String turnWho) {
		forWhoToPlay.setText(turnWho);
	}

	public void appendChatArea(String chatString) {
		chatArea.appendChatArea(chatString);
	}

	public void setPlayerAPhoto(ImageIcon photo) {
		playerInfo.setPlayerAPhoto(photo);
	}

	public void setPlayerBPhoto(ImageIcon photo) {
		playerInfo.setPlayerBPhoto(photo);
	}
	
	public void setPlayerAInfoWin(int win) {
		playerInfo.setPlayerAWin(win);
	}
	
	public void setPlayerBInfoWin(int win) {
		playerInfo.setPlayerBWin(win);
	}
	
	public void setPlayerAInfoLose(int lose) {
		playerInfo.setPlayerALose(lose);
	}
	
	public void setPlayerBInfoLose(int lose) {
		playerInfo.setPlayerBLose(lose);
	}
	
	public void setPlayerAInfoName(String name) {
		chatArea.setLocalPlayerName(name);
		playerInfo.setplayerAName(name);
	}

	public void setPlayerBInfoName(String name) {
		playerInfo.setplayerBName(name);
	}

	public PlayerInfoJPanel getPlayerInfoJPanel() {
		return playerInfo;
	}

	public ChatPanel getChatPanel() {
		return chatArea;
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}

//	// observable //
//
//	public void setChanged() {
//		obs.setChanged();
//	}
//
//	public void addObserver(Observer observer) {
//		obs.addObserver(observer);
//	}
//
//	public int countObservers() {
//		return obs.countObservers();
//	}
//
//	public void deleteObserver(Observer observer) {
//		obs.deleteObserver(observer);
//	}
//
//	public void deleteObservers() {
//		obs.deleteObservers();
//	}
//
//	public void notifyObservers() {
//		obs.notifyObservers();
//	}
//
//	public void notifyObservers(Object o) {
//		obs.notifyObservers(o);
//	}
//
//	public void notifyObservers(Observer observer) {
//		obs.notifyObservers(observer);
//	}

	// API end //
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		PlayRoom p = new PlayRoom();
//	}
	
}
