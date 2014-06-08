package data.chessPiece;

import java.awt.Point;

public class ChessPiece extends ChessPiecePicture {
	private String chessName = "";
	private ChessPieceLocation chessBoardLoc;
	private int beforeX, beforeY, afterX, afterY;
	private int grid;
	
	public ChessPiece(Point point, int chessCoorX, int chessCoorY, int grid, String chessName, ChessPieceLocation chessBoardLoc) {
		super(point, grid, chessName);
		this.grid = grid;
		this.chessBoardLoc = chessBoardLoc;
		this.chessName = chessName;
		this.beforeX = chessCoorX;
		this.beforeY = chessCoorY;
	}

	public void goBack() {
		setLocation(chessBoardLoc.getChessLocation(beforeX, beforeY));
		setAfterX(beforeX);
		setAfterY(beforeY);
	}

	public String getChessName() {
		return chessName;
	}

	public int getFrameX() {
		return getLocation().x;
	}

	public int getFrameY() {
		return getLocation().y;
	}
	
	public int getGrid() {
		return grid;
	}
	
	public int getBeforeX() {
		return beforeX;
	}
	
	public int getBeforeY() {
		return beforeY;
	}
	
	public int getAfterX() {
		return afterX;
	}
	
	public int getAfterY() {
		return afterY;
	}
	
	public void setBeforeX(int beforeX) {
		this.beforeX = beforeX;
	}
	
	public void setBeforeY(int beforeY) {
		this.beforeY = beforeY;
	}
	
	public void setAfterX(int afterX) {
		this.afterX = afterX;
	}
	
	public void setAfterY(int afterY) {
		this.afterY = afterY;
	}
}
