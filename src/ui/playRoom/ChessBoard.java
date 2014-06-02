package ui.playRoom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import control.ChessGameObservable;
import data.chessPiece.ChessPiece;
import data.chessPiece.ChessPieceList;

public class ChessBoard extends ChessBoardPanel implements MouseMotionListener,
		MouseListener, Observer {
	private int grid;
	private int X, Y, toX, toY;
	private ChessGameObservable gameObs;

	public ChessBoard(int width, int height) {
		super(width, height);
		gameObs = new ChessGameObservable();
	}

	public ChessBoard(int locationX, int locationY, int width, int height) {
		super(width, height);
		setLocation(locationX, locationY);
		gameObs = new ChessGameObservable();
	}

	public ChessGameObservable getChessGameObservable() {
		return gameObs;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int locX = (e.getX() + ((JComponent) e.getSource()).getLocation().x - grid / 2);
		int locY = (e.getY() + ((JComponent) e.getSource()).getLocation().y - grid / 2);
		setComponentZOrder(((JComponent) e.getSource()), 0);
		((JComponent) e.getSource()).setLocation(locX, locY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) { // mouse location
		X = (e.getX() + ((ChessPiece) e.getSource()).getLocation().x);
		Y = (e.getY() + ((ChessPiece) e.getSource()).getLocation().y);
		((ChessPiece) e.getSource()).setBeforeX((X - getWidthFromPanelEdge())
				/ getGridLength());
		((ChessPiece) e.getSource()).setBeforeY((Y - getHeightFromPanelEdge())
				/ getGridLength());
	}

	@Override
	public void mouseReleased(MouseEvent e) { // mouse location
		toX = (e.getX() + ((ChessPiece) e.getSource()).getLocation().x);
		toY = (e.getY() + ((ChessPiece) e.getSource()).getLocation().y);

		((ChessPiece) e.getSource()).setAfterX((toX - getWidthFromPanelEdge())
				/ getGridLength());
		((ChessPiece) e.getSource()).setAfterY((toY - getHeightFromPanelEdge())
				/ getGridLength());
		gameObs.setChanged();
		gameObs.notifyObservers(((JComponent) e.getSource()));
	}

	// observer //

	@Override
	public void update(Observable o, Object arg) {
		removeAll();
		if (o instanceof ChessPieceList) {
			if (arg instanceof ArrayList<?>) {
				for (ChessPiece chess : (ArrayList<ChessPiece>) arg) {
					this.grid = chess.getGrid();
					add(chess);
					if (!chess.getChessName().equals("cover")) {
						chess.addMouseMotionListener(this);
					}
					chess.addMouseListener(this);
				}
				repaint();
				revalidate();
			}
		}
	}
}
