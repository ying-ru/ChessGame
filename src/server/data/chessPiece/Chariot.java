package server.data.chessPiece;

public class Chariot extends Chess {
	private static int priority = 4;

	public Chariot(int color, boolean cover, boolean dead, String name, int x,
			int y) {
		super(color, cover, dead, name, x, y, priority);
	}
}
