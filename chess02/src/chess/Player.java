package chess;

import board.Spot;

public class Player {
	private boolean black;
	private Spot king;
	
	public Player(boolean black, Spot king) {
		this.black = black;
		this.king = king;
	}
	
	public void printStartTurn() {
		if (isWhite()) {
			System.out.print("White's move: ");
		} else {
			System.out.print("Black's move: ");
		}
	}
	
	public void printWinningStatement() {
		if (isWhite()) {
			System.out.print("White wins");
		} else {
			System.out.print("Black wins");
		}
	}
	
	//setters
	public void setKingSpot(Spot king) {
		this.king = king;
	}
	
	//getters
	public Spot getKingSpot() {
		return king;
	}
	
	public boolean isBlack() {
		return black;
	}
	
	public boolean isWhite() {
		return !isBlack();
	}
}
