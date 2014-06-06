package ui.playRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import data.player.Player;

public class PlayerInfoJPanel extends JPanel {

	private JLabel playerAScore, playerBScore, playerAPhoto, playerBPhoto,
			playerAName, playerBName;
	private JLabel playerAWin, playerBWin, playerALose, playerBLose;
	private ImageIcon defaultPlayerAPhoto, defaultPlayerBPhoto;
	private Player playerA, playerB;

	public PlayerInfoJPanel(int locationX, int locationY, int width, int height) {
		setSize(width, height);
		setLocation(locationX, locationY);
		setLayout(null);
		setOpaque(false);
		initImageIcon();
		initPlayer();
		initJLabel();
		initBound();
		initLocation();
		setComponentFont();
	}

	// init Component //

	private void setComponentFont() {
		playerAWin.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerBWin.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerALose.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerBLose.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerAScore.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerBScore.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerAName.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
		playerBName.setFont(new Font(Font.DIALOG, Font.BOLD,
				getHeight() * 7 / 40));
	}

	private void initImageIcon() {
		defaultPlayerAPhoto = new ImageIcon(getClass().getResource(
				"/Image/playerAPhoto.jpg"));
		defaultPlayerBPhoto = new ImageIcon(getClass().getResource(
				"/Image/playerBPhoto.jpg"));
	}

	private void initPlayer() {
		playerA = new Player("playerA", defaultPlayerAPhoto, -1, 0);
		playerB = new Player("playerB", defaultPlayerBPhoto, -1, 0);
	}

	private void initJLabel() {
		playerAWin = new JLabel("勝績：" + 0);
		playerAWin.setBackground(Color.white);
		playerAWin.setOpaque(true);
		add(playerAWin);

		playerBWin = new JLabel("勝績：" + 0);
		playerBWin.setBackground(Color.white);
		playerBWin.setOpaque(true);
		add(playerBWin);

		playerALose = new JLabel("敗績：" + 0);
		playerALose.setBackground(Color.white);
		playerALose.setOpaque(true);
		add(playerALose);

		playerBLose = new JLabel("敗績：" + 0);
		playerBLose.setBackground(Color.white);
		playerBLose.setOpaque(true);
		add(playerBLose);

		playerAScore = new JLabel("分數：" + playerA.getScore() + "分");
		playerAScore.setBackground(Color.white);
		playerAScore.setOpaque(true);
		add(playerAScore);

		playerBScore = new JLabel("分數：" + playerB.getScore() + "分");
		playerBScore.setBackground(Color.white);
		playerBScore.setOpaque(true);
		add(playerBScore);

		playerAPhoto = new JLabel();
		playerAPhoto.setSize((getWidth() - 20) / 4, (getWidth() - 20) / 4);
		playerA.getPhoto().setImage(
				playerA.getPhoto()
						.getImage()
						.getScaledInstance(playerAPhoto.getWidth(),
								playerAPhoto.getHeight(), Image.SCALE_DEFAULT));// 設定圖片的顯示
		playerAPhoto.setIcon(playerA.getPhoto());
		add(playerAPhoto);

		playerBPhoto = new JLabel();
		playerBPhoto.setSize((getWidth() - 20) / 4, (getWidth() - 20) / 4);
		playerB.getPhoto().setImage(
				playerB.getPhoto()
						.getImage()
						.getScaledInstance(playerBPhoto.getWidth(),
								playerBPhoto.getHeight(), Image.SCALE_DEFAULT));
		playerBPhoto.setIcon(playerB.getPhoto());
		add(playerBPhoto);

		playerAName = new JLabel("玩家：" + playerA.getName());
		playerAName.setBackground(Color.white);
		playerAName.setOpaque(true);
		add(playerAName);

		playerBName = new JLabel("玩家：" + playerB.getName());
		playerBName.setBackground(Color.white);
		playerBName.setOpaque(true);
		add(playerBName);
	}

	private void initBound() {
		playerAWin.setBounds((getWidth() / 2) - (getWidth() / 4), 0,
				(getWidth() - 20) / 4, (getWidth() - 20) / 18);
		playerBWin.setBounds(getWidth() - (getWidth() - 20) / 4 - getWidth()
				/ 4, 0, (getWidth() - 20) / 4, (getWidth() - 20) / 18);
		playerALose.setBounds((getWidth() / 2) - (getWidth() / 4),
				(getWidth() - 20) / 18, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
		playerBLose.setBounds(getWidth() - (getWidth() - 20) / 4 - getWidth()
				/ 4, (getWidth() - 20) / 18, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
		playerAScore.setBounds((getWidth() / 2) - (getWidth() / 4),
				(getWidth() - 20) / 9, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
		playerBScore.setBounds(getWidth() - (getWidth() - 20) / 4 - getWidth()
				/ 4, (getWidth() - 20) / 9, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
		playerAName.setBounds((getWidth() / 2) - (getWidth() / 4), getHeight()
				- getHeight() / 4, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
		playerBName.setBounds(getWidth() - (getWidth() - 20) / 4 - getWidth()
				/ 4, getHeight() - getHeight() / 4, (getWidth() - 20) / 4,
				(getWidth() - 20) / 18);
	}

	private void initLocation() {
		playerAPhoto.setLocation(0, 0);
		playerBPhoto.setLocation(getWidth() - (getWidth() - 20) / 4, 0);
	}

	// init Component end //

	// API//
	public void setPlayerAWin(int win) {
		this.playerAWin.setText("勝績：" + win);
	}

	public void setPlayerBWin(int win) {
		this.playerBWin.setText("勝績：" + win);
	}

	public void setPlayerALose(int lose) {
		this.playerALose.setText("敗績：" + lose);
	}

	public void setPlayerBLose(int lose) {
		this.playerBLose.setText("敗績：" + lose);
	}

	public void setplayerAName(String name) {
		this.playerAName.setText("玩家：" + name);
	}

	public void setplayerBName(String name) {
		this.playerBName.setText("玩家：" + name);
	}

	public void setPlayerAScore(String score) {
		this.playerAScore.setText("分數：" + score + "分");
	}

	public void setPlayerBScore(String score) {
		this.playerBScore.setText("分數：" + score + "分");
	}

	public void setPlayerAPhoto(ImageIcon photo) {
		photo.setImage(photo.getImage().getScaledInstance(
				playerAPhoto.getWidth(), playerAPhoto.getHeight(),
				Image.SCALE_DEFAULT));
		playerAPhoto.setIcon(photo);
	}

	public void setPlayerBPhoto(ImageIcon photo) {
		photo.setImage(photo.getImage().getScaledInstance(
				playerBPhoto.getWidth(), playerBPhoto.getHeight(),
				Image.SCALE_DEFAULT));
		playerBPhoto.setIcon(photo);
	}

	// API end //
}
