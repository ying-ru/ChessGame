package server.data.chessPiece;

public class Advisor extends Chess{
	private static int priority = 6;
	public Advisor(int color, boolean cover, boolean dead, String name, int x, int y){
		super(color, cover, dead, name, x, y, priority);
	}

}
