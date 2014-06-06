package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import data.chessPiece.ChessPieceList;
import rmi.GameClient;

public class ChatPanel extends JPanel implements Observer {

	private JTextArea chatInputArea, chatTextArea;
	private JScrollPane chatScrollPanel;
	private Thread updateChat;
	private GameClient server;
	private String userToken;

	public ChatPanel(int locationX, int locationY, int width, int height,
			GameClient server, String userToken) {
		this.server = server;
		this.userToken = userToken;
		setSize(width, height);
		setLocation(locationX, locationY);
		setLayout(null);
		initJTextArea();
		initScrollPane();
		setComponentFont();
		updateChat();
	}

	// init Component //

	private void setComponentFont() {
		chatInputArea.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		chatTextArea.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
	}

	private void initJTextArea() {
		// 聊天室顯示框
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		chatTextArea.setLineWrap(true);
		// 聊天室輸入框
		chatInputArea = new JTextArea();
		chatInputArea.setLineWrap(true);
		LineBorder tt = new LineBorder(Color.BLACK);
		chatInputArea.setBorder(tt);

		chatInputArea.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println(chatInputArea.getText()
							.replace("\n", ""));
					// call server chat start
					try {
						server.s.chat(server.getRoom(), userToken, "<"
								+ userToken + ">" + " ： "
								+ chatInputArea.getText().replace("\n", "")
								+ "\n");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					// call server chat end
					chatInputArea.setText("");
				}
			}
		});
		chatInputArea.setBounds(0, getHeight() * 6 / 7, getWidth(), getHeight()
				- getHeight() * 6 / 7);
		add(chatInputArea);
	}

	private void initScrollPane() {
		chatScrollPanel = new JScrollPane();
		chatScrollPanel.setBounds(0, 0, getWidth(), getHeight() * 6 / 7);
		chatScrollPanel
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPanel.getViewport().setView(chatTextArea);
		add(chatScrollPanel);
	}

	// init Component end //

	// API //

	public void appendChatArea(String chatString) {
		chatTextArea.append(chatString.replace('\n', ' ') + "\n");
	}

	public String getLocalPlayerName() {
		return userToken;
	}

	public void setLocalPlayerName(String userToken) {
		this.userToken = userToken;
	}

	// API end //

	private void updateChat() {
		updateChat = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(100);
						// update chat start

						if (server.s.hasNewMsg(server.getRoom(), userToken)) {
							appendChatArea(server.s.updateChat(server.getRoom(), userToken));
						}
						// update chat end
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					appendChatArea("<系統> ： 連線中斷，請重新開啟遊戲。");
					appendChatArea("<系統> ： 若 60 秒仍然無法連線，則強制斷線，並判為輸局。");
					e.printStackTrace();
				}
			}
		});
		updateChat.start();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ChessPieceList) {
			if (arg instanceof String) {
				appendChatArea((String) arg);
			}
		}
	}
}
