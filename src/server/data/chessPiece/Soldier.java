package server.data.chessPiece;

public class Soldier extends Chess{
	private static int priority = 1;
	public Soldier(int color, boolean cover, boolean dead, String name, int x, int y){
		super(color, cover, dead, name ,x ,y, priority);
	}

}
