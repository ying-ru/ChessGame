package server.rule;
import server.data.chessPiece.Chess;
import server.rule.ChessBoard;

public class Rule {
	Chess[][] chessBoard;
	int player0 = 0;// count score of player1
	int player1 = 0;// count score of player2
	boolean colorEqualNowplay = true;

	public boolean moveRule(boolean first, int nowPlay, ChessBoard chessBoard1, int x, int y, int toX, int toY) {
		this.chessBoard = chessBoard1.getChessBoard();
		if (!colorEqualNowplay && !first) {
			nowPlay = (nowPlay + 1) % 2;
		}
		if (x == toX && y == toY) {
			// open
			if (chessBoard[y][x].getCover() == false) {
				chessBoard[y][x].setCover(true);
				if (first && chessBoard[y][x].getColor() != nowPlay) {
					colorEqualNowplay = false;
				}
				return true;
			}
		} else {
			// check the chess which you move is right and open state
			if ((chessBoard[y][x].getCover() == true) && (chessBoard[y][x].getColor() == nowPlay)) {
				// move rule of Cannon
				if (chessBoard[y][x].getName().equalsIgnoreCase("Cannon")) {
					if (chessBoard[toY][toX] == null) {
						if (((Math.abs(x - toX)) + (Math.abs(y - toY))) > 1) {
							System.out.println("can't move");
							return false;
						} else {
							chessBoard[y][x].setX(toY);
							chessBoard[y][x].setY(toX);
							chessBoard[toY][toX] = chessBoard[y][x];
							chessBoard[y][x] = null;
							return true;
						}
					} else {  // eat rule of Cannon
						if (chessBoard[toY][toX].getCover() == true) {// target chess is open
							if (chessBoard[y][x].getColor() == chessBoard[toY][toX].getColor()) {
								System.out.println("好隊友非敵人");
								return false;
							} else {
								if ((Math.abs(x - toX) == 0) || (Math.abs(y - toY) == 0)) {
									System.out.println("Cannon eatting..");
									int count = 0; // count how many chess between two chess
									if (x == toX) { // y-direction move
										int j, k;
										if (y < toY) {
											j = y;
											k = toY;
										} else {
											j = toY;
											k = y;
										}
										for (int i = j+1; i < k; i++) {
											if (chessBoard[i][x] != null) {
												count++;
											}
										}
									} else if (y == toY) { // x-direction move
										int j, k;
										if (x < toX) {
											j = x;
											k = toX;
										} else {
											j = toX;
											k = x;
										}
										for (int i = j+1; i < k; i++) {
											if (chessBoard[y][i] != null) {
												count++;
											}
										}
									}
									if (count == 1) {
										// eat
										chessBoard[toY][toX].setDead(true);
										chessBoard[y][x].setX(toY);
										chessBoard[y][x].setY(toX);
										chessBoard[toY][toX] = chessBoard[y][x];
										chessBoard[y][x] = null;
										if (chessBoard[toY][toX].getColor() == 0) { // 0 = red
										} else { // black
											player1++;
										}
										return true;
									}
								} else {
									System.out.println("歪歪的吃子");
									return false;
								}
							}
						} else {// target chess is cover
							System.out.println("欲移動位置旗子蓋著");
							return false;
						}
					}
				}else{ // other chess
					if (((Math.abs(x - toX)) + (Math.abs(y - toY))) != 1) {
						System.out.println("走法不合規則");
						return false;  //can't move 
					} else { // other chess can move
						if (chessBoard[toY][toX] == null) {  // target location is null
							chessBoard[y][x].setX(toY);
							chessBoard[y][x].setY(toX);
							chessBoard[toY][toX] = chessBoard[y][x];
							chessBoard[y][x] = null;
							return true;
						} else {  // target location has chess
							if (chessBoard[toY][toX].getCover() == true) { //target chess is open
								if (chessBoard[toY][toX].getColor() == chessBoard[y][x].getColor()) { // teammate not rival
									System.out.println("好隊友非敵人");
									return false;
								} else { //target chess is rival and judge priority  
									if ((chessBoard[y][x].getPriority() == 1) && (chessBoard[toY][toX].getPriority() == 7)) {
										chessBoard[toY][toX].setDead(true);
										chessBoard[y][x].setX(toY);
										chessBoard[y][x].setY(toX);
										chessBoard[toY][toX] = chessBoard[y][x];
										chessBoard[y][x] = null;
										if (chessBoard[toY][toX].getColor() == 0) {  
											player0++;
										} else {
											player1++;
										}
										return true;
									} else {
										System.out.println("D " + chessBoard[toY][toX].getPriority() + " " + chessBoard[y][x].getPriority());
										System.out.println("D " + chessBoard[toY][toX].getName() + " " + chessBoard[y][x].getName());
										if (chessBoard[toY][toX].getPriority() <= chessBoard[y][x].getPriority()) {
											chessBoard[toY][toX].setDead(true);
											chessBoard[y][x].setX(toY);
											chessBoard[y][x].setY(toX);
											chessBoard[toY][toX] = chessBoard[y][x];
											chessBoard[y][x] = null;
											if (chessBoard[toY][toX].getColor() == 0) {
												player0++;
											} else {
												player1++;
											}
											return true;
										} else {
											// can't eat
											return false;
										}
									}
								}
							} else {
								return false;
							}
						}
					}
				}

			} else { // the chess which you move is illegal or is cover state
				System.out.println("the chess which you move is illegal");
				return false;
			}
		}

		return false;
	}
	public int score(int nowPlay) {
		if (nowPlay == 0) {
			return player0;
		} else {
			return player1;
		}
	}
}
