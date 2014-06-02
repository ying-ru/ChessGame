package server.data.chessPiece;

public class General extends Chess {
	private static int priority = 7;

	public General(int color, boolean cover, boolean dead, String name, int x,
			int y) {
		super(color, cover, dead, name, x, y, priority);
	}
}
