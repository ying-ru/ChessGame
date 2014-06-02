package server.data.chessPiece;

public class Cannon extends Chess {
	private static int priority = 2;

	public Cannon(int color, boolean cover, boolean dead, String name, int x,
			int y) {
		super(color, cover, dead, name, x, y, priority);
	}
}
