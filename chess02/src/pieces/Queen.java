package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * @author shlomi
 * Class that represents a Queen chess piece
 */
public class Queen extends Piece {
	
	/**
	 * Constructor method for Queen
	 * @param black, true if piece black, false if piece white
	 */
	public Queen(boolean black) {
		super(black);
		String piece_name = (black) ? "bQ" : "wQ";
		setPieceName(piece_name);
	}

	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		//The queen has the power of a Bishop and a Rook, take the union of both sets
		Bishop b = new Bishop(isBlack());
		Rook r = new Rook(isBlack());
		
		ArrayList<Spot> possible_moves = b.moves(board, start);
		possible_moves.addAll(r.moves(board, start));
		return possible_moves;
	}
}

