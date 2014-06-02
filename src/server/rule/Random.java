package server.rule;

import server.data.chessPiece.*;

public class Random {
	/* 回傳型態 */
	private int i, j, swap, randomA;
	private String[][] chessName;
	private Chess[][] chessBoard;
	private int[] random = { 0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 15,
			16, 17, 20, 21, 22, 23, 24, 25, 26, 27, 30, 31, 32, 33, 34, 35, 36,
			37 };

	public String[][] getChessName() {
		return chessName;
	}

	public Chess[][] getChessBoard() {
		return chessBoard;
	}

	public Random() {
		chessBoard = new Chess[5][9];
		chessName = new String[5][9];
		for (i = 0; i < 32; i++) {
			randomA = (int) (Math.random() * 32);
			swap = random[i];
			random[i] = random[randomA];
			random[randomA] = swap;
		}

		i = random[0] / 10;
		j = random[0] % 10;
		chessBoard[i][j] = new Cannon(0, false, false, "Cannon", i, j);// 紅大炮
		chessName[i][j] = "redCannon";
		i = random[1] / 10;
		j = random[1] % 10;
		chessBoard[i][j] = new Cannon(0, false, false, "Cannon", i, j);
		chessName[i][j] = "redCannon";
		i = random[2] / 10;
		j = random[2] % 10;
		chessBoard[i][j] = new Cannon(1, false, false, "Cannon", i, j);// 黑大炮
		chessName[i][j] = "blackCannon";
		i = random[3] / 10;
		j = random[3] % 10;
		chessBoard[i][j] = new Cannon(1, false, false, "Cannon", i, j);
		chessName[i][j] = "blackCannon";
		i = random[4] / 10;
		j = random[4] % 10;
		chessBoard[i][j] = new Horse(0, false, false, "Horse", i, j);// 紅馬
		chessName[i][j] = "redHorse";
		i = random[5] / 10;
		j = random[5] % 10;
		chessBoard[i][j] = new Horse(0, false, false, "Horse", i, j);
		chessName[i][j] = "redHorse";
		i = random[6] / 10;
		j = random[6] % 10;
		chessBoard[i][j] = new Horse(1, false, false, "Horse", i, j);// 黑馬
		chessName[i][j] = "blackHorse";
		i = random[7] / 10;
		j = random[7] % 10;
		chessBoard[i][j] = new Horse(1, false, false, "Horse", i, j);
		chessName[i][j] = "blackHorse";
		i = random[8] / 10;
		j = random[8] % 10;
		chessBoard[i][j] = new Elephant(0, false, false, "Elephant", i, j);// 紅象
		chessName[i][j] = "redElephant";
		i = random[9] / 10;
		j = random[9] % 10;
		chessBoard[i][j] = new Elephant(0, false, false, "Elephant", i, j);
		chessName[i][j] = "redElephant";
		i = random[10] / 10;
		j = random[10] % 10;
		chessBoard[i][j] = new Elephant(1, false, false, "Elephant", i, j);// 黑象
		chessName[i][j] = "blackElephant";
		i = random[11] / 10;
		j = random[11] % 10;
		chessBoard[i][j] = new Elephant(1, false, false, "Elephant", i, j);
		chessName[i][j] = "blackElephant";
		i = random[12] / 10;
		j = random[12] % 10;
		chessBoard[i][j] = new General(0, false, false, "General", i, j);// 紅帥
		chessName[i][j] = "redGeneral";
		i = random[13] / 10;
		j = random[13] % 10;
		chessBoard[i][j] = new General(1, false, false, "General", i, j);// 黑將
		chessName[i][j] = "blackGeneral";
		i = random[14] / 10;
		j = random[14] % 10;
		chessBoard[i][j] = new Soldier(0, false, false, "Soldier", i, j);// 紅兵
		chessName[i][j] = "redSoldier";
		i = random[15] / 10;
		j = random[15] % 10;
		chessBoard[i][j] = new Soldier(0, false, false, "Soldier", i, j);
		chessName[i][j] = "redSoldier";
		i = random[16] / 10;
		j = random[16] % 10;
		chessBoard[i][j] = new Soldier(0, false, false, "Soldier", i, j);
		chessName[i][j] = "redSoldier";
		i = random[17] / 10;
		j = random[17] % 10;
		chessBoard[i][j] = new Soldier(0, false, false, "Soldier", i, j);
		chessName[i][j] = "redSoldier";
		i = random[18] / 10;
		j = random[18] % 10;
		chessBoard[i][j] = new Soldier(0, false, false, "Soldier", i, j);
		chessName[i][j] = "redSoldier";
		i = random[19] / 10;
		j = random[19] % 10;
		chessBoard[i][j] = new Chariot(0, false, false, "Chariot", i, j);// 紅車
		chessName[i][j] = "redChariot";
		i = random[20] / 10;
		j = random[20] % 10;
		chessBoard[i][j] = new Chariot(0, false, false, "Chariot", i, j);
		chessName[i][j] = "redChariot";
		i = random[21] / 10;
		j = random[21] % 10;
		chessBoard[i][j] = new Chariot(1, false, false, "Chariot", i, j);// 黑車
		chessName[i][j] = "blackChariot";
		i = random[22] / 10;
		j = random[22] % 10;
		chessBoard[i][j] = new Chariot(1, false, false, "Chariot", i, j);
		chessName[i][j] = "blackChariot";
		i = random[23] / 10;
		j = random[23] % 10;
		chessBoard[i][j] = new Soldier(1, false, false, "Soldier", i, j);// 黑卒
		chessName[i][j] = "blackSoldier";
		i = random[24] / 10;
		j = random[24] % 10;
		chessBoard[i][j] = new Soldier(1, false, false, "Soldier", i, j);
		chessName[i][j] = "blackSoldier";
		i = random[25] / 10;
		j = random[25] % 10;
		chessBoard[i][j] = new Soldier(1, false, false, "Soldier", i, j);
		chessName[i][j] = "blackSoldier";
		i = random[26] / 10;
		j = random[26] % 10;
		chessBoard[i][j] = new Soldier(1, false, false, "Soldier", i, j);
		chessName[i][j] = "blackSoldier";
		i = random[27] / 10;
		j = random[27] % 10;
		chessBoard[i][j] = new Soldier(1, false, false, "Soldier", i, j);
		chessName[i][j] = "blackSoldier";
		i = random[28] / 10;
		j = random[28] % 10;
		chessBoard[i][j] = new Advisor(0, false, false, "Advisor", i, j);// 紅士
		chessName[i][j] = "redAdvisor";
		i = random[29] / 10;
		j = random[29] % 10;
		chessBoard[i][j] = new Advisor(0, false, false, "Advisor", i, j);
		chessName[i][j] = "redAdvisor";
		i = random[30] / 10;
		j = random[30] % 10;
		chessBoard[i][j] = new Advisor(1, false, false, "Advisor", i, j);// 黑士
		chessName[i][j] = "blackAdvisor";
		i = random[31] / 10;
		j = random[31] % 10;
		chessBoard[i][j] = new Advisor(1, false, false, "Advisor", i, j);
		chessName[i][j] = "blackAdvisor";
	}
}
