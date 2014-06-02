package server.data.chessPiece;

public class Chess {
	private int color;// 0為紅
	private boolean cover;
	private boolean dead;
	private String name;
	private int priority;// 權重判斷
	private int x;
	private int y;

	public Chess(int color, boolean cover, boolean dead, String name, int x,
			int y, int priority) {
		this.color = color;
		this.cover = cover;
		this.dead = dead;
		this.name = name;
		this.x = x;
		this.y = y;
		this.priority = priority;
	}

	public int getColor() {
		return color;
	}

	public boolean getCover() {
		return cover;
	}

	public void setCover(boolean cover) {
		this.cover = cover;
	}

	public boolean getDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public String getName() {
		return name;
	}

	public int getPriority() {
		return priority;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}