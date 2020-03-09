package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * @author shlomi
 * Class that represents a Knight chess piece
 */
public class Knight extends Piece {
	
	/**
	 * Constructor method for Knight
	 * @param black, true if piece black, false if piece white
	 */
	public Knight(boolean black) {
		super(black);
		String piece_name = (black) ? "bN" : "wN";
		setPieceName(piece_name);
	}
	
	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		ArrayList<Spot> possible_moves = new ArrayList<Spot>();
		int r = start.getR();
		int c = start.getC();
		
		/* Note that the rules for white and black knights are the same */
		//top 4 positions knight can go to
		if ( r >= 1 && c >= 2 && (!board[r-1][c-2].hasPiece() || isBlack() != board[r-1][c-2].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r-1, c-2));
		if ( r >= 2 && c >= 1 && (!board[r-2][c-1].hasPiece() || isBlack() != board[r-2][c-1].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r-2, c-1));
		if ( r >= 2 && c <= 6 && (!board[r-2][c+1].hasPiece() || isBlack() != board[r-2][c+1].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r-2, c+1));
		if ( r >= 1 && c <= 5 && (!board[r-1][c+2].hasPiece() || isBlack() != board[r-1][c+2].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r-1, c+2));
		
		//bottom 4 positions knight can go to
		if ( r <= 6 && c >= 2 && (!board[r+1][c-2].hasPiece() || isBlack() != board[r+1][c-2].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r+1, c-2));
		if ( r <= 5 && c >= 1 && (!board[r+2][c-1].hasPiece() || isBlack() != board[r+2][c-1].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r+2, c-1));
		if ( r <= 5 && c <= 6 && (!board[r+2][c+1].hasPiece() || isBlack() != board[r+2][c+1].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r+2, c+1));
		if ( r <= 6 && c <= 5 && (!board[r+1][c+2].hasPiece() || isBlack() != board[r+1][c+2].getPiece().isBlack()) )
			possible_moves.add(new Spot(null, r+1, c+2));
			
		
		return possible_moves;
	}
}
