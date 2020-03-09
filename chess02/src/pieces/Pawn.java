package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * @author shlomi
 * Class that represents a Pawn chess piece
 */
public class Pawn extends Piece {
	/**
	 * This field represents if a pawn can die from an en passant move
	 */
	private boolean canDieFromEnPassant;
	
	/**
	 * Constructor method for Pawn
	 * @param black, true if piece black, false if white
	 */
	public Pawn(boolean black) {
		super(black);
		String piece_name = (black) ? "bp" : "wp";
		setPieceName(piece_name);
		canDieFromEnPassant = false;
	}
	
	/**
	 * This method returns if a pawn can die from an en passant move
	 * @return
	 */
	public boolean canDieFromEnPassant() {
		return this.canDieFromEnPassant;
	}
	
	/**
	 * This method sets the status of whether a pawn can die from an en passant
	 * @param canDie, true if can die from en passant, else false
	 */
	public void setCanDieFromEnPassant(boolean canDie) {
		this.canDieFromEnPassant = canDie;
	}
	

	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		ArrayList<Spot> possible_moves = new ArrayList<Spot>();
		
		int r = start.getR();
		int c = start.getC();
		
		if (isBlack())
		{
			if (!hasMoved() && !board[r+1][c].hasPiece() && !board[r+2][c].hasPiece())
				possible_moves.add(new Spot(null, r+2, c));
			if (r <= 6 && !board[r+1][c].hasPiece())
				possible_moves.add(new Spot(null, r+1, c));
			if (c >= 1 && r <= 6 && board[r+1][c-1].hasPiece() && board[r+1][c-1].getPiece().isWhite())
				possible_moves.add(new Spot(null, r+1, c-1));
			if (c <= 6 && r <= 6 && board[r+1][c+1].hasPiece() && board[r+1][c+1].getPiece().isWhite())
				possible_moves.add(new Spot(null, r+1, c+1));
			//add en passant
			//add promotion
		}
		else
		{
			if (!hasMoved() && !board[r-1][c].hasPiece() && !board[r-2][c].hasPiece())
				possible_moves.add(new Spot(null, r-2, c));
			if (r >= 1 && !board[r-1][c].hasPiece())
				possible_moves.add(new Spot(null, r-1, c));
			if (c >= 1 && r >= 1 && board[r-1][c-1].hasPiece() && board[r-1][c-1].getPiece().isBlack())
				possible_moves.add(new Spot(null, r-1, c-1));
			if (c <= 6 && r >= 1 && board[r-1][c+1].hasPiece() && board[r-1][c+1].getPiece().isBlack())
				possible_moves.add(new Spot(null, r-1, c+1));
			//add en passant
			//add promotion
		}
		
		return possible_moves;
	}
}
